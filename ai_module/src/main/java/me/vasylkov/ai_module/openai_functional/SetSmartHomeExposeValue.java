package me.vasylkov.ai_module.openai_functional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.common.function.Functional;
import me.vasylkov.ai_module.service.SmartHomeService;
import me.vasylkov.ai_module.spring_context.SpringContext;

// todo: Use AOP instead Spring Context
public class SetSmartHomeExposeValue implements Functional {

    @JsonProperty(required = true)
    @JsonPropertyDescription("Device ieee address which you can find in devices json response.")
    public String deviceIEEEAddress;

    @JsonProperty(required = true)
    @JsonPropertyDescription("The Zigbee2MQTT payload based on device exposes. For example: {\"state\":\"ON\",\"brightness\":254,\"color\":{\"hue\":50,\"saturation\":100}}\n")
    public String payload;

    @Override
    public String execute()
    {
        SmartHomeService smartHomeService = SpringContext.getBean(SmartHomeService.class);
        return smartHomeService.setExposeValue(deviceIEEEAddress, payload);
    }
}
