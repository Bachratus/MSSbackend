package com.mss.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mss.app.domain.DefaultTask;
import com.mss.app.domain.Task;
import com.mss.app.domain.User;
import com.mss.app.error.BadRequestAlertException;
import com.mss.app.repository.DefaultTaskRepository;
import com.mss.app.repository.TaskRepository;
import com.mss.app.repository.UserRepository;
import com.mss.app.security.SecurityUtil;
import com.mss.app.service.dto.TaskDTO;
import com.mss.app.service.mapper.TaskMapper;

import jakarta.transaction.Transactional;

/**
 * REST controller for managing {@link pl.com.ente.swz.domain.Task}.
 */
@Transactional
@RestController
@RequestMapping("/api")
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final UserRepository userRepository;

    private final DefaultTaskRepository defaultTaskRepository;

    public TaskResource(TaskRepository taskRepository,
            TaskMapper taskMapper,
            UserRepository userRepository,
            DefaultTaskRepository defaultTaskRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
        this.defaultTaskRepository = defaultTaskRepository;
    }

    @GetMapping("/tasks/for-user")
    public List<TaskDTO> getTaskAssignedToUser() {
        User user = SecurityUtil
                .getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin).get();
        List<DefaultTask> defaultTasksForUser = defaultTaskRepository.findAllByUserId(user.getId());
        List<TaskDTO> tasksAssigned = defaultTasksForUser.stream()
                .map(def -> taskMapper.toDto(taskRepository.findById(def.getTask().getId()).get()))
                .collect(Collectors.toList());
        return tasksAssigned;
    }

    @GetMapping("/tasks")
    public List<TaskDTO> getAllTasks() {
        log.debug("REST request to get all Tasks");
        return taskMapper.toDtoList(taskRepository.findAll());
    }

    @GetMapping("/tasks/{subprojectId}")
    public List<TaskDTO> getAllTasksForSubproject(@PathVariable Long subprojectId) {
        log.debug("REST request to get all Tasks");
        return taskMapper.toDtoList(taskRepository.findAll().stream()
                .filter(task -> task.getSubproject().getId() == subprojectId).collect(Collectors.toList()));
    }

    @GetMapping("/tasks/default")
    public List<Long> getDefaultTasksIds() {
        log.debug("REST request to get default Tasks ids");
        return taskRepository.findDefaultTasksIds();
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> addTask(@RequestBody TaskDTO taskDTO) {
        if (taskDTO.getId() != null) {
            throw new BadRequestAlertException("Id null", "Task", "idnull");
        }
        TaskDTO result = taskMapper.toDto(taskRepository.save(taskMapper.toEntity(taskDTO)));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/tasks")
    public ResponseEntity<TaskDTO> editTask(@RequestBody TaskDTO taskDTO) {
        if (taskDTO.getId() == null) {
            throw new BadRequestAlertException("Id not null", "Task", "idnotnull");
        }
        Task task = this.taskRepository.findById(taskDTO.getId()).orElseThrow(
                () -> new BadRequestAlertException("Task not found", "Task", "tasknotfound"));

        task.setName(taskDTO.getName());
        task.setFromDate(taskDTO.getFromDate());
        task.setToDate(taskDTO.getToDate());
        task.setHoursPredicted(taskDTO.getHoursPredicted());

        TaskDTO result = taskMapper.toDto(taskRepository.save(task));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> deleteTask(@PathVariable Long id) {
        Task task = this.taskRepository.findById(id).orElseThrow(
                () -> new BadRequestAlertException("Task not found", "Task", "tasknotfound"));
        if (task.getTasksReports().size() > 0) {
            throw new BadRequestAlertException("Cannot delete", "Task", "hasreports");
        }
        this.taskRepository.delete(task);
        return ResponseEntity
                .noContent()
                .headers(HttpHeaders.EMPTY)
                .build();
    }
}
