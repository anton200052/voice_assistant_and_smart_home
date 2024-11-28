package me.vasylkov.main_controller_module.services;

public interface VoiceRecognitionServerService {
    void connect();
    void disconnect();
    boolean isConnected();
    void send(String text);
    void send(byte[] bytes);
}
