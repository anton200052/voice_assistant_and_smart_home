package me.vasylkov.main_controller_module.components;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.services.ModulesHealthStateUpdaterService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HealthCheckScheduler
{
    private final ModulesHealthStateUpdaterService modulesHealthStateUpdaterService;

    @Scheduled(initialDelay = 0, fixedRate = 5000)
    public void scheduleHealthCheck()
    {
        modulesHealthStateUpdaterService.updateModulesHealth();
    }
}
