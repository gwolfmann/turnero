package com.espou.turnero.model.timeline;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class WorkingHours {
    private List<HourRange> monHours;
    private List<HourRange> tueHours;
    private List<HourRange> wenHours;
    private List<HourRange> thuHours;
    private List<HourRange> friHours;
    private List<HourRange> satHours;
    private List<HourRange> sunHours;
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

    private boolean isTimeInRange(List<HourRange> hourRanges, LocalTime time) {
        for (HourRange hourRange : hourRanges) {
            if (hourRange.isTimeInRange(time)) {
                return true;
            }
        }
        return false;
    }
}