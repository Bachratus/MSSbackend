package com.mss.app.repository;

import com.mss.app.domain.Project;
import com.mss.app.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {}
