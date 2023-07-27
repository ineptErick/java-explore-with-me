package ru.practicum.request.model;

import lombok.experimental.UtilityClass;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event((request.getEvent().getId()))
                .status(request.getStatus())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .build();
    }

    public static List<ParticipationRequestDto> toParticipationRequestDto(List<Request> requests) {
        return requests
                .stream()
                .map(request -> toParticipationRequestDto(request))
                .collect(Collectors.toList());
    }

}