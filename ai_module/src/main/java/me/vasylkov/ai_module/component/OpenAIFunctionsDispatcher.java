package me.vasylkov.ai_module.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.core.JsonObject;
import com.openai.core.JsonValue;
import com.openai.models.chat.completions.ChatCompletionMessageToolCall;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OpenAIFunctionsDispatcher {
    private final Map<String, OpenAiFunctionExecutor> functionExecutors = new HashMap<>();
    private final ObjectMapper mapper;

    public OpenAIFunctionsDispatcher(List<OpenAiFunctionExecutor> functionExecutorsList, ObjectMapper mapper) {
        this.mapper = mapper;
        for (OpenAiFunctionExecutor functionExecutor : functionExecutorsList) {
            functionExecutors.put(functionExecutor.getExecutedFunctionName(), functionExecutor);
        }
    }

    public String executeFunction(ChatCompletionMessageToolCall.Function function) {
        String funcName = function.name();
        OpenAiFunctionExecutor functionExecutor = functionExecutors.get(funcName);

        if (functionExecutor != null) {
            Map<String, JsonValue> arguments;
            try {
                JsonValue jsonValue = JsonValue.from(mapper.readTree(function.arguments()));
                arguments = ((JsonObject) jsonValue).values();
            } catch (JsonProcessingException e) {
                return "Bad functions arguments: " + function.arguments();
            }

            return functionExecutor.execute(arguments);
        }

        return "Unknown function: " + funcName;
    }
}
