package com.espou.turnero.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Resource {
    private String id;
    private String name;
    private TimeLine timeline;
    private String internalId; // Internal ID
}