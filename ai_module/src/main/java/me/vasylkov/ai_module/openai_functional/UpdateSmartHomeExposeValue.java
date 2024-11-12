package me.vasylkov.ai_module.openai_functional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.common.function.Functional;
import me.vasylkov.ai_module.service.SmartHomeService;
import me.vasylkov.ai_module.spring_context.SpringContext;

// todo: Use AOP instead Spring Context
public class UpdateSmartHomeExposeValue implements Functional {
    @JsonProperty(required = true)
    @JsonPropertyDescription("Smart home url which you can find in second SYSTEM instructions message in our dialogue")
    public String smartHomeUrl;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Device 'friendly_name' field which you can find in devices json response.")
    public String friendlyName;

    @JsonProperty(required = true)
    @JsonPropertyDescription("To correctly obtain the value of 'expose', you need to send one or more properties of the expose, the value of which you want to retrieve, as the key and an empty string as the value. Example request: {\"temperature\": \"\", \"humidity\": \"\"}.")
    public String payload;

    @Override
    public String execute()
    {
        SmartHomeService smartHomeService = SpringContext.getBean(SmartHomeService.class);
        return smartHomeService.updateExposeValue(smartHomeUrl, friendlyName, payload);
    }
}
