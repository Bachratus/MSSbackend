package com.mss.app.repository;

import com.mss.app.domain.Authority;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    @Query(value = "SELECT ja.* FROM users ju INNER JOIN user_authority jua ON ju.id = jua.user_id " +
    "INNER JOIN authority ja ON ja.name = jua.authority_name WHERE ju.id = :userid", nativeQuery = true)
    List<Authority> findAllForUser(@Param("userid") Long userId);

    @Query(value = "SELECT * FROM authority ja WHERE ja.name <> 'ROLE_ADMIN'", nativeQuery = true)
    List<Authority> findAllWithoutAdmin();
}
