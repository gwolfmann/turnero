package com.espou.turnero.storage;

import com.espou.turnero.model.User;

public class UserMapper {
    public static UserDTO toDto(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassw(), user.getTelephone(), user.getInternalId(), user.getProfile());
    }

    public static User toEntity(UserDTO userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail(), userDto.getPassw(), userDto.getTelephone(), userDto.getInternalId(), userDto.getProfile());
    }
}
