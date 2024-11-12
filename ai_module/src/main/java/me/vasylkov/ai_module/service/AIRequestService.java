package me.vasylkov.ai_module.service;

import me.vasylkov.ai_module.entity.Client;

public interface AIRequestService
{
    String requestToAI(String request, Client client);
}
