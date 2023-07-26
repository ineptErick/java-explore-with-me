package ru.practicum.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UsersRepository;
import ru.practicum.users.service.UsersService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
/*    5.1.1.	GET /admin/users – получение информации о пользователях
5.1.2.	POST /admin/users – добавление нового пользователя
5.1.3.	DELETE /admin/users/{userId} – удаление пользователя*/

    @PostMapping
    public UserDto post(@RequestBody User user) {
        return usersService.post(user);
    }

    @GetMapping
    public List<UserDto> getAllUsers(
            @RequestParam Map<String, String> params) {
        return usersService.getAllUsers(params);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        usersService.deleteUser(userId);

    }
}