package com.mss.app.controller;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mss.app.domain.User;
import com.mss.app.service.TaskReportService;
import com.mss.app.service.UserService;
import com.mss.app.service.dto.UserReportDTO;
import com.mss.app.service.dto.weekly_report_dtos.WeeklySummaryDTO;
import com.mss.app.service.dto.weekly_report_dtos.WeeklyTaskReportDTO;

@RestController
@RequestMapping("/api")
public class TaskReportResource {
    private final Logger log = LoggerFactory.getLogger(TaskReportResource.class);

    private final TaskReportService taskReportService;

    private final UserService userService;

    public TaskReportResource(TaskReportService taskReportService, UserService userService) {
        this.taskReportService = taskReportService;
        this.userService = userService;
    }

    @GetMapping("/task-reports/for-user/{fromDate}/{toDate}")
    public List<UserReportDTO> getUserReport(@PathVariable LocalDate fromDate, @PathVariable LocalDate toDate) {
        return taskReportService.getMonthlyUserReport(fromDate, toDate);
    }

    @PostMapping(value = "/task-reports/summary-csv/{fromDate}/{toDate}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getCSV(@PathVariable LocalDate fromDate, @PathVariable LocalDate toDate) {
        User user = userService.getUserWithAuthorities().get();

        byte[] fileBytes = taskReportService.getCSVForUser(fromDate, toDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(
                "attachment",
                String.format("%s_%s_ewid_%s_%s",
                        user.getFirstName(),
                        user.getLastName(),
                        fromDate.getYear(),
                        fromDate.getMonthValue()) + ".csv");

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/task-reports/weekly/{fromDate}/{toDate}")
    public List<WeeklySummaryDTO> getWeeklySummaries(@PathVariable LocalDate fromDate,
            @PathVariable LocalDate toDate) {
        log.debug("REST request to get summary for service");
        return this.taskReportService.getWeeklySummaries(fromDate, toDate);
    }

    @GetMapping("/task-reports/weekly/{userId}/{fromDate}/{toDate}")
    public List<WeeklyTaskReportDTO> getWeeklyTaskReports(@PathVariable Long userId, @PathVariable LocalDate fromDate,
            @PathVariable LocalDate toDate) {
        log.debug("REST request to get Totals for user: {}", userId, fromDate, toDate);
        return this.taskReportService.getWeeklyTaskReportsForUser(userId, fromDate, toDate);
    }

    @PutMapping("/task-reports/weekly/{userId}")
    public ResponseEntity<?> updateWeeklyReport(@PathVariable Long userId,
            @RequestBody List<WeeklyTaskReportDTO> weeklyTaskReports)
            throws URISyntaxException {
        log.debug("REST request to update weeklyTaskReport:", weeklyTaskReports);
        try {
            taskReportService.updateWeeklyTaskReportForUser(userId, weeklyTaskReports);
            return ResponseEntity
                    .noContent()
                    .headers(HttpHeaders.EMPTY)
                    .build();
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    @PostMapping("/task-reports/add-weekly-report/{userId}/{taskId}/{fromDate}/{toDate}")
    public ResponseEntity<WeeklyTaskReportDTO> addDefaultTaskAndGetEmptyReport(@PathVariable Long userId,
            @PathVariable Long taskId,
            @PathVariable LocalDate fromDate,
            @PathVariable LocalDate toDate) throws URISyntaxException {
        WeeklyTaskReportDTO result = this.taskReportService.addDefaultTaskAndGetEmptyReport(userId, taskId,
                fromDate, toDate);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/task-reports/delete-weekly-report/{userId}")
    public ResponseEntity<Void> deleteWeeklyReport(@RequestBody WeeklyTaskReportDTO weeklyReport,
            @PathVariable Long userId) {
        taskReportService.deleteWeeklyReport(userId, weeklyReport);
        return ResponseEntity
                .noContent()
                .headers(HttpHeaders.EMPTY)
                .build();
    }
}
