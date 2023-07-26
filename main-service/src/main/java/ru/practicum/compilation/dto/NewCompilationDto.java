package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
public class NewCompilationDto {

    @NotBlank
    private String title;

    private Boolean pinned;

    private List<Long> events;

}