package com.mss.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mss.app.domain.Subproject;
import com.mss.app.error.BadRequestAlertException;
import com.mss.app.repository.SubprojectRepository;
import com.mss.app.repository.SubprojectTypeRepository;
import com.mss.app.service.dto.SubprojectDTO;
import com.mss.app.service.mapper.SubprojectMapper;

import jakarta.transaction.Transactional;

@Transactional
@RestController
@RequestMapping("/api")
public class SubprojectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    private final SubprojectRepository subprojectRepository;

    private final SubprojectMapper subprojectMapper;

    private final SubprojectTypeRepository subprojectTypeRepository;

    public SubprojectResource(SubprojectRepository subprojectRepository,
            SubprojectMapper subprojectMapper,
            SubprojectTypeRepository subprojectTypeRepository) {
        this.subprojectRepository = subprojectRepository;
        this.subprojectMapper = subprojectMapper;
        this.subprojectTypeRepository = subprojectTypeRepository;
    }

    @GetMapping("/subprojects/{projectId}")
    public List<SubprojectDTO> getAllSubprojects(@PathVariable Long projectId) {
        log.debug("REST request to get all Subprojects");
        return subprojectMapper.toDtoList(subprojectRepository.findAll().stream()
                .filter(subproject -> subproject.getProject() != null && subproject.getProject().getId() == projectId)
                .collect(Collectors.toList()));
    }

    @PostMapping("/subprojects")
    public ResponseEntity<SubprojectDTO> addSubproject(@RequestBody SubprojectDTO subprojectDTO) {
        if (subprojectDTO.getId() != null) {
            throw new BadRequestAlertException("Id null", "Subproject", "idnull");
        }
        SubprojectDTO result = subprojectMapper
                .toDto(subprojectRepository.save(subprojectMapper.toEntity(subprojectDTO)));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/subprojects")
    public ResponseEntity<SubprojectDTO> editSubproject(@RequestBody SubprojectDTO subprojectDTO) {
        if (subprojectDTO.getId() == null) {
            throw new BadRequestAlertException("Id not null", "Subproject", "idnotnull");
        }
        Subproject subproject = this.subprojectRepository.findById(subprojectDTO.getId()).orElseThrow(
                () -> new BadRequestAlertException("Subroject not found", "Subproject", "subprojectnotfound"));
        subproject.setCode(subprojectDTO.getCode());
        subproject.setDateFrom(subprojectDTO.getDateFrom());
        subproject.setDateTo(subprojectDTO.getDateTo());
        subproject.setHoursPredicted(subprojectDTO.getHoursPredicted());
        subproject.setName(subprojectDTO.getName());
        subproject.setSubprojectType(
                this.subprojectTypeRepository.findById(subprojectDTO.getSubprojectTypeId()).orElseThrow(
                        () -> new BadRequestAlertException("SubprojectType not found", "Subproject", "typenotfound")));

        SubprojectDTO result = subprojectMapper.toDto(subprojectRepository.save(subproject));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/subprojects/{id}")
    public ResponseEntity<SubprojectDTO> deleteSubproject(@PathVariable Long id) {
        Subproject subproject = this.subprojectRepository.findById(id).orElseThrow(
                () -> new BadRequestAlertException("Project not found", "Subproject", "subprojectnotfound"));
        if (subproject.getTasks().size() > 0) {
            throw new BadRequestAlertException("Cannot delete", "Subproject", "hastasks");
        }
        this.subprojectRepository.delete(subproject);
        return ResponseEntity
                .noContent()
                .headers(HttpHeaders.EMPTY)
                .build();
    }
}
