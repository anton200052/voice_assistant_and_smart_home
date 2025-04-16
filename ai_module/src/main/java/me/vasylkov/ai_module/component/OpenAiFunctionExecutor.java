package me.vasylkov.ai_module.component;

import com.openai.core.JsonValue;

import java.util.Map;

public interface OpenAiFunctionExecutor {
    String execute(Map<String, JsonValue> arguments);
    String getExecutedFunctionName();
}
