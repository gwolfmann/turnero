package com.espou.turnero.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class RestrictedHours {
    private DateRange dateRange;
    private WorkingHours workingHours;
    public boolean isDateTimeInRange(LocalDate date, LocalTime time) {
        return dateRange.isDateInRange(date) && workingHours.isDateTimeInRange(date, time);
    }
}
