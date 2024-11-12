package me.vasylkov.main_controller_module.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MicrophoneListenerInitializer implements ApplicationRunner {
    private final MicrophoneListener microphoneListener;

    @Override
    public void run(ApplicationArguments args) {
        microphoneListener.startRecordingAndSendingMicAudioToServer();
    }
}
