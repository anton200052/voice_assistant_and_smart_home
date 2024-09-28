package me.vasylkov.main_controller_module.component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.enums.ModuleType;
import me.vasylkov.main_controller_module.model.Module;
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

    public Module getModule(ModuleType type)
    {
        for (Module module : modules)
        {
            if (module.getModuleType() == type)
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

        modules.add(new Module(ModuleType.RECOGNITION, HealthState.UNINITIALIZED, modulesProperties.getRecognitionModuleAddress()));
        modules.add(new Module(ModuleType.AI, HealthState.UNINITIALIZED, modulesProperties.getAiModuleAddress()));
        modules.add(new Module(ModuleType.SMART_HOME, HealthState.UNINITIALIZED, modulesProperties.getSmartHomeModuleAddress()));
    }
}
