package ru.practicum.users.service;

import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.Map;

public interface UsersService {
    UserDto post(User user);

    List<UserDto> getAllUsers(Map<String, String> params);

    void deleteUser(Long userId);
}