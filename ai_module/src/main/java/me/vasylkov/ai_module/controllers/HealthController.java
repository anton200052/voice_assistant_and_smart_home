package me.vasylkov.ai_module.controllers;

import me.vasylkov.ai_module.dto.HealthResponse;
import me.vasylkov.ai_module.enums.HealthState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class HealthController
{
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> getHealth()
    {
        return ResponseEntity.ok(new HealthResponse(HealthState.UP));
    }
}
