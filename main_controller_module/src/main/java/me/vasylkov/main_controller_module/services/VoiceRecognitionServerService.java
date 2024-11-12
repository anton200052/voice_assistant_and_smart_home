package me.vasylkov.main_controller_module.services;

import java.nio.ByteBuffer;

public interface VoiceRecognitionServerService {
    void connect();
    void disconnect();
    boolean isConnected();
    void send(String text);
    void send(byte[] bytes);
    void send(ByteBuffer buffer);
}
