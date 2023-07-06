package com.espou.turnero.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class TimeLine {
    private List<OperationHours> operationHours;
    private List<AdditionalHours> additionalHours;
    private List<RestrictedHours> restrictedHours;
    public boolean isDateTimeInRange(LocalDate date, LocalTime time) {
        boolean isInOperationHours = operationHours.stream()
                .anyMatch(operationHour -> operationHour.isDateTimeInRange(date, time));

        if (isInOperationHours) {
            return !restrictedHours.stream()
                    .anyMatch(restrictedHour -> restrictedHour.isDateTimeInRange(date, time));
        } else {
            return additionalHours.stream()
                    .anyMatch(additionalHour -> additionalHour.isDateTimeInRange(date, time));
        }
    }
}
