package com.espou.turnero.model;

import com.espou.turnero.model.profile.Profile;
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
    private Profile profile;
}
