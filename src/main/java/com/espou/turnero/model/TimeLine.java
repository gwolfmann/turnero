package com.espou.turnero.model;

import com.espou.turnero.model.timeline.AdditionalHours;
import com.espou.turnero.model.timeline.OperationHours;
import com.espou.turnero.model.timeline.RestrictedHours;
import com.espou.turnero.storage.MeetDTO;
import com.espou.turnero.storage.MeetMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
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
    public boolean isMeetOnRange(Meet meet) {
        LocalDateTime meetStartDateTime = LocalDateTime.of(meet.getDate(), meet.getHour());
        LocalDateTime meetEndDateTime = meetStartDateTime.plusMinutes(meet.getDuration());

        boolean isStartInRange = isDateTimeInRange(meetStartDateTime.toLocalDate(), meetStartDateTime.toLocalTime());
        boolean isEndInRange = isDateTimeInRange(meetEndDateTime.toLocalDate(), meetEndDateTime.toLocalTime());

        return isStartInRange && isEndInRange;
    }

    public boolean isMeetOnRange(MeetDTO meetDTO) {
        return isMeetOnRange(MeetMapper.toEntity(meetDTO));
    }


}
