package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.users.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class EventShortDto {

    private Long id;
    private String annotation;
    private String title;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private CategoryDto category;
    private Integer confirmedRequests;
    private Boolean paid;
    private Long views;
}
