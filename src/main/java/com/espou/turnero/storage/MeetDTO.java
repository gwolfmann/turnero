package com.espou.turnero.storage;

import com.espou.turnero.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@Document(collection = "meets")
public class MeetDTO {
    @Id
    private String id;
    private Resource resource;
    private Provider provider;
    private Receiver receiver;
    private Task task;
    private LocalDate date;
    private LocalTime hour;
    private Integer duration;
}