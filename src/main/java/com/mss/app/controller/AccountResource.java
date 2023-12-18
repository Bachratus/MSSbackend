package com.mss.app.controller;

import com.mss.app.error.InvalidPasswordException;
import com.mss.app.service.UserService;
import com.mss.app.service.dto.AdminUserDTO;
import com.mss.app.service.dto.PasswordChangeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserService userService;

    public AccountResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return userService
                .getUserWithAuthorities()
                .map(AdminUserDTO::new)
                .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        log.debug("REST request to change current user's password.");
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    @GetMapping(path = "/account/forgot-password/{email}")
    public void forgotpassword(@PathVariable() String email) {
        log.debug("REST request to get new password. Forgot password case.");
        userService.forcePasswordReset(email);
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (password.length() < 4 ||
                password.length() > 100);
    }
}
