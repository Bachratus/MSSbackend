package com.mss.app.service;

import java.util.List;

import com.mss.app.service.dto.TaskDTO;

public interface TaskService {
    List<TaskDTO> findAll();
    List<Long> findDefaultTasksIds();
}
