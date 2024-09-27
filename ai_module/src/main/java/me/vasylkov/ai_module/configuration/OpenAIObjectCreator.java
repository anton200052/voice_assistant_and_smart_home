package me.vasylkov.ai_module.configuration;

import io.github.sashirestela.openai.SimpleOpenAI;
import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.service.PropertyService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAIObjectCreator
{
    private final PropertyService propertyService;

    public SimpleOpenAI getOpenAIObject()
    {
        return SimpleOpenAI.builder().apiKey(propertyService.findByKey("api-key").getValue()).build();
    }
}
