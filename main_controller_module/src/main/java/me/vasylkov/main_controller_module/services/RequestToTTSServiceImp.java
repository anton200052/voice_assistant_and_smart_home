package me.vasylkov.main_controller_module.services;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.component.AudioFilesPathManager;
import me.vasylkov.main_controller_module.component.ModulesManager;
import me.vasylkov.main_controller_module.dto.RecognitionRequest;
import me.vasylkov.main_controller_module.dto.TTSRequest;
import me.vasylkov.main_controller_module.enums.ModuleType;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
@RequiredArgsConstructor
public class RequestToTTSServiceImp implements RequestToTTSService
{
    private final HttpClientService httpClientService;
    private final ModulesManager modulesManager;
    private final AudioPlayerService audioPlayerService;
    private final AudioFilesPathManager audioFilesPathManager;

    @Override
    public Path requestToTTS(String text)
    {
        String url = modulesManager.getModule(ModuleType.TTS).getRootUrl() + "/text-to-speech";

        TTSRequest ttsRequest = new TTSRequest(text);
        HttpEntity<TTSRequest> entity = new HttpEntity<>(ttsRequest);

        ResponseEntity<byte[]> response = httpClientService.sendPostRequest(url, entity, byte[].class);

        if (response == null || response.getBody() == null)
        {
            audioPlayerService.play(audioFilesPathManager.getAudioPathFromResources("tts_module_error_sound.mp3"), true);
            return null;
        }

        try
        {
            Path tempFile = Files.createTempFile("audio_", ".mp3");
            Files.write(tempFile, response.getBody(), StandardOpenOption.WRITE);
            return tempFile;
        }
        catch (IOException e)
        {
            audioPlayerService.play(audioFilesPathManager.getAudioPathFromResources("tts_module_error_sound.mp3"), true);
            return null;
        }
    }
}
