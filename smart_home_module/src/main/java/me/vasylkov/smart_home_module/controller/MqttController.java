package me.vasylkov.smart_home_module.controller;

import lombok.RequiredArgsConstructor;
import me.vasylkov.smart_home_module.component.MqttDevicesManager;
import me.vasylkov.smart_home_module.dto.MqttDevicesResponse;
import me.vasylkov.smart_home_module.service.MqttService;
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
    public ResponseEntity<String> setExpose(@RequestParam(value = "friendly_name") String name, @RequestBody String payload) {
        return ResponseEntity.ok(mqttService.setExposeValue(name, payload));
    }

    @PostMapping("/expose/update")
    public ResponseEntity<String> updateExposeValue(@RequestParam(value = "friendly_name") String name, @RequestBody String payload) {
        return ResponseEntity.ok(mqttService.updateExposeValue(name, payload));
    }
}
