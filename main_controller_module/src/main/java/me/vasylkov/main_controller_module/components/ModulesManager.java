package me.vasylkov.main_controller_module.components;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.entities.Module;
import me.vasylkov.main_controller_module.enums.HealthState;
import me.vasylkov.main_controller_module.properties.ModulesProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ModulesManager
{
    @Getter
    private List<Module> modules;
    private final ModulesProperties modulesProperties;

    public Module getModuleByName(String name)
    {
        for (Module module : modules)
        {
            if (module.getModuleName().equals(name))
            {
                return module;
            }
        }
        return null;
    }

    @PostConstruct
    public void initList()
    {
        modules = new ArrayList<>();

        modules.add(new Module("recognition_module", HealthState.UNINITIALIZED, modulesProperties.getRecognitionModuleAddress()));
        modules.add(new Module("ai_module", HealthState.UNINITIALIZED, modulesProperties.getAiModuleAddress()));
        modules.add(new Module("smart_home_module", HealthState.UNINITIALIZED, modulesProperties.getSmartHomeModuleAddress()));
    }
}
