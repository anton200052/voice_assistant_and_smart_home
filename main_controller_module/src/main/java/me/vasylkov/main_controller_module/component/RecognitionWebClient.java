package me.vasylkov.main_controller_module.component;

import me.vasylkov.main_controller_module.enums.ModuleType;
import me.vasylkov.main_controller_module.properties.ModulesProperties;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;


@Component
public class RecognitionWebClient extends WebSocketClient {
    private final Logger logger;
    private final RecognizedTextProcessor textProcessor;

    public RecognitionWebClient(ModulesProperties modulesProperties, Logger logger, RecognizedTextProcessor textProcessor) throws URISyntaxException {
        super(new URI(modulesProperties.getRecognitionModuleAddress() + "/recognize/audio/chunk"));
        this.logger = logger;
        this.textProcessor = textProcessor;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        logger.info("Connected to WebSocket server");
    }

    @Override
    public void onMessage(String message) {
        textProcessor.processRecognizedText(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.info("Disconnected from WebSocket server: {}", reason);
    }

    @Override
    public void onError(Exception ex) {
        logger.info("WebSocket error: {}", ex.getMessage());
    }
}
