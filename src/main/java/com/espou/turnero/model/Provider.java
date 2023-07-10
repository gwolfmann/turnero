package com.espou.turnero.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Provider {
    private String id;
    private String name;
    private TimeLine timeline;
    private Resource defaultResource;
    private Task defaultTask;
    private String internalId;
}

