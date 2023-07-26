package ru.practicum.users.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.ApiError.BadRequestException;
import ru.practicum.ApiError.ErrorResponse;
import ru.practicum.users.repository.UsersRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component("userValidation")
@Slf4j
public class UserValidation {

    @Autowired
    @Qualifier("dbUsersRepository")
    private UsersRepository usersRepository;

    public void isPresent(Long userId){
        if (usersRepository.findById(userId).isEmpty()) {
            log.error(String.format("Пользователь с ID %s не существует.", userId));
            throw new BadRequestException(HttpStatus.BAD_REQUEST,
                    "The required object was not found.",
                    "User with id=" + userId + " was not found",
                    LocalDateTime.now());
        }
        System.out.println("hi");


    }
}