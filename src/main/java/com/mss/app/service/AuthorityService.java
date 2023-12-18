package com.mss.app.service;

import com.mss.app.service.dto.AuthorityDTO;

import java.util.List;

public interface AuthorityService {
    List<AuthorityDTO> findAllWithoutAdmin();

    List<AuthorityDTO> findAllForUser(Long userId);
}
