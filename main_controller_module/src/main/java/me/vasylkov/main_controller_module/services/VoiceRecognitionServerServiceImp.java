package me.vasylkov.main_controller_module.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.java_websocket.client.WebSocketClient;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service
@RequiredArgsConstructor
public class VoiceRecognitionServerServiceImp implements VoiceRecognitionServerService {
    private final WebSocketClient webSocketClient;

    @Override
    public synchronized void connect() {
        webSocketClient.connect();
    }

    @Override
    public synchronized void disconnect() {
        webSocketClient.close();
    }

    @Override
    public synchronized boolean isConnected() {
        return webSocketClient.isOpen();
    }

    @Override
    public synchronized void send(String text) {
        webSocketClient.send(text);
    }

    @Override
    public synchronized void send(byte[] bytes) {
        webSocketClient.send(bytes);
    }

    @Override
    public synchronized void send(ByteBuffer buffer) {
        webSocketClient.send(buffer);
    }

    @PostConstruct
    private void init() {
        connect();
    }

    @PreDestroy
    private void destroy() {
        if (isConnected()) {
            disconnect();
        }
    }
}
