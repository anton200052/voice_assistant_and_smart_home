package me.vasylkov.main_controller_module.services;

import me.vasylkov.main_controller_module.dto.AIProperty;

import java.util.List;

public interface RequestToAIService
{
     String requestToAIModel(String request);

     List<AIProperty> getAIProperties();

     AIProperty saveProperty(AIProperty property);

     AIProperty getPropertyByKey(String key);
}
