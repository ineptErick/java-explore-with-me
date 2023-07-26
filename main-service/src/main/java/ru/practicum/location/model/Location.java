package ru.practicum.location.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private Float lat;

    @NotBlank
    private Float lon;

}
