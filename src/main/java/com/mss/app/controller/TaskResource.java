package com.mss.app.controller;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.mss.app.service.TaskService;
import com.mss.app.service.dto.TaskDTO;

/**
 * REST controller for managing {@link pl.com.ente.swz.domain.Task}.
 */
@RestController
@RequestMapping("/api")
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    private final TaskService taskService;

    public TaskResource(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public List<TaskDTO> getAllTasks() {
        log.debug("REST request to get all Tasks");
        return taskService.findAll();
    }

    @GetMapping("/tasks/default")
    public List<Long> getDefaultTasksIds() {
        log.debug("REST request to get default Tasks ids");
        return taskService.findDefaultTasksIds();
    }
}

    

                 