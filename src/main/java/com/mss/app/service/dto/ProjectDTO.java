package com.mss.app.service.dto;
import java.time.LocalDate;

public class ProjectDTO {
    private Long id;

    private String code;

    private String name;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    private Integer hoursPredicted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
