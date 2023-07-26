package ru.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsClient {

    /*    //Для локального запуска
    private String serverUrl = "http://localhost:9090";*/
    private String serverUrl = "http://stats-server:9090";

    private final String appName = "ewm-service";

    private final ObjectMapper objectMapper;

    private final String start = "2000-01-01 00:00:00";

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

    private List<ViewStats> sendStatsRequest(String path) {
        ResponseEntity<Object[]> response = restTemplate.getForEntity(path, Object[].class);
        System.out.println("hi");
        Object[] objects = response.getBody();
        if (objects != null) {
            return Arrays.stream(objects)
                    .map(object -> objectMapper.convertValue(object, ViewStats.class))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<ViewStats> getViewsByUris(Set<String> uri) {
        List<String> uris = new ArrayList<>(uri);
        URIBuilder path = new URIBuilder().setPath("stats")
                .addParameter("start", start)
                .addParameter("end", end);
        for (String url: uris) {
            path.addParameter("uris", url);
        }
        return sendStatsRequest(URLDecoder.decode(path.toString(), StandardCharsets.UTF_8));
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

}