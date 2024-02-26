package com.mss.app.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mss.app.domain.Project;
import com.mss.app.error.BadRequestAlertException;
import com.mss.app.repository.ProjectRepository;
import com.mss.app.service.dto.ProjectDTO;
import com.mss.app.service.mapper.ProjectMapper;

import jakarta.transaction.Transactional;

@Transactional
@RestController
@RequestMapping("/api")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(SubprojectResource.class);

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    public ProjectResource(ProjectRepository projectRepository,
            ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @GetMapping("/projects")
    public List<ProjectDTO> getAllProjects() {
        log.debug("REST request to get all Projects");
        return projectMapper.toDtoList(projectRepository.findAll());
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectDTO> addProject(@RequestBody ProjectDTO projectDTO) {
        if (projectDTO.getId() != null) {
            throw new BadRequestAlertException("Id null", "Project", "idnull");
        }
        ProjectDTO result = projectMapper.toDto(projectRepository.save(projectMapper.toEntity(projectDTO)));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/projects")
    public ResponseEntity<ProjectDTO> editProject(@RequestBody ProjectDTO projectDTO) {
        if (projectDTO.getId() == null) {
            throw new BadRequestAlertException("Id not null", "Project", "idnotnull");
        }
        Project project = this.projectRepository.findById(projectDTO.getId()).orElseThrow(
                () -> new BadRequestAlertException("Project not found", "Project", "projectnotfound"));
        project.setCode(projectDTO.getCode());
        project.setDateFrom(projectDTO.getDateFrom());
        project.setDateTo(projectDTO.getDateTo());
        project.setHoursPredicted(projectDTO.getHoursPredicted());
        project.setName(projectDTO.getName());

        ProjectDTO result = projectMapper.toDto(projectRepository.save(project));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<ProjectDTO> deleteProject(@PathVariable Long id) {
        Project project = this.projectRepository.findById(id).orElseThrow(
                () -> new BadRequestAlertException("Project not found", "Project", "projectnotfound"));
        if (project.getSubprojects().size() > 0) {
            throw new BadRequestAlertException("Cannot delete", "Project", "hassubprojects");
        }
        this.projectRepository.delete(project);
        return ResponseEntity
                .noContent()
                .headers(HttpHeaders.EMPTY)
                .build();
    }
}
