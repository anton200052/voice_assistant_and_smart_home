package me.vasylkov.main_controller_module.controller;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.dto.RecognitionRequest;
import me.vasylkov.main_controller_module.services.AudioPlayerService;
import me.vasylkov.main_controller_module.services.RequestToAIService;
import me.vasylkov.main_controller_module.services.RequestToTTSService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recognition")
public class RecognitionRestController
{
    private final RequestToAIService requestToAIService;
    private final RequestToTTSService requestToTTSService;
    private final AudioPlayerService audioPlayerService;

    @PostMapping("/recognized-text")
    @ResponseStatus(HttpStatus.OK)
    public void recognizedText(@RequestBody RecognitionRequest request)
    {
        String text = request.getText();
        String aiResponse = requestToAIService.requestToAI(text);
        Path filePath = requestToTTSService.requestToTTS(aiResponse);
        audioPlayerService.play(filePath, true);
    }
}
