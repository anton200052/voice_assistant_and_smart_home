package me.vasylkov.ai_module.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.vasylkov.ai_module.enums.HealthState;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthResponse
{
    private HealthState state;
}
