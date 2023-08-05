package com.espou.turnero.authentication;

import com.espou.turnero.storage.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final UserDTO userDTO;

    public CustomUserDetails(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert the user's profile to a GrantedAuthority object
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userDTO.getProfile());
        return Collections.singletonList(authority);
    }
    @Override
    public String getPassword() {
        return userDTO.getPassw();
    }

    @Override
    public String getUsername() {
        return userDTO.getInternalId();
    }

    // Implement other UserDetails methods based on your UserDTO fields

    // Example:
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

