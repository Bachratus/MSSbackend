package com.mss.app.service.impl;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mss.app.repository.TaskRepository;
import com.mss.app.service.TaskService;
import com.mss.app.service.dto.TaskDTO;
import com.mss.app.service.mapper.TaskMapper;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<TaskDTO> findAll() {
        log.debug("TaskService findAll invoked");
        return taskMapper.toDtoList(taskRepository.findAll());
    }

    @Override
    public List<Long> findDefaultTasksIds() {
        log.debug("TaskService findDefaultTasksIds invoked");
        return taskRepository.findDefaultTasksIds();
    }
}
