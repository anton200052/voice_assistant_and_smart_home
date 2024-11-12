package me.vasylkov.ai_module.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SmartHomeServiceImp implements SmartHomeService {

    private final RestTemplate restTemplate;

    @Override
    public String getDevices(String smartHomeUrl) {
        try {
            return restTemplate.getForObject(smartHomeUrl + "/mqtt/devices", String.class);
        }
        catch (RestClientException e) {
            return null;
        }
    }

    @Override
    public String setExposeValue(String smartHomeUrl, String friendlyName, String payload) {
        try {
            return restTemplate.postForObject(smartHomeUrl + "/mqtt/expose/set?friendly_name=" + friendlyName, payload, String.class);
        }
        catch (RestClientException e) {
            return null;
        }
    }

    @Override
    public String updateExposeValue(String smartHomeUrl, String friendlyName, String payload) {
        try {
            return restTemplate.postForObject(smartHomeUrl + "/mqtt/expose/update?friendly_name=" + friendlyName, payload, String.class);
        }
        catch (RestClientException e) {
            return null;
        }
    }
}
