package com.mss.app.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class UserReportDTO implements Serializable {
    private LocalDate date;
    private Double hours;
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }
}
