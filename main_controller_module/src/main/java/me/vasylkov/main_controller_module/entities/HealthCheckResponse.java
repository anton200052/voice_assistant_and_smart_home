package me.vasylkov.main_controller_module.entities;

import lombok.Data;
import me.vasylkov.main_controller_module.enums.HealthState;

@Data
public class HealthCheckResponse
{
    private HealthState state;
}
