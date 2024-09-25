package me.vasylkov.main_controller_module.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "moduleproperties")
public class ModulesProperties
{
    private String recognitionModuleAddress;
    private String aiModuleAddress;
    private String smartHomeModuleAddress;
}
