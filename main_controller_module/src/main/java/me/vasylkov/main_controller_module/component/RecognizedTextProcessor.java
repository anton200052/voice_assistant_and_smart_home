package me.vasylkov.main_controller_module.component;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.services.AIService;
import me.vasylkov.main_controller_module.services.RequestToTTSService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class RecognizedTextProcessor {
    private final AIService aiService;
    private final RequestToTTSService requestToTTSService;
    private final AudioPlayer audioPlayer;
    private final AudioFilesPathManager audioFilesPathManager;

    @Async
    public void processRecognizedText(String text) {
        if (text.equals("маркус")) {
            audioPlayer.play(audioFilesPathManager.getAudioPathFromResources("long_waiting_sound.mp3"), true);
        }
        else if (text.contains("маркус")) {
            audioPlayer.play(audioFilesPathManager.getAudioPathFromResources("request_confirm_sound.mp3"), true);
            String aiResponse = aiService.requestToAIModel(text);
            Path filePath = requestToTTSService.requestToTTS(aiResponse);
            audioPlayer.play(filePath, true);
        }
    }
}
