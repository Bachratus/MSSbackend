package com.mss.app.repository;

import com.mss.app.domain.Task;
import com.mss.app.domain.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT t.id FROM project p JOIN subproject s ON p.id = s.project_id " + 
    "JOIN task t on s.id = t.subproject_id WHERE p.code like '0000'", nativeQuery = true)
    List<Long> findDefaultTasksIds();
}