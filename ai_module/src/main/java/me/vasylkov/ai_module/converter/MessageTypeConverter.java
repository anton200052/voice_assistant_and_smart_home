package me.vasylkov.ai_module.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import me.vasylkov.ai_module.enums.MessageType;

@Converter(autoApply = true)
public class MessageTypeConverter implements AttributeConverter<MessageType, String>
{

    @Override
    public String convertToDatabaseColumn(MessageType attribute)
    {
        return attribute != null ? attribute.toString() : null;
    }

    @Override
    public MessageType convertToEntityAttribute(String dbData)
    {
        if (dbData == null)
        {
            return null;
        }
        for (MessageType type : MessageType.values())
        {
            if (type.toString().equals(dbData))
            {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + dbData);
    }
}
