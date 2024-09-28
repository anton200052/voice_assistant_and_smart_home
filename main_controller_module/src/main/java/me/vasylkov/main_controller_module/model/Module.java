package me.vasylkov.main_controller_module.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.vasylkov.main_controller_module.enums.HealthState;
import me.vasylkov.main_controller_module.enums.ModuleType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Module
{
    private ModuleType moduleType;
    private HealthState healthState;
    private LocalDateTime lastHealthCheckTime;
    private String rootUrl;

    public Module(ModuleType moduleType, HealthState healthState, String rootUrl)
    {
        this.moduleType = moduleType;
        this.healthState = healthState;
        this.rootUrl = rootUrl;
    }
}
