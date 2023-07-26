package ru.practicum.event.dto.location;

//Широта и долгота места проведения события

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDto {
    //Широта
    private Float lat;
    //Долгота
    private Float lon;
}
