package com.mss.app.service.dto.weekly_report_dtos;
import java.util.List;

public class WeeklyTaskReportDTO {

    private Long taskId;

    private String projectName;

    private String subprojectName;

    private String taskName;

    private Long subprojectType;

    private String subprojectCode;

    private List<DailyReportDTO> dailyReports;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public List<DailyReportDTO> getDailyReports() {
        return dailyReports;
    }

    public void setDailyReports(List<DailyReportDTO> dailyReports) {
        this.dailyReports = dailyReports;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String taskName) {
        this.projectName = taskName;
    }

    public String getSubprojectName() {
        return subprojectName;
    }

    public void setSubprojectName(String subprojectName) {
        this.subprojectName = subprojectName;
    }

    public Long getSubprojectType() {
        return subprojectType;
    }

    public void setSubprojectType(Long subprojectType) {
        this.subprojectType = subprojectType;
    }

    public String getSubprojectCode() {
        return subprojectCode;
    }

    public void setSubprojectCode(String subprojectCode) {
        this.subprojectCode = subprojectCode;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
