package com.mss.app.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mss.app.domain.Subproject;
import com.mss.app.service.dto.SubprojectDTO;

@Mapper(componentModel = "spring", uses = {})
public interface SubprojectMapper extends EntityMapper<SubprojectDTO, Subproject> {

    @Mapping(source = "subprojectType.id", target = "subprojectTypeId")
    @Mapping(source = "project.id", target = "projectId")
    SubprojectDTO toDto(Subproject Task);

    @Mapping(source = "subprojectTypeId", target = "subprojectType.id")
    @Mapping(source = "projectId", target = "project.id")
    Subproject toEntity(SubprojectDTO TaskDTO);

    List<SubprojectDTO> toDtoList(List<Subproject> taskList);

    List<Subproject> toEntityList(List<SubprojectDTO> taskDTOList);

    default Subproject fromId(Long id) {
        if (id == null) {
            return null;
        }
        Subproject task = new Subproject();
        task.setId(id);
        return task;
    }
}
