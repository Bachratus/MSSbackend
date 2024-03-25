package com.mss.app.repository;

import com.mss.app.domain.SubprojectType;
import com.mss.app.domain.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface SubprojectTypeRepository extends JpaRepository<SubprojectType, Long> {

    @Query(value = "SELECT * FROM subproject_type st where st.id = 8 limit 1", nativeQuery = true)
    Optional<SubprojectType> getOvertimeSubprojectType();

    @Query(value = "SELECT * FROM subproject_type st where st.id = 7 limit 1", nativeQuery = true)
    Optional<SubprojectType> getExitsSubprojectType();
}
