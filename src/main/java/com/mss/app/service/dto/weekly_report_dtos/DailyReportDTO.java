package com.mss.app.service.dto.weekly_report_dtos;
import java.time.LocalDate;
import java.util.List;

import com.mss.app.service.dto.TaskReportDTO;

public class DailyReportDTO {

    private LocalDate date;

    private Double totalHours;

    private List<TaskReportDTO> taskReports;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<TaskReportDTO> getTaskReports() {
        return taskReports;
    }

    public void setTaskReports(List<TaskReportDTO> taskReports) {
        this.taskReports = taskReports;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }
}
