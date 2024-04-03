package com.mss.app.enums;

public enum WorkDayStatus {
    VERY_LOW_HOURS("Bardzo mała liczba przepracowanych godzin", 1),
    LOW_HOURS("Mała liczba przepracowanych godzin", 2),
    AVERAGE_HOURS("Przeciętna liczba przepracowanych godzin", 3),
    HIGH_HOURS("Duża liczba przepracowanych godzin", 4),
    VERY_HIGH_HOURS("Bardzo duża liczba przepracowanych godzin", 5),
    OVERTIME_ON_DAY_OFF("Nadgodziny w dniu wolnym od pracy", 6),
    NO_WORK_ON_DAY_OFF("Brak godzin pracy w dniu wolnym od pracy", 7);

    private final String description;

    private final Integer value;

    WorkDayStatus(String description, Integer value) {
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public Integer getValue() {
        return value;
    }
}