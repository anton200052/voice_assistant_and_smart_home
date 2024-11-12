package me.vasylkov.ai_module.service;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import io.github.sashirestela.openai.common.tool.ToolCall;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.component.FunctionExecutorManager;
import me.vasylkov.ai_module.component.OpenAIManager;
import me.vasylkov.ai_module.configuration.OpenAiProperties;
import me.vasylkov.ai_module.entity.Client;
import me.vasylkov.ai_module.enums.MessageType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OpenAIRequestService implements AIRequestService {
    private final OpenAIManager openAIManager;
    private final ClientService clientService;
    private final FunctionExecutorManager functionExecutorManager;
    private final OpenAiProperties openAiProperties;

    @Override
    public String requestToAI(String request, Client client) {
        String clientUUID = client.getUuid();
        clientService.addMessageToClient(clientUUID, MessageType.USER_MSG, request);

        FunctionExecutor functionExecutor = functionExecutorManager.getFunctionExecutor();
        List<ChatMessage> chatMessages = openAIManager.convertEntityMessagesToChatMessages(client.getMessages());
        SimpleOpenAI openAI = openAIManager.buildOpenAI();

        String strResponse = getResponse(chatMessages, openAI, functionExecutor);
        clientService.addMessageToClient(clientUUID, MessageType.ASSISTANT_MSG, strResponse);

        if (client.getMessages().size() >= openAiProperties.getContextLimit()) {
            clientService.deleteFirstFiveClientMessagesExcludeInstructionsByClientUUID(client.getUuid());
        }

        return strResponse;
    }

    public String getResponse(List<ChatMessage> chatMessages, SimpleOpenAI openAI, FunctionExecutor functionExecutor) {
        List<ToolCall> toolCalls;
        String strResponse;

        do {
            ChatRequest chatRequest = openAIManager.buildChatRequest(chatMessages);
            CompletableFuture<Chat> futureChat = openAI.chatCompletions().create(chatRequest);
            Chat chatResponse = futureChat.join();
            ChatMessage.ResponseMessage chatMessage = chatResponse.firstMessage();
            chatMessages.add(chatMessage);

            toolCalls = chatMessage.getToolCalls();
            if (toolCalls != null) {
                for (ToolCall toolCall : toolCalls) {
                    var result = functionExecutor.execute(toolCall.getFunction());
                    chatMessages.add(ChatMessage.ToolMessage.of(result.toString(), toolCall.getId()));
                }
            }
            strResponse = chatResponse.firstContent();
        } while (toolCalls != null && !toolCalls.isEmpty());

        return strResponse;
    }
}
