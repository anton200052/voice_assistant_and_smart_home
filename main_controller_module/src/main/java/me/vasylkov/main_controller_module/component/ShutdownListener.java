package me.vasylkov.main_controller_module.component;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.services.AIService;
import me.vasylkov.main_controller_module.services.VoiceRecognitionServerService;
import org.springframework.context.SmartLifecycle;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShutdownListener implements SmartLifecycle {
    private final VoiceRecognitionServerService voiceRecognitionServerService;
    private final AIService aiService;

    private boolean running = false;

    @Override
    public void start() {
        running = true;

        aiService.registerClient();
    }

    @Override
    public void stop() {
        running = false;

        disconnectRecognitionService();
        aiService.deleteClient();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void disconnectRecognitionService() {
        if (voiceRecognitionServerService.isConnected()) {
            voiceRecognitionServerService.disconnect();
        }
    }
}
