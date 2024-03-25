package com.mss.app.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.mss.app.domain.SubprojectType;
import com.mss.app.service.dto.SubprojectTypeDTO;

@Mapper(componentModel = "spring", uses = {})
public interface SubprojectTypeMapper extends EntityMapper<SubprojectTypeDTO, SubprojectType> {

    SubprojectTypeDTO toDto(SubprojectType Task);

    SubprojectType toEntity(SubprojectTypeDTO TaskDTO);

    List<SubprojectTypeDTO> toDtoList(List<SubprojectType> taskList);

    List<SubprojectType> toEntityList(List<SubprojectTypeDTO> taskDTOList);

    default SubprojectType fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubprojectType task = new SubprojectType();
        task.setId(id);
        return task;
    }
}
