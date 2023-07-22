package com.espou.turnero.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String id;
    private String name;
    private String email;
    private String passw;
    private String telephone;
    private String internalId; // Internal ID
}
