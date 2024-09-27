package me.vasylkov.ai_module.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.entity.Message;
import me.vasylkov.ai_module.repository.MessagesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OpenAIMessagesService implements AIMessagesService
{
    private final MessagesRepository messagesRepository;

    @Override
    @Transactional
    public Message findInstructions()
    {
        return messagesRepository.findFirstByOrderByIdAsc();
    }

    @Override
    @Transactional
    public void save(Message message)
    {
         messagesRepository.save(message);
    }

    @Override
    public void saveAll(List<Message> messages)
    {
        messagesRepository.saveAll(messages);
    }

    @Override
    @Transactional
    public void clearAllExceptInstructions()
    {
        messagesRepository.deleteAllExceptFirst();
    }

    @Override
    @Transactional
    public List<Message> findAll()
    {
        return messagesRepository.findAll();
    }
}
