package ru.practicum.event.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {

    @NotNull
    private Long id;

    private String annotation;

    private String title;

    private String description;

    private LocalDateTime eventDate;

    private Long category;

    private Boolean paid;

    private Integer participantLimit;

}
