package com.espou.turnero.model.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
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
//    @JsonCreator
//    public static Profile fromValue(String value) {
//        for (Profile profile : Profile.values()) {
//            if (profile.value.equalsIgnoreCase(value)) {
//                return profile;
//            }
//        }
//        throw new IllegalArgumentException("Invalid Profile value: " + value);
//    }
}
