package com.mss.app.service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mss.app.domain.TaskReport;
import com.mss.app.service.dto.TaskReportDTO;

@Mapper(componentModel = "spring", uses = {})
public interface TaskReportMapper extends EntityMapper<TaskReportDTO, TaskReport> {

    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "task.name", target = "taskName")
    @Mapping(source = "task.subproject.id", target = "subprojectId")
    @Mapping(source = "task.subproject.name", target = "subprojectName")
    @Mapping(source = "task.subproject.code", target = "subprojectCode")
    @Mapping(source = "task.subproject.project.id", target = "projectId")
    @Mapping(source = "task.subproject.project.name", target = "projectName")
    @Mapping(source = "task.subproject.project.code", target = "projectCode")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "user.lastName", target = "userLastName")
    @Mapping(source = "task.subproject.subprojectType.id", target = "type")
    TaskReportDTO toDto(TaskReport taskReport);

    @Mapping(source = "taskId", target = "task.id")
    @Mapping(source = "userId", target = "user.id")
    TaskReport toEntity(TaskReportDTO taskReportDTO);

    List<TaskReportDTO> toDtoList(List<TaskReport> taskReportList);

    List<TaskReport> toEntityList(List<TaskReportDTO> taskReportDTOList);

    default TaskReport fromId(Long id) {
        if (id == null) {
            return null;
        }
        TaskReport taskReport = new TaskReport();
        taskReport.setId(id);
        return taskReport;
    }
}