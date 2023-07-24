package com.espou.turnero.model;


import com.espou.turnero.model.timeline.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeLineTest {

    @Test
    public void testDateTimeInRange_OperationHours() {
        // Arrange
        OperationHours operationHours = new OperationHours(
                new DateRange(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 12, 31)),
                new WorkingHours(
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(17, 0)),
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(17, 0)),
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(17, 0)),
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(17, 0)),
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(17, 0)),
                                createHourRange(LocalTime.of(10, 0), LocalTime.of(14, 0)),
                                createHourRange(LocalTime.of(10, 0), LocalTime.of(14, 0))
                        )
        );


        RestrictedHours restrictedHours = new RestrictedHours(
                new DateRange(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 12, 31)),
                new WorkingHours(
                        createHourRange(LocalTime.of(12, 0), LocalTime.of(13, 0)),
                                createHourRange(LocalTime.of(12, 0), LocalTime.of(13, 0)),
                                createHourRange(LocalTime.of(12, 0), LocalTime.of(13, 0)),
                                createHourRange(LocalTime.of(12, 0), LocalTime.of(13, 0)),
                                createHourRange(LocalTime.of(12, 0), LocalTime.of(13, 0)),
                                createHourRange(LocalTime.of(12, 0), LocalTime.of(13, 0)),
                                createHourRange(LocalTime.of(12, 0), LocalTime.of(13, 0))
                        )
                );

        AdditionalHours additionalHours = null;

        TimeLine timeLine = new TimeLine(operationHours, additionalHours, restrictedHours);

        // Act
        boolean result = timeLine.isDateTimeInRange(LocalDate.of(2022, 6, 1), LocalTime.of(11, 0));

        // Assert
        Assertions.assertTrue(result);
    }
/*
    @Test
    public void testDateTimeInRange_AdditionalHours() {
        // Arrange
        List<OperationHours> operationHours = new ArrayList<>();

        List<AdditionalHours> additionalHours = new ArrayList<>();
        additionalHours.add(new AdditionalHours(
                new DateRange(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 12, 31)),
                new WorkingHours(
                        Arrays.asList(
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(17, 0))
                        ),
                        Arrays.asList(
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(17, 0))
                        ),
                        Arrays.asList(
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(17, 0))
                        ),
                        Arrays.asList(
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(17, 0))
                        ),
                        Arrays.asList(
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(17, 0))
                        ),
                        Arrays.asList(
                                createHourRange(LocalTime.of(10, 0), LocalTime.of(14, 0))
                        ),
                        Arrays.asList(
                                createHourRange(LocalTime.of(10, 0), LocalTime.of(14, 0))
                        )
                )
        ));

        List<RestrictedHours> restrictedHours = new ArrayList<>();

        TimeLine timeLine = new TimeLine(operationHours, additionalHours, restrictedHours);

        // Act
        boolean result = timeLine.isDateTimeInRange(LocalDate.of(2022, 6, 1), LocalTime.of(10, 0));

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    public void testDateTimeInRange_RestrictedHours() {
        // Arrange
        List<OperationHours> operationHours = new ArrayList<>();

        List<AdditionalHours> additionalHours = new ArrayList<>();

        List<RestrictedHours> restrictedHours = new ArrayList<>();
        restrictedHours.add(new RestrictedHours(
                new DateRange(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 12, 31)),
                new WorkingHours(Arrays.asList(
                        createHourRange(LocalTime.of(9, 0), LocalTime.of(12, 0))
                ),
                        Arrays.asList(
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(12, 0))
                        ),
                        Arrays.asList(
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(12, 0))
                        ),
                        Arrays.asList(
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(12, 0))
                        ),
                        Arrays.asList(
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(12, 0))
                        ),
                        Arrays.asList(
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(12, 0))
                        ),
                        Arrays.asList(
                                createHourRange(LocalTime.of(9, 0), LocalTime.of(12, 0))
                )
        )));

        TimeLine timeLine = new TimeLine(operationHours, additionalHours, restrictedHours);

        // Act
        boolean result = timeLine.isDateTimeInRange(LocalDate.of(2022, 6, 1), LocalTime.of(10, 0));

        // Assert
        Assertions.assertFalse(result);
    }
*/
    private HourRange createHourRange(LocalTime beginHour, LocalTime endHour) {
        return new HourRange(beginHour, endHour);
    }
}
