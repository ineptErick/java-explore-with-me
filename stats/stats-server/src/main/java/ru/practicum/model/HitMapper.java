package ru.practicum.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EndpointHit;

@UtilityClass
public final class HitMapper {

    public static EndpointHit toEndpointHit(Hit hit) {
        return new EndpointHit(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }

    public static Hit toHit(EndpointHit endpointHit) {
        return new Hit(
                endpointHit.getId(),
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                endpointHit.getTimestamp()
        );
    }

}