package me.vasylkov.ai_module.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import io.github.sashirestela.openai.domain.chat.ChatMessage;

public enum MessageType
{
    USER_MSG("user-msg"), SYSTEM_MSG("system-msg"), ASSISTANT_MSG("assistant-msg");

    private final String value;

    MessageType(String value)
    {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString()
    {
        return value;
    }
}

