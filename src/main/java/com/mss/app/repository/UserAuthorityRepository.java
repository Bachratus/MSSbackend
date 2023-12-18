package com.mss.app.repository;

import com.mss.app.domain.User;
import com.mss.app.domain.UserAuthority;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {

    List<UserAuthority> findAllByUserId(Long userId);
}
