package me.vasylkov.main_controller_module.component;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.services.AudioPlayerService;
import me.vasylkov.main_controller_module.services.RequestToAIService;
import me.vasylkov.main_controller_module.services.RequestToTTSService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class RecognizedTextProcessor
{
    private final RequestToAIService requestToAIService;
    private final RequestToTTSService requestToTTSService;
    private final AudioPlayerService audioPlayerService;
    private final AudioFilesPathManager audioFilesPathManager;

    @Async
    public void processRecognizedText(String text)
    {
        if (text.equals("маркус"))
        {
            audioPlayerService.play(audioFilesPathManager.getAudioPathFromResources("long_waiting_sound.mp3"), true);
            return;
        }
        audioPlayerService.play(audioFilesPathManager.getAudioPathFromResources("request_confirm_sound.mp3"), true);
        String aiResponse = requestToAIService.requestToAI(text);
        Path filePath = requestToTTSService.requestToTTS(aiResponse);
        audioPlayerService.play(filePath, true);
    }
}
