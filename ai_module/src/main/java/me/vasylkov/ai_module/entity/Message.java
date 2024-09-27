package me.vasylkov.ai_module.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.vasylkov.ai_module.converter.MessageTypeConverter;
import me.vasylkov.ai_module.enums.MessageType;

@Data
@Entity
@NoArgsConstructor
@Table(name = "messages")
public class Message
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_type")
    @Convert(converter = MessageTypeConverter.class)
    private MessageType messageType;

    @Column(name = "message_text")
    private String messageText;

    public Message(MessageType messageType, String messageText)
    {
        this.messageType = messageType;
        this.messageText = messageText;
    }
}
