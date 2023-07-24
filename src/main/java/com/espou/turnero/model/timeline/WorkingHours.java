package com.espou.turnero.model.timeline;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class WorkingHours {
    private HourRange monHours;
    private HourRange tueHours;
    private HourRange wenHours;
    private HourRange thuHours;
    private HourRange friHours;
    private HourRange satHours;
    private HourRange sunHours;
    public boolean isDateTimeInRange(LocalDate date, LocalTime time) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        switch (dayOfWeek) {
            case MONDAY:
                return isTimeInRange(monHours, time);
            case TUESDAY:
                return isTimeInRange(tueHours, time);
            case WEDNESDAY:
                return isTimeInRange(wenHours, time);
            case THURSDAY:
                return isTimeInRange(thuHours, time);
            case FRIDAY:
                return isTimeInRange(friHours, time);
            case SATURDAY:
                return isTimeInRange(satHours, time);
            case SUNDAY:
                return isTimeInRange(sunHours, time);
            default:
                return false;
        }
    }

    private boolean isTimeInRange(HourRange hourRange, LocalTime time) {
        return hourRange.isTimeInRange(time);
    }
}