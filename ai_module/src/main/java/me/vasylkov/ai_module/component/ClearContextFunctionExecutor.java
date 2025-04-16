package me.vasylkov.ai_module.component;

import com.openai.core.JsonValue;
import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.service.ClientService;
import me.vasylkov.ai_module.spring_context.SpringContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ClearContextFunctionExecutor implements OpenAiFunctionExecutor {
    private final ClientService clientService;

    @Override
    public String execute(Map<String, JsonValue> arguments) {
        String clientId = arguments.get("clientId").asStringOrThrow();
        clientService.deleteAllClientMessagesExcludeInstructionsByClientUUID(clientId);
        return "Successfully deleted client";
    }

    @Override
    public String getExecutedFunctionName() {
        return "clear_context";
    }
}
