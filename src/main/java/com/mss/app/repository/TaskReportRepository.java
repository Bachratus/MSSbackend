package com.mss.app.repository;

import com.mss.app.domain.TaskReport;
import com.mss.app.domain.User;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface TaskReportRepository extends JpaRepository<TaskReport, Long> {

        List<TaskReport> findAllByUserIdAndDateBetweenOrderByDateDesc(@Param("userId") Long userId,
                        @Param("dateFrom") LocalDate fromDate, @Param("dateTo") LocalDate toDate);

        List<TaskReport> findAllByUserIdAndTaskIdAndDateBetween(Long userId, Long taskId, LocalDate dateFrom,
                        LocalDate dateTo);

        List<TaskReport> findAllByUserIdAndTaskIdAndDate(Long userId, Long taskId, LocalDate date);
}
