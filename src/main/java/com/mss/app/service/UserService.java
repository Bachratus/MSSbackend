package com.mss.app.service;

import com.mss.app.domain.Authority;
import com.mss.app.domain.User;
import com.mss.app.domain.UserAuthority;
import com.mss.app.error.BadRequestAlertException;
import com.mss.app.error.EmailAlreadyUsedException;
import com.mss.app.error.InvalidPasswordException;
import com.mss.app.error.UsernameAlreadyUsedException;
import com.mss.app.repository.AuthorityRepository;
import com.mss.app.repository.UserAuthorityRepository;
import com.mss.app.repository.UserRepository;
import com.mss.app.security.AuthoritiesConstants;
import com.mss.app.security.SecurityUtil;
import com.mss.app.service.dto.UserDTO;
import com.mss.app.service.mapper.UserMapper;
import com.mss.app.tools.PasswordGenerator;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final JavaMailSender emailSender;

    private final UserMapper userMapper;

    private final UserAuthorityRepository userAuthorityRepository;

    private final LocalLogsService localLogsService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthorityRepository authorityRepository,
            JavaMailSender emailSender,
            UserMapper userMapper,
            UserAuthorityRepository userAuthorityRepository,
            LocalLogsService localLogsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.emailSender = emailSender;
        this.userMapper = userMapper;
        this.userAuthorityRepository = userAuthorityRepository;
        this.localLogsService = localLogsService;
    }

    public List<UserDTO> findAll() {
        return userMapper.toDTOList(userRepository.findAllByActiveTrue());
    }

    @Transactional
    public void registerUser(UserDTO userDTO) {
        userRepository
                .findOneByLogin(userDTO.getLogin().toLowerCase())
                .ifPresent(user -> {
                    throw new UsernameAlreadyUsedException();
                });
        userRepository
                .findOneByEmailIgnoreCase(userDTO.getEmail())
                .ifPresent(user -> {
                    throw new EmailAlreadyUsedException();
                });
        User newUser = new User();
        String password = PasswordGenerator.generateRandomPassword();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        User createdUser = userRepository.save(newUser);

        localLogsService.registerUser(createdUser);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(createdUser.getEmail());
        message.setSubject("MSS - Registration");
        message.setText("You have been registered in MSS system. \nYour login: " + createdUser.getLogin()
                + "\nYour password: " + password);
        emailSender.send(message);
    }

    @Transactional
    public void forcePasswordReset(String email) {
        User user = userRepository.findOneByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadRequestAlertException("User with provided email not found", "User",
                        "emailnotfound"));
        String password = PasswordGenerator.generateRandomPassword();
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        User updatedUser = userRepository.save(user);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(updatedUser.getEmail());
        message.setSubject("MSS - Password Reset");
        message.setText("Your password in MSS system has been changed. \nYour new password: " + password);
        emailSender.send(message);
    }

    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).get();
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        User editedUser = userRepository.save(user);

        localLogsService.updateUser(editedUser);

        return userMapper.toDTO(editedUser);
    }

    @Transactional
    public UserDTO updateUserAuthorities(Long userId, List<Authority> authorities) {
        User user = userRepository.findOneWithAuthoritiesById(userId).get();
        List<UserAuthority> userAuthoritiesToRemove = user.getUserAuthorities().stream()
                .filter(userAuthority -> !authorities.contains(userAuthority.getAuthority()))
                .collect(Collectors.toList());
        userAuthorityRepository.deleteAll(userAuthoritiesToRemove);
        User userWithReducedAuthorities = userRepository.findOneWithAuthoritiesById(userId).get();
        Set<Authority> authoritiesSet = new HashSet<>(authorities);
        userWithReducedAuthorities.setAuthorities(authoritiesSet);
        return userMapper.toDTO(userRepository.save(userWithReducedAuthorities));
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtil
                .getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin)
                .ifPresent(user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword,
                            currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    log.debug("Changed password for User: {}", user);
                });
    }

    public void deleteUser(String login) {
        userRepository
                .findOneByLogin(login)
                .ifPresent(user -> {
                    user.setActive(false);
                    userRepository.save(user);
                    log.debug("Deleted User: {}", user);
                });
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtil.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }
}
