package com.mss.app.repository;

import com.mss.app.domain.DefaultTask;
import com.mss.app.domain.User;
import com.mss.app.domain.keys.UserTaskKey;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface DefaultTaskRepository extends JpaRepository<DefaultTask, Long> {

    Optional<DefaultTask> findById(UserTaskKey id);

    @Query(value = "SELECT * FROM default_task where user_id = :userId", nativeQuery = true)
    List<DefaultTask> findAllByUserId(Long userId);
}
