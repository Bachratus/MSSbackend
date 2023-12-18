package com.mss.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mss.app.repository.AuthorityRepository;
import com.mss.app.service.AuthorityService;
import com.mss.app.service.dto.AuthorityDTO;
import com.mss.app.service.mapper.AuthorityMapper;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityMapper authorityMapper;
    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AuthorityMapper authorityMapper,
            AuthorityRepository authorityRepository) {
        this.authorityMapper = authorityMapper;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public List<AuthorityDTO> findAllForUser(Long userId) {
        return authorityMapper.toDto(authorityRepository.findAllForUser(userId));
    }

    @Override
    public List<AuthorityDTO> findAllWithoutAdmin() {
        return authorityMapper.toDto(authorityRepository.findAllWithoutAdmin());
    }

}
