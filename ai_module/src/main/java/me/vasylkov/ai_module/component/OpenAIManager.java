package me.vasylkov.ai_module.component;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.entity.Message;
import me.vasylkov.ai_module.enums.MessageType;
import me.vasylkov.ai_module.service.PropertyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenAIManager
{
    private final PropertyService propertyService;
    private final FunctionExecutorManager functionExecutorManager;

    @Bean
    public SimpleOpenAI buildOpenAI()
    {
        return SimpleOpenAI.builder().apiKey(propertyService.findByKey("api-key").getValue()).build();
    }

    public ChatRequest buildChatRequest(List<ChatMessage> chatMessages)
    {
        return ChatRequest.builder()
                .model(propertyService.findByKey("model").getValue())
                .messages(chatMessages)
                .temperature(Double.valueOf(propertyService.findByKey("temperature").getValue()))
                .maxTokens(Integer.valueOf(propertyService.findByKey("max-tokens").getValue()))
                .tools(functionExecutorManager.getFunctionExecutor().getToolFunctions())
                .build();
    }

    public List<ChatMessage> convertEntityMessagesToChatMessages(List<Message> entityMessages)
    {
        List<ChatMessage> chatMessages = new ArrayList<>();

        for (Message entityMessage : entityMessages)
        {
            if (entityMessage.getMessageType() == MessageType.SYSTEM_MSG)
            {
                chatMessages.add(ChatMessage.SystemMessage.of(entityMessage.getMessageText()));
            }
            else if (entityMessage.getMessageType() == MessageType.USER_MSG)
            {
                chatMessages.add(ChatMessage.UserMessage.of(entityMessage.getMessageText()));
            }
            else
            {
                chatMessages.add(ChatMessage.AssistantMessage.of(entityMessage.getMessageText()));
            }
        }

        return chatMessages;
    }

}
