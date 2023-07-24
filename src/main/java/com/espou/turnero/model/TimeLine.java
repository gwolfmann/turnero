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

@Data
@AllArgsConstructor
public class TimeLine {
    private OperationHours operationHours;
    private AdditionalHours additionalHours;
    private RestrictedHours restrictedHours;
    public boolean isDateTimeInRange(LocalDate date, LocalTime time) {
        boolean isInOperationHours = operationHours.isDateTimeInRange(date, time);

        if (isInOperationHours) {
            return !restrictedHours.isDateTimeInRange(date, time);
        } else {
            return additionalHours.isDateTimeInRange(date, time);
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
