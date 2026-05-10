package org.j4di.visualisation.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

@Service
public class AnalyticsRestClient {

    private static final Logger logger = Logger.getLogger(AnalyticsRestClient.class.getName());

    @Value("${dsa.backend.base-url}")
    private String backendBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> List<T> getList(String endpointPath, ParameterizedTypeReference<List<T>> responseType) {
        String url = backendBaseUrl + normalizePath(endpointPath);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<List<T>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                responseType
        );

        logger.info("Loaded data from: " + url);

        return response.getBody() != null ? response.getBody() : List.of();
    }

    private String normalizePath(String endpointPath) {
        if (endpointPath == null || endpointPath.isBlank()) {
            throw new IllegalArgumentException("Endpoint path cannot be null or blank.");
        }

        return endpointPath.startsWith("/") ? endpointPath : "/" + endpointPath;
    }
}