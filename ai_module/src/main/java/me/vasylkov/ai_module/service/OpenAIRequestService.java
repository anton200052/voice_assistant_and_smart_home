package me.vasylkov.ai_module.service;

import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.*;
import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.component.OpenAIChatCompletionFunctionToolsManager;
import me.vasylkov.ai_module.component.OpenAIFunctionsDispatcher;
import me.vasylkov.ai_module.component.OpenAIManager;
import me.vasylkov.ai_module.configuration.OpenAiProperties;
import me.vasylkov.ai_module.entity.Client;
import me.vasylkov.ai_module.enums.MessageType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAIRequestService implements AIRequestService {
    private final OpenAIManager openAIManager;
    private final ClientService clientService;
    private final OpenAIChatCompletionFunctionToolsManager openAIChatCompletionFunctionToolsManager;
    private final OpenAIClient openAIClient;
    private final OpenAiProperties openAiProperties;
    private final OpenAIFunctionsDispatcher openAIFunctionsDispatcher;

    @Override
    public String requestToAI(String request, Client client) {
        String clientUUID = client.getUuid();
        clientService.addMessageToClient(clientUUID, MessageType.USER_MSG, request);

        List<ChatCompletionMessageParam> chatMessages = openAIManager.convertEntityMessagesToChatCompletionMessages(client.getMessages());

        String strResponse = getResponse(chatMessages);
        clientService.addMessageToClient(clientUUID, MessageType.ASSISTANT_MSG, strResponse);

        if (client.getMessages().size() >= openAiProperties.getContextLimit()) {
            clientService.deleteFirstFiveClientMessagesExcludeInstructionsByClientUUID(client.getUuid());
        }

        return strResponse;
    }

    public String getResponse(List<ChatCompletionMessageParam> chatMessages) {
        List<ChatCompletionMessageToolCall> toolCalls;
        String strResponse;

        do {
            ChatCompletionCreateParams chatCompletionCreateParams = openAIManager.buildChatCompletionCreateParams(chatMessages);
            ChatCompletionMessage chatCompletionMessage = openAIClient.chat().completions().create(chatCompletionCreateParams).choices().get(0).message();
            ChatCompletionMessageParam chatCompletionMessageParam = ChatCompletionMessageParam.ofAssistant(chatCompletionMessage.toParam());

            chatMessages.add(chatCompletionMessageParam);

            toolCalls = chatCompletionMessage.toolCalls().orElse(null);
            if (toolCalls != null) {
                for (ChatCompletionMessageToolCall toolCall : toolCalls) {
                    ChatCompletionMessageToolCall.Function function = toolCall.function();
                    String result = openAIFunctionsDispatcher.executeFunction(function);

                    ChatCompletionMessageParam toolCompletionMessageParam = ChatCompletionMessageParam.ofTool(ChatCompletionToolMessageParam.builder().toolCallId(toolCall.id()).content(result).build());

                    chatMessages.add(toolCompletionMessageParam);
                }
            }
            strResponse = chatCompletionMessage.content().orElse("Error to get response.");
        } while (toolCalls != null && !toolCalls.isEmpty());

        return strResponse;
    }
}
