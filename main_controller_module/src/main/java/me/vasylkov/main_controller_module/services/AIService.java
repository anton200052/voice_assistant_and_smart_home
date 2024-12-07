package me.vasylkov.main_controller_module.services;

public interface AIService
{
     String requestToAIModel(String request);
     void registerClient();
     void deleteClient();
}
