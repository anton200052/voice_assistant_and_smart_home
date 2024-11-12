package me.vasylkov.ai_module.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.configuration.OpenAiProperties;
import me.vasylkov.ai_module.entity.Client;
import me.vasylkov.ai_module.entity.Message;
import me.vasylkov.ai_module.enums.MessageType;
import me.vasylkov.ai_module.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImp implements ClientService {
    private final OpenAiProperties openAiProperties;
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    @Transactional
    public Optional<Client> findByUUID(String uuid) {
        return clientRepository.findClientByUuid(uuid);
    }

    @Override
    @Transactional
    public void deleteByUUID(String uuid) {
        clientRepository.deleteClientByUuid(uuid);
    }

    @Override
    @Transactional
    public void registerClient(String uuid, String smartHomeUrl) {
        if (clientRepository.findClientByUuid(uuid).isEmpty()) {
            Client newClient = new Client();
            newClient.setUuid(uuid);
            newClient.setSmartHomeUrl(smartHomeUrl);
            newClient.setMessages(List.of(new Message(MessageType.SYSTEM_MSG, openAiProperties.getInstructions(), newClient), new Message(MessageType.SYSTEM_MSG, "Client UniqueId: " + uuid, newClient), new Message(MessageType.SYSTEM_MSG, "SmartHomeUrl: " + smartHomeUrl, newClient)));
            clientRepository.save(newClient);
        }
    }

    @Override
    @Transactional
    public void deleteAllClientMessagesExcludeInstructionsByClientUUID(String uuid) {
        clientRepository.findClientByUuid(uuid).ifPresent(client -> {
            List<Message> messagesToKeep = client.getMessages().stream().limit(3).toList();

            client.getMessages().clear();
            client.getMessages().addAll(messagesToKeep);

            clientRepository.save(client);
        });
    }


    @Override
    @Transactional
    public void deleteFirstFiveClientMessagesExcludeInstructionsByClientUUID(String uuid) {
        clientRepository.findClientByUuid(uuid).ifPresent(client -> {
            List<Message> messagesToKeep = new ArrayList<>();
            messagesToKeep.addAll(client.getMessages().stream().limit(3).toList());
            messagesToKeep.addAll(client.getMessages().stream().skip(8).toList());

            client.getMessages().clear();
            client.getMessages().addAll(messagesToKeep);

            clientRepository.save(client);
        });
    }

    @Override
    @Transactional
    public void addMessageToClient(String uuid, MessageType messageType, String messageText) {
        Client client = clientRepository.findClientByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Client with uniqueId " + uuid + " not found"));

        client.getMessages().add(new Message(messageType, messageText, client));
        clientRepository.save(client);
    }
}
