package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.event.dto.location.LocationDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {

    private String annotation;

    private String title;

    private String description;

    private LocalDateTime eventDate;

    private LocationDto location;

    private Long category;

    private Boolean paid;

    private Boolean requestModeration;

    private Integer participantLimit;

//    private String stateAction;

}
