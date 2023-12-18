package com.mss.app.service.dto;

import java.time.LocalDate;

public class TaskDTO {
    private Long id;

    private String name;

    private String subprojectId;

    private String subprojectCode;

    private String subprojectName;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Integer hoursPredicted;

    private String projectName;

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Integer getHoursPredicted() {
        return hoursPredicted;
    }

    public void setHoursPredicted(Integer hoursPredicted) {
        this.hoursPredicted = hoursPredicted;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubprojectId() {
        return subprojectId;
    }

    public void setSubprojectId(String subprojectId) {
        this.subprojectId = subprojectId;
    }

    public String getSubprojectCode() {
        return subprojectCode;
    }

    public void setSubprojectCode(String subprojectCode) {
        this.subprojectCode = subprojectCode;
    }

    public String getSubprojectName() {
        return subprojectName;
    }

    public void setSubprojectName(String subprojectName) {
        this.subprojectName = subprojectName;
    }
}
