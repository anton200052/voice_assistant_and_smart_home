package me.vasylkov.main_controller_module.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class HttpClientServiceImp implements HttpClientService
{
    private final RestTemplate restTemplate;
    private final Logger logger;

    @Override
    public <R> ResponseEntity<R> sendGetRequest(String url, Class<R> responseType)
    {
        try
        {
            ResponseEntity<R> response = restTemplate.getForEntity(url, responseType);

            if (response.getStatusCode().is2xxSuccessful())
            {
                return response;
            }
            else
            {
                return null;
            }

        }
        catch (HttpStatusCodeException e)
        {
            logger.error("Ошибка HTTP: {}", e.getStatusCode());
            logger.error("Тело ошибки: {}", e.getResponseBodyAsString());
        }
        catch (ResourceAccessException e)
        {
            logger.error("Ошибка доступа к ресурсу: {}", e.getMessage());
        }
        catch (RestClientException e)
        {
            logger.error("Ошибка клиента RestTemplate: {}", e.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Произошла ошибка: {}", e.getMessage());
        }

        return null;
    }

    @Override
    public <T, R> ResponseEntity<R> sendPostRequest(String url, HttpEntity<T> requestEntity, Class<R> responseType)
    {
        try
        {
            ResponseEntity<R> response = restTemplate.postForEntity(url, requestEntity, responseType);

            if (response.getStatusCode().is2xxSuccessful())
            {
                return response;
            }
            else
            {
                return null;
            }

        }
        catch (HttpStatusCodeException e)
        {
            logger.error("Ошибка HTTP: {}", e.getStatusCode());
            logger.error("Тело ошибки: {}", e.getResponseBodyAsString());
        }
        catch (ResourceAccessException e)
        {
            logger.error("Ошибка доступа к ресурсу: {}", e.getMessage());
        }
        catch (RestClientException e)
        {
            logger.error("Ошибка клиента RestTemplate: {}", e.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Произошла ошибка: {}", e.getMessage());
        }

        return null;
    }
}
