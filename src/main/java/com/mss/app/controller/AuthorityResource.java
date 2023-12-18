package com.mss.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mss.app.service.AuthorityService;
import com.mss.app.service.dto.AuthorityDTO;

@RestController
@RequestMapping("/api")
public class AuthorityResource {

    private final Logger log = LoggerFactory.getLogger(AuthorityResource.class);

    private final AuthorityService authorityService;

    public AuthorityResource(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping("/authority")
    public List<AuthorityDTO> getAuthorities() {
        log.debug("REST request to get all authorities");
        return authorityService.findAllWithoutAdmin();
    }

    @GetMapping("/authority/{userId}")
    public List<AuthorityDTO> getAuthorities(@PathVariable("userId") Long userId) {
        log.debug("REST request to get all authorities for userId: " + userId);
        return authorityService.findAllForUser(userId);
    }
}
