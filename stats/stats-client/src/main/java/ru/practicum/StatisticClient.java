package ru.practicum;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.StatisticPostDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.RequestEntity.post;

@Service
public class StatisticClient {
    private final RestTemplate rest =  new RestTemplateBuilder()
            .uriTemplateHandler(new DefaultUriBuilderFactory("${server.url}"))
            .requestFactory(HttpComponentsClientHttpRequestFactory::new)
            .build();

    protected  <T> ResponseEntity<Object> post(String path, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, null, body);
    }

    protected ResponseEntity<Object> get(String path, String start, String end, String[] uris, Boolean isUnique) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("isUnique", isUnique);
        if (uris != null) {
            parameters.put("uris", uris);
        }
        StringBuilder fullPath = new StringBuilder().append(path);
        fullPath.append("?start=").append(start).append("&end=").append(end);
        if (uris != null){
            for (String uri: uris) {
                fullPath.append("&uris=").append(uri);
            }
        }
        fullPath.append("&isUnique=").append(isUnique);
        return makeAndSendRequest(HttpMethod.GET, fullPath.toString(), parameters, null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path,
                                                          @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> exploreWithMeServerResponse;
        try {
            if (parameters != null) {
                exploreWithMeServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                exploreWithMeServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(exploreWithMeServerResponse);
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

}
