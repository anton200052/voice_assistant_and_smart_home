package me.vasylkov.main_controller_module.component;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class HttpRequestSender {
    private final RestTemplate restTemplate;
    private final Logger logger;

    public <T, R> ResponseEntity<R> sendRequest(String url, HttpMethod method, HttpEntity<T> requestEntity, Class<R> responseType) {
        try {
            ResponseEntity<R> response = restTemplate.exchange(url, method, requestEntity, responseType);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response;
            }
            return ResponseEntity.status(response.getStatusCode()).build();
        } catch (Exception e) {
            return handleRestClientException(e);
        }
    }

    private <T> ResponseEntity<T> handleRestClientException(Exception e) {
        if (e instanceof HttpClientErrorException exception) {
            return ResponseEntity.status(exception.getStatusCode()).build();
        }
        logger.error("Ошибка доступа к ресурсу:", e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
