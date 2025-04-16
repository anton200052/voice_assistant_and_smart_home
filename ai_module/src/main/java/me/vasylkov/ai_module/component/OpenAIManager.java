package me.vasylkov.ai_module.component;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.*;
import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.configuration.OpenAiProperties;
import me.vasylkov.ai_module.entity.Message;
import me.vasylkov.ai_module.enums.MessageType;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenAIManager
{
    private final OpenAiProperties openAiProperties;
    private final OpenAIChatCompletionFunctionToolsManager openAIChatCompletionFunctionToolsManager;

    public ChatCompletionCreateParams buildChatCompletionCreateParams(List<ChatCompletionMessageParam> chatMessages)
    {
        return ChatCompletionCreateParams.builder()
                .model(ChatModel.of(openAiProperties.getModel()))
                .messages(chatMessages)
                .temperature(openAiProperties.getTemperature())
                .maxCompletionTokens(openAiProperties.getMaxTokens())
                .tools(openAIChatCompletionFunctionToolsManager.getFunctionTools())
                .build();
    }

    public List<ChatCompletionMessageParam> convertEntityMessagesToChatCompletionMessages(List<Message> entityMessages)
    {
        List<ChatCompletionMessageParam> chatMessages = new ArrayList<>();

        for (Message entityMessage : entityMessages)
        {
            if (entityMessage.getMessageType() == MessageType.SYSTEM_MSG)
            {
                chatMessages.add(ChatCompletionMessageParam.ofSystem(ChatCompletionSystemMessageParam.builder().content(entityMessage.getMessageText()).build()));
            }
            else if (entityMessage.getMessageType() == MessageType.USER_MSG)
            {
                chatMessages.add(ChatCompletionMessageParam.ofUser(ChatCompletionUserMessageParam.builder().content(entityMessage.getMessageText()).build()));
            }
            else
            {
                chatMessages.add(ChatCompletionMessageParam.ofAssistant(ChatCompletionAssistantMessageParam.builder().content(entityMessage.getMessageText()).build()));
            }
        }

        return chatMessages;
    }

}
