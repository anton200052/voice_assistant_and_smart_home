package me.vasylkov.main_controller_module.controller;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.properties.ModulesProperties;
import me.vasylkov.main_controller_module.services.HttpClientService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/smart_home")
public class SmartHomeController {
    private final HttpClientService httpClientService;
    private final ModulesProperties modulesProperties;

    @GetMapping("/devices")
    public ResponseEntity<String> getDevices() {
        ResponseEntity<String> responseEntity = httpClientService.sendGetRequest(modulesProperties.getSmartHomeModuleAddress() + "/mqtt/devices", String.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseEntity.getBody());
    }

    @PostMapping("/expose/set")
    public ResponseEntity<String> setExpose(@RequestParam(value = "ieee_address") String address, @RequestBody String payload) {
        ResponseEntity<String> responseEntity = httpClientService.sendPostRequest(modulesProperties.getSmartHomeModuleAddress() + "/mqtt/expose/set?ieee_address=" + address, new HttpEntity<>(payload), String.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseEntity.getBody());
    }

    @PostMapping("/expose/get")
    public ResponseEntity<String> getExpose(@RequestParam(value = "ieee_address") String address, @RequestBody String payload) {
        ResponseEntity<String> responseEntity = httpClientService.sendPostRequest(modulesProperties.getSmartHomeModuleAddress() + "/mqtt/expose/get?ieee_address=" + address, new HttpEntity<>(payload), String.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseEntity.getBody());
    }
}
