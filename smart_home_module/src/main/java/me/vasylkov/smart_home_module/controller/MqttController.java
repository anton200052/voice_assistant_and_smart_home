package me.vasylkov.smart_home_module.controller;

import lombok.RequiredArgsConstructor;
import me.vasylkov.smart_home_module.component.MqttDevicesManager;
import me.vasylkov.smart_home_module.dto.MqttDevicesResponse;
import me.vasylkov.smart_home_module.service.MqttService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mqtt")
@RequiredArgsConstructor
public class MqttController {
    private final MqttDevicesManager mqttDevicesManager;
    private final MqttService mqttService;

    @GetMapping("/devices")
    public ResponseEntity<MqttDevicesResponse> getDevices() {
       MqttDevicesResponse response = new MqttDevicesResponse(mqttDevicesManager.getDevices());
       return ResponseEntity.ok(response);
    }

    @PostMapping("/expose/set")
    public ResponseEntity<String> setExpose(@RequestParam(value = "ieee_address") String address, @RequestBody String payload) {
        return ResponseEntity.ok(mqttService.setExposeValue(address, payload));
    }

    @PostMapping("/expose/get")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getExpose(@RequestParam(value = "ieee_address") String address, @RequestBody String payload) {
        return ResponseEntity.ok(mqttService.getExposeValue(address, payload));
    }
}
