package me.vasylkov.ai_module.service;

import me.vasylkov.ai_module.entity.Message;

import java.util.List;
import java.util.Optional;

public interface AIMessagesService
{
    Message findInstructions();

    void save(Message message);

    void saveAll(List<Message> messages);

    void clearAllExceptInstructions();

    void deleteFirstFiveExceptInstructions();

    List<Message> findAll();
}
