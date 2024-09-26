package me.vasylkov.ai_module.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthRestController
{
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> getHealth()
    {
        Map<String, String> response = new HashMap<>();
        response.put("state", "UP");
        return ResponseEntity.ok(response);
    }
}
