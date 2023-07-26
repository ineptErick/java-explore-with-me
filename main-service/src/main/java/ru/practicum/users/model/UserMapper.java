package ru.practicum.users.model;

import ru.practicum.users.dto.UserDto;

public enum UserMapper {
    INSTANT;

    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
