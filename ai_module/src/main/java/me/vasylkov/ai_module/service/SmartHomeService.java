package me.vasylkov.ai_module.service;

public interface SmartHomeService {
    String getDevices(String smartHomeUrl);
    String setExposeValue(String smartHomeUrl, String friendlyName, String payload);
    String updateExposeValue(String smartHomeUrl, String friendlyName, String payload);
}
