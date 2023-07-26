package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.ApiError.BadRequestException;
import ru.practicum.ApiError.ErrorResponse;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;
import ru.practicum.users.model.UserMapper;
import ru.practicum.users.repository.UsersRepository;
import ru.practicum.users.validation.UserValidation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final UserValidation userValidation;

    @Override
    public UserDto post(User user) {
        System.out.println("hi");
        return UserMapper.INSTANT.toUserDto(usersRepository.save(user));
    }

    @Override
    public List<UserDto> getAllUsers(Map<String, String> params) {
        int page = 0;
        int size = 10;
        if (params.containsKey("from") && params.containsKey("size")) {
            size = Integer.parseInt(params.get("size"));
            page = Integer.parseInt(params.get("from")) / size;
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> pages = usersRepository.findAll(pageRequest);
        List<User> requests = pages.getContent();
        List<UserDto> requestsDto = requests.stream()
                .map(request -> UserMapper.INSTANT.toUserDto(request))
                .collect(Collectors.toList());
        return requestsDto;
    }

    @Override
    public void deleteUser(Long userId) {
        userValidation.isPresent(userId);
        System.out.println("hi");
        usersRepository.deleteById(userId);
    }
}