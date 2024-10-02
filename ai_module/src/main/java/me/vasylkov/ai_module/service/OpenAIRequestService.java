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
import me.vasylkov.ai_module.entity.Message;
import me.vasylkov.ai_module.enums.MessageType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OpenAIRequestService implements AIRequestService
{
    private final OpenAIManager openAIManager;
    private final AIMessagesService aiMessagesService;
    private final FunctionExecutorManager functionExecutorManager;
    private final PropertyService propertyService;

    @Override
    public String requestToAI(String request)
    {
        List<Message> entityMessages = aiMessagesService.findAll();
        entityMessages.add(new Message(MessageType.USER_MSG, request));

        FunctionExecutor functionExecutor = functionExecutorManager.getFunctionExecutor();
        List<ChatMessage> chatMessages = openAIManager.convertEntityMessagesToChatMessages(entityMessages);
        SimpleOpenAI openAI = openAIManager.buildOpenAI();

        String strResponse = getResponse(chatMessages, openAI, functionExecutor);
        entityMessages.add(new Message(MessageType.ASSISTANT_MSG, strResponse));

        if (entityMessages.size() >= Integer.parseInt(propertyService.findByKey("context-limit").getValue()))
        {
            entityMessages.subList(1, 7).clear();
            aiMessagesService.deleteFirstFiveExceptInstructions();
        }

        aiMessagesService.saveAll(entityMessages);
        return strResponse;
    }

    public String getResponse(List<ChatMessage> chatMessages, SimpleOpenAI openAI, FunctionExecutor functionExecutor)
    {
        List<ToolCall> toolCalls = null;
        String strResponse = null;

        do
        {
            ChatRequest chatRequest = openAIManager.buildChatRequest(chatMessages);
            CompletableFuture<Chat> futureChat = openAI.chatCompletions().create(chatRequest);
            Chat chatResponse = futureChat.join();
            ChatMessage.ResponseMessage chatMessage = chatResponse.firstMessage();
            chatMessages.add(chatMessage);

            toolCalls = chatMessage.getToolCalls();
            if (toolCalls != null)
            {
                for (ToolCall toolCall : toolCalls)
                {
                    var result = functionExecutor.execute(toolCall.getFunction());
                    chatMessages.add(ChatMessage.ToolMessage.of(result.toString(), toolCall.getId()));
                }
            }
            strResponse = chatResponse.firstContent();
        }
        while (toolCalls != null && !toolCalls.isEmpty());

        return strResponse;
    }
}
