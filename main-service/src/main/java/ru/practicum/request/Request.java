package ru.practicum.request;

import lombok.Data;
import ru.practicum.event.model.Event;
import ru.practicum.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private LocalDateTime created;

    //private Long event;

    //private Long requester;

    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requester;

    @OneToOne
    @JoinColumn(name = "id")
    private Event event;
}