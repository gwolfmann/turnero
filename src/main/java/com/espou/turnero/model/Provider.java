package com.espou.turnero.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Provider {
    private String id;
    private String name;
    private TimeLine timeline;
//    private Resource defaultResource;
//    private Task defaultTask;
    private String internalId;
}

