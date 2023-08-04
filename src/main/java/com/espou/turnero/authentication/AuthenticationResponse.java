package com.espou.turnero.authentication;

import com.espou.turnero.model.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class AuthenticationResponse {
    private String internalId;
    private String token;
    private Profile profile;
}
