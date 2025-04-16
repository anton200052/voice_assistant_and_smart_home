package me.vasylkov.ai_module.component;

import com.openai.core.JsonValue;
import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.service.SmartHomeService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UpdateSmartHomeExposeValueFunctionExecutor implements OpenAiFunctionExecutor{
    private final SmartHomeService smartHomeService;

    @Override
    public String execute(Map<String, JsonValue> arguments) {
        String smartHomeUrl = arguments.get("smartHomeUrl").asStringOrThrow();
        String friendlyName = arguments.get("friendlyName").asStringOrThrow();
        String payload = arguments.get("payload").asStringOrThrow();
        return smartHomeService.updateExposeValue(smartHomeUrl, friendlyName, payload);
    }

    @Override
    public String getExecutedFunctionName() {
        return "update_smart_home_expose_value";
    }
}
