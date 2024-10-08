package me.vasylkov.main_controller_module.component;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.services.ModulesHealthStateUpdaterServiceImp;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HealthCheckScheduler
{
    private final ModulesHealthStateUpdaterServiceImp modulesHealthStateUpdaterServiceImp;

    @Scheduled(initialDelay = 0, fixedRate = 5000)
    public void scheduleHealthCheck()
    {
        modulesHealthStateUpdaterServiceImp.updateModulesHealth();
    }
}
