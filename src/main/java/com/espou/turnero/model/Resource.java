package com.espou.turnero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@AllArgsConstructor
public class Resource {
    private String id;
    private String name;
    private TimeLine timeline;
    @Indexed(unique = true)
    private String internalId; // Internal ID
}