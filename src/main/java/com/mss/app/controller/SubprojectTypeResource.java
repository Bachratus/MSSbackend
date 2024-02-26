package com.mss.app.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.mss.app.repository.SubprojectTypeRepository;
import com.mss.app.service.dto.SubprojectTypeDTO;
import com.mss.app.service.mapper.SubprojectTypeMapper;

import jakarta.transaction.Transactional;

@Transactional
@RestController
@RequestMapping("/api")
public class SubprojectTypeResource {

    private final Logger log = LoggerFactory.getLogger(SubprojectResource.class);

    private final SubprojectTypeRepository subprojectTypeRepository;

    private final SubprojectTypeMapper subprojectTypeMapper;

    public SubprojectTypeResource(SubprojectTypeRepository subprojectTypeRepository,
            SubprojectTypeMapper subprojectTypeMapper) {
        this.subprojectTypeRepository = subprojectTypeRepository;
        this.subprojectTypeMapper = subprojectTypeMapper;
    }

    @GetMapping("/subproject-type")
    public List<SubprojectTypeDTO> getAllSubprojectTypes() {
        log.debug("REST request to get all subproject types");
        return this.subprojectTypeMapper.toDtoList(subprojectTypeRepository.findAll());
    }
}
