package com.espou.turnero.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Resource {
    private String id;
    private String name;
    private TimeLine timeline;
}