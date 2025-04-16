package me.vasylkov.ai_module.component;

import com.openai.core.JsonValue;
import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.service.SmartHomeService;
import me.vasylkov.ai_module.spring_context.SpringContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GetSmartHomeDevicesFunctionExecutor implements OpenAiFunctionExecutor{
    private final SmartHomeService smartHomeService;

    @Override
    public String execute(Map<String, JsonValue> arguments) {
        String smartHomeUrl = arguments.get("smartHomeUrl").asStringOrThrow();
        return smartHomeService.getDevices(smartHomeUrl);
    }

    @Override
    public String getExecutedFunctionName() {
        return "get_smart_home_devices";
    }
}
