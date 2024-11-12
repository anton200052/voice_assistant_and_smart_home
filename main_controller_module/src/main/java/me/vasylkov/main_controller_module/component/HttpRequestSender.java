package me.vasylkov.main_controller_module.component;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class HttpRequestSender {
    private final RestTemplate restTemplate;
    private final Logger logger;

    public <R> ResponseEntity<R> sendGetForEntityRequest(String url, Class<R> responseType) {
        ResponseEntity<R> response;
        try {
            response = restTemplate.getForEntity(url, responseType);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response;
            }
        }
        catch (Exception e) {
            return handleRestClientException(e);
        }

        return ResponseEntity.status(response.getStatusCode()).build();
    }

    public <T, R> ResponseEntity<R> sendPostForEntityRequest(String url, HttpEntity<T> requestEntity, Class<R> responseType) {
        ResponseEntity<R> response;
        try {
            response = restTemplate.postForEntity(url, requestEntity, responseType);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response;
            }
        }
        catch (RestClientException e) {
            return handleRestClientException(e);
        }

        return ResponseEntity.status(response.getStatusCode()).build();
    }

    public ResponseEntity<Void> sendGetRequest(String url) {
        ResponseEntity<Void> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, null, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response;
            }
        }
        catch (RestClientException e) {
            return handleRestClientException(e);
        }

        return ResponseEntity.status(response.getStatusCode()).build();
    }

    public <T> ResponseEntity<Void> sendPostRequest(String url, HttpEntity<T> requestEntity) {
        ResponseEntity<Void> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response;
            }
        }
        catch (RestClientException e) {
            return handleRestClientException(e);
        }

        return ResponseEntity.status(response.getStatusCode()).build();
    }

    private <T> ResponseEntity<T> handleRestClientException(Exception e) {
        if (e instanceof HttpClientErrorException exception) {
            return ResponseEntity.status(exception.getStatusCode()).build();
        }
        logger.error("Ошибка доступа к ресурсу:", e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
