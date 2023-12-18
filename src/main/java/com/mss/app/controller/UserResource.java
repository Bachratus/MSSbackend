package com.mss.app.controller;


import com.mss.app.domain.Authority;
import com.mss.app.service.UserService;
import com.mss.app.service.dto.UserDTO;
import jakarta.validation.Valid;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
        return ResponseEntity
                .noContent()
                .headers(HttpHeaders.EMPTY)
                .build();
    }

    @PutMapping("/users")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        log.debug("REST request to update User: {}", userDTO.getLogin());
        UserDTO updatedUser = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/users/user-authorities/{userId}")
    public ResponseEntity<UserDTO> updateUserAuthorities(@PathVariable Long userId, @RequestBody List<Authority> authorities) {
        log.debug("REST request to update authorities for User: {}", userId);
        UserDTO updatedUser = userService.updateUserAuthorities(userId, authorities);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{login}")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity
                .noContent()
                .headers(HttpHeaders.EMPTY)
                .build();
    }

    @GetMapping("/users")
    public List<UserDTO> getAllPublicUsers() {
        log.debug("REST request to get all users");
        return userService.findAll();
    }
}
