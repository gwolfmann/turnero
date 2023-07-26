package com.espou.turnero.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Receiver {
    private String id;
    private String name;
    private String affiliation;
    private Integer identification;
    private String email;
    private String telephone;
    private String internalId; // Internal ID
}
