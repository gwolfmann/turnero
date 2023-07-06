package com.espou.turnero.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class HourRange {
    private LocalTime beginHour;
    private LocalTime endHour;

    public boolean isTimeInRange(LocalTime time) {
        return !time.isBefore(beginHour) && !time.isAfter(endHour);
    }
}
