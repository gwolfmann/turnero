package com.espou.turnero.model.profile;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Profile {
    ADMINISTRADOR("ADMINISTRADOR"),
    GESTOR("GESTOR"),
    INVITADO("INVITADO");

    private final String value;

    Profile(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
