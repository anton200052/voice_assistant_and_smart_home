package me.vasylkov.ai_module.service;

import me.vasylkov.ai_module.entity.Client;
import me.vasylkov.ai_module.enums.MessageType;

import java.util.Optional;

public interface ClientService {
    Client save(Client client);
    Optional<Client> findByUUID(String uuid);
    void deleteByUUID(String uuid);
    void registerClient(String uuid, String smartHomeUrl);
    void deleteAllClientMessagesExcludeInstructionsByClientUUID(String uuid);
    void deleteFirstFiveClientMessagesExcludeInstructionsByClientUUID(String uuid);
    void addMessageToClient(String uuid, MessageType messageType, String messageText);
}
