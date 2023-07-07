package com.espou.turnero.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Meet {
    private String id;
    private Resource resource;
    private Provider provider;
    private Receiver receiver;
    private Task task;
    private LocalDate date;
    private LocalTime hour;
    private Integer duration;
}
