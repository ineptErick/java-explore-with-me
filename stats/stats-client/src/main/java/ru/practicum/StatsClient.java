package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsClient {

        private String serverUrl = "http://stats-server:9090";

//    private String serverUrl = "http://localhost:9090";
    private final String appName = "ewm-service";
    private final ObjectMapper objectMapper;

    private final String start = "1970-01-01 00:00:00";
    private final String end = "2100-01-01 00:00:00";

    private final RestTemplate restTemplate =  new RestTemplateBuilder()
            .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
            .requestFactory(HttpComponentsClientHttpRequestFactory::new)
            .build();

    public void createHit(String uri, String ip) {
        String path = "/hit";
        EndpointHit hitDto = new EndpointHit();
        hitDto.setApp(appName);
        hitDto.setUri(uri);
        hitDto.setIp(ip);
        hitDto.setTimestamp(LocalDateTime.now());
        HttpEntity<Object> requestEntity = new HttpEntity<>(hitDto, defaultHeaders());
        restTemplate.exchange(path, HttpMethod.POST, requestEntity, Object.class);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    public List<ViewStats> getAllStats(Set<String> uris) {
        String path = "/stats?start={start}&end={end}&uris={uris}";

        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "uris", uris
        );

        return sendStatsRequest(path, params);
    }

    private List<ViewStats> sendStatsRequest(String path, Map<String, Object> parameters) {
        ResponseEntity<Object[]> response = restTemplate.getForEntity(path, Object[].class, parameters);
        Object[] objects = response.getBody();
        if (objects != null) {
            return Arrays.stream(objects)
                    .map(object -> objectMapper.convertValue(object, ViewStats.class))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}
