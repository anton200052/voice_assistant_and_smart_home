package me.vasylkov.ai_module.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.vasylkov.ai_module.enums.MessageType;

@Data
@Entity
@Table(name = "MESSAGES")
@NoArgsConstructor
public class Message {
    public Message(MessageType messageType, String messageText, Client client) {
        this.messageType = messageType;
        this.messageText = messageText;
        this.client = client;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private MessageType messageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private String messageText;
}
