package com.mss.app.tools;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class FreeDays {
    public static boolean isDayFreeOfWork(LocalDate date, boolean weekendsFreeOfWork) {
        if (weekendsFreeOfWork) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                return true;
            }
        }

        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        int[][] staticHolidays = {
                { 1, 1 }, { 1, 6 }, { 5, 1 }, { 5, 3 }, { 8, 15 }, { 11, 1 }, { 11, 11 }, { 12, 25 }, { 12, 26 }
        };

        for (int[] holiday : staticHolidays) {
            if (month == holiday[0] && day == holiday[1]) {
                return true;
            }
        }

        LocalDate easterSunday = calculateEasterSunday(date.getYear());
        if (date.equals(easterSunday)) {
            return true;
        } else if (date.equals(easterSunday.plusDays(1))) {
            return true;
        } else if (date.equals(easterSunday.plusDays(49))) {
            return true;
        } else if (date.equals(easterSunday.plusDays(60))) {
            return true;
        }

        return false;
    }

    private static LocalDate calculateEasterSunday(int year) {
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int month = (h + l - 7 * m + 114) / 31;
        int day = ((h + l - 7 * m + 114) % 31) + 1;
        return LocalDate.of(year, month, day);
    }
}
