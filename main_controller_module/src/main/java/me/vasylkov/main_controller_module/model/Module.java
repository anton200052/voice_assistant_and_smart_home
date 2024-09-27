package me.vasylkov.main_controller_module.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.vasylkov.main_controller_module.enums.HealthState;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Module
{
    private String moduleName;
    private HealthState healthState;
    private LocalDateTime lastHealthCheckTime;
    private String rootUrl;

    public Module(String moduleName, HealthState healthState, String rootUrl)
    {
        this.moduleName = moduleName;
        this.healthState = healthState;
        this.rootUrl = rootUrl;
    }
}
