package me.vasylkov.main_controller_module.services;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.component.ModulesManager;
import me.vasylkov.main_controller_module.dto.HealthCheckResponse;
import me.vasylkov.main_controller_module.model.Module;
import me.vasylkov.main_controller_module.enums.HealthState;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModulesHealthStateUpdaterServiceImp implements ModulesHealthStateUpdaterService
{
    private final ModulesManager modulesManager;
    private final RestTemplate restTemplate;

    @Override
    public void updateModulesHealth()
    {
        List<Module> modules = modulesManager.getModules();
        for (Module module : modules)
        {
            String requestUrl = module.getRootUrl() + "/health";

            try
            {
                ResponseEntity<HealthCheckResponse> healthCheckResponseRequestEntity = restTemplate.getForEntity(requestUrl, HealthCheckResponse.class);
                HealthCheckResponse healthCheckResponse = healthCheckResponseRequestEntity.getBody();

                if (healthCheckResponseRequestEntity.getStatusCode().is2xxSuccessful() && healthCheckResponse != null && healthCheckResponse.getState() == HealthState.UP)
                {
                    module.setHealthState(HealthState.UP);
                }
                else
                {
                    module.setHealthState(HealthState.DOWN);
                }
            }
            catch (ResourceAccessException e)
            {
                module.setHealthState(HealthState.DOWN);
            }
            finally
            {
                module.setLastHealthCheckTime(LocalDateTime.now());
            }
        }
    }
}
