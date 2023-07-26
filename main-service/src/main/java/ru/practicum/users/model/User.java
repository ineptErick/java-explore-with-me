package ru.practicum.users.model;

import lombok.Data;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.Request;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table (name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "id")
    private List<Event> events;

    @OneToMany
    private List<Request> requests;
}
