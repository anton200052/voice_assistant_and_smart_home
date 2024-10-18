package me.vasylkov.ai_module.openai_functional;

import io.github.sashirestela.openai.common.function.Functional;
import me.vasylkov.ai_module.service.AIMessagesService;
import me.vasylkov.ai_module.service.SmartHomeService;
import me.vasylkov.ai_module.spring_context.SpringContext;

// todo: Use AOP instead Spring Context
public class GetSmartHomeDevices implements Functional {
    @Override
    public String execute()
    {
        SmartHomeService smartHomeService = SpringContext.getBean(SmartHomeService.class);
        return smartHomeService.getDevices();
    }
}
