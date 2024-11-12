package me.vasylkov.main_controller_module.component;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.services.VoiceRecognitionServerService;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShutdownListener implements SmartLifecycle {
    private final VoiceRecognitionServerService voiceRecognitionServerService;

    private boolean running = false;

    @Override
    public void start() {
        running = true;
    }

    @Override
    public void stop() {
        running = false;
        if (voiceRecognitionServerService.isConnected()) {
            voiceRecognitionServerService.disconnect();
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
