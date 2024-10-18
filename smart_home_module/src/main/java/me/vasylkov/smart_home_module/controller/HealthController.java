package me.vasylkov.smart_home_module.controller;

import me.vasylkov.smart_home_module.dto.HealthResponse;
import me.vasylkov.smart_home_module.enums.HealthState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController
{
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> getHealth()
    {
        return ResponseEntity.ok(new HealthResponse(HealthState.UP));
    }
}
