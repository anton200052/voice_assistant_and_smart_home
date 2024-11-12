package me.vasylkov.ai_module.openai_functional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.common.function.Functional;
import me.vasylkov.ai_module.service.ClientService;
import me.vasylkov.ai_module.spring_context.SpringContext;

// todo: Use AOP instead Spring Context
public class ClearContext implements Functional {
    @JsonProperty(required = true)
    @JsonPropertyDescription("Client unique id which you can find in second SYSTEM instructions message in our dialogue")
    public String clientId;

    @Override
    public String execute() {
        ClientService clientService = SpringContext.getBean(ClientService.class);
        clientService.deleteAllClientMessagesExcludeInstructionsByClientUUID(clientId);
        return "Успех";
    }
}
