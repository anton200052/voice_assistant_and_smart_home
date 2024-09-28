package me.vasylkov.main_controller_module.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface HttpClientService
{
    <R> ResponseEntity<R> sendGetRequest(String url, Class<R> responseType);

    <T, R> ResponseEntity<R> sendPostRequest(String url, HttpEntity<T> requestEntity, Class<R> responseType);
}
