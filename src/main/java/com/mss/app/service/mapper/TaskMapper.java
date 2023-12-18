package com.mss.app.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mss.app.domain.Task;
import com.mss.app.service.dto.TaskDTO;

@Mapper(componentModel = "spring", uses = {})
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {

    @Mapping(source = "subproject.id", target = "subprojectId")
    @Mapping(source = "subproject.name", target = "subprojectName")
    @Mapping(source = "subproject.code", target = "subprojectCode")
    @Mapping(source = "subproject.project.name", target = "projectName")
    TaskDTO toDto(Task Task);

    @Mapping(source = "subprojectId", target = "subproject.id")
    @Mapping(target = "tasksReports", ignore = true)
    Task toEntity(TaskDTO TaskDTO);

    List<TaskDTO> toDtoList(List<Task> taskList);

    List<Task> toEntityList(List<TaskDTO> taskDTOList);

    default Task fromId(Long id) {
        if (id == null) {
            return null;
        }
        Task task = new Task();
        task.setId(id);
        return task;
    }
}
