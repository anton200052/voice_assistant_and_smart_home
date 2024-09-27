package me.vasylkov.ai_module.service;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.configuration.OpenAIObjectCreator;
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
    private final OpenAIObjectCreator aiObjectCreator;
    private final PropertyService propertyService;
    private final AIMessagesService aiMessagesService;

    @Override
    public String requestToAI(String request)
    {
        List<Message> entityMessages = aiMessagesService.findAll();
        entityMessages.add(new Message(MessageType.USER_MSG, request));
        List<ChatMessage> chatMessages = convertEntityMessagesToChatMessages(entityMessages);

        SimpleOpenAI openAI = aiObjectCreator.getOpenAIObject();
        ChatRequest chatRequest = ChatRequest.builder()
                .model(propertyService.findByKey("model").getValue())
                .messages(chatMessages)
                .temperature(0.0)
                .maxTokens(1000)
                .build();

        CompletableFuture<Chat> futureChat = openAI.chatCompletions().create(chatRequest);
        Chat chatResponse = futureChat.join();
        String strResponse = chatResponse.firstContent();

        entityMessages.add(new Message(MessageType.ASSISTANT_MSG, strResponse));
        aiMessagesService.saveAll(entityMessages);
        return strResponse;
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
