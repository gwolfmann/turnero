package com.espou.turnero.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DateRange {
    private LocalDate beginDate;
    private LocalDate endDate;

    public boolean isDateInRange(LocalDate date) {
        return !date.isBefore(beginDate) && !date.isAfter(endDate);
    }
}
