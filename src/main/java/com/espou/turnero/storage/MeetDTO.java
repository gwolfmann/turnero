package com.espou.turnero.storage;

import com.espou.turnero.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@Document(collection = "meets")
public class MeetDTO {
    @Id
    private String id;
    private String resourceInternalId;
    private String providerInternalId;
    private String receiverInternalId;
    private Task task;
    private LocalDate date;
    private LocalTime hour;
    private Integer duration;
    @Indexed(unique = true)
    private String internalId;
    private User lastUser;
    private String receiverName;

    public LocalTime getEndTime() {
        return hour.plusMinutes(duration);
    }
}