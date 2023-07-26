package ru.practicum.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.practicum.users.model.User;

@Component("dbUsersRepository")
public interface UsersRepository extends JpaRepository <User, Long> {
}