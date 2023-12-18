package com.mss.app.service;

import java.time.LocalDate;
import java.util.List;

import com.mss.app.domain.TaskReport;
import com.mss.app.service.dto.TaskReportDTO;
import com.mss.app.service.dto.UserReportDTO;
import com.mss.app.service.dto.weekly_report_dtos.WeeklySummaryDTO;
import com.mss.app.service.dto.weekly_report_dtos.WeeklyTaskReportDTO;

public interface TaskReportService {

    List<TaskReportDTO> findAllByDatesForUser(LocalDate fromDate, LocalDate toDate, Long userId);

    WeeklyTaskReportDTO getEmptyWeeklyReportForTask(Long taskId, LocalDate fromDate, LocalDate toDate);

    List<WeeklySummaryDTO> getWeeklySummaries(LocalDate fromDate, LocalDate toDate);

    List<WeeklyTaskReportDTO> getWeeklyTaskReportsForUser(Long userId, LocalDate fromDate, LocalDate toDate);

    void updateWeeklyTaskReportForUser(Long userId, List<WeeklyTaskReportDTO> weeklyTaskReports);

    WeeklyTaskReportDTO addDefaultTaskAndGetEmptyReport(Long userId, Long taskId, LocalDate fromDate,
            LocalDate toDate);

    void deleteWeeklyReport(Long userId, WeeklyTaskReportDTO weeklyReport);

    TaskReportDTO save(TaskReportDTO taskReportDTO);

    List<TaskReport> findAll();

    List<UserReportDTO> getMonthlyUserReport(LocalDate fromDate, LocalDate toDate);

    byte[] getCSVForUser(LocalDate fromDate, LocalDate toDate);
}
