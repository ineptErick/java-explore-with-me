package ru.practicum.request.privatePart.dto;

import java.util.List;

//Результат подтверждения/отклонения заявок на участие в событии
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}