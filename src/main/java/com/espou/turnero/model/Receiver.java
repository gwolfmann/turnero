package com.espou.turnero.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Receiver {
    private String id;
    private String name;
    private String affiliation;
    private Integer identification;
    private String internalId; // Internal ID
}
