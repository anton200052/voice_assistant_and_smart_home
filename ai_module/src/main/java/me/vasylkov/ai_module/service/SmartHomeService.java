package me.vasylkov.ai_module.service;

public interface SmartHomeService {
    String getDevices();
    String setExposeValue(String ieeeAddress, String payload);
    String getExposeValue(String ieeeAddress, String payload);
}
