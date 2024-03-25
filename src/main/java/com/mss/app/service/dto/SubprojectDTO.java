package com.mss.app.service.dto;
import java.time.LocalDate;

public class SubprojectDTO {
    private Long id;

    private String name;

    private String code;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    private Integer hoursPredicted;

    private Long subprojectTypeId;

    private Long projectId;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getHoursPredicted() {
        return hoursPredicted;
    }

    public void setHoursPredicted(Integer hoursPredicted) {
        this.hoursPredicted = hoursPredicted;
    }

    public Long getSubprojectTypeId() {
        return subprojectTypeId;
    }

    public void setSubprojectTypeId(Long subprojectTypeId) {
        this.subprojectTypeId = subprojectTypeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
