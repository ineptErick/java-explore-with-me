package ru.practicum.request.model;

import ru.practicum.request.dto.RequestDto;

public enum RequestMapper {
    INSTANT;

    public RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .event((request.getEvent().getId()))
                .status(request.getStatus())
                .requester(request.getRequester().getId())
                .created(request.getCreated())
                .build();
    }
}
