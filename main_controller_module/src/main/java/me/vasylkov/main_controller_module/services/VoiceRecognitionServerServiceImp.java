package me.vasylkov.main_controller_module.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.component.RecognitionWebClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoiceRecognitionServerServiceImp implements VoiceRecognitionServerService {
    private final RecognitionWebClient webSocketClient;

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
        return webSocketClient.isConnected();
    }

    @Override
    public synchronized void send(String text) {
        webSocketClient.send(text);
    }

    @Override
    public synchronized void send(byte[] bytes) {
        webSocketClient.send(bytes);
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
