package com.mss.app.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.mss.app.domain.Project;
import com.mss.app.service.dto.ProjectDTO;

@Mapper(componentModel = "spring", uses = {})
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {

    ProjectDTO toDto(Project Task);

    Project toEntity(ProjectDTO TaskDTO);

    List<ProjectDTO> toDtoList(List<Project> taskList);

    List<Project> toEntityList(List<ProjectDTO> taskDTOList);

    default Project fromId(Long id) {
        if (id == null) {
            return null;
        }
        Project task = new Project();
        task.setId(id);
        return task;
    }
}
