package ru.practicum.compilation.model;

import lombok.Data;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "compilation")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToMany
    private List<Event> events;

    private Boolean pinned;

    private String title;
}
