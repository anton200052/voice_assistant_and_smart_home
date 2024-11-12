package me.vasylkov.ai_module.openai_functional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.common.function.Functional;
import me.vasylkov.ai_module.service.SmartHomeService;
import me.vasylkov.ai_module.spring_context.SpringContext;

// todo: Use AOP instead Spring Context
public class GetSmartHomeDevices implements Functional {
    @JsonProperty(required = true)
    @JsonPropertyDescription("Smart home url which you can find in second SYSTEM instructions message in our dialogue")
    public String smartHomeUrl;

    @Override
    public String execute()
    {
        SmartHomeService smartHomeService = SpringContext.getBean(SmartHomeService.class);
        return smartHomeService.getDevices(smartHomeUrl);
    }
}
