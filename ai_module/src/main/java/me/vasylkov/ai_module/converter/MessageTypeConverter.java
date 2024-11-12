package me.vasylkov.ai_module.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import me.vasylkov.ai_module.enums.MessageType;

@Converter(autoApply = true)
public class MessageTypeConverter implements AttributeConverter<MessageType, String> {

    @Override
    public String convertToDatabaseColumn(MessageType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public MessageType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (MessageType type : MessageType.values()) {
            if (type.getValue().equals(dbData)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + dbData);
    }
}

