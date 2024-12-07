package me.vasylkov.main_controller_module.services;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.component.AudioFilesPathManager;
import me.vasylkov.main_controller_module.component.AudioPlayer;
import me.vasylkov.main_controller_module.component.HttpRequestSender;
import me.vasylkov.main_controller_module.dto.TTSRequest;
import me.vasylkov.main_controller_module.properties.ModulesProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
    private final HttpRequestSender httpRequestSender;
    private final AudioPlayer audioPlayer;
    private final AudioFilesPathManager audioFilesPathManager;
    private final ModulesProperties modulesProperties;

    @Override
    public Path requestToTTS(String text)
    {
        String ttsUrl = modulesProperties.getTextToSpeechModuleAddress() + "/text-to-speech";

        TTSRequest ttsRequest = new TTSRequest(text);
        HttpEntity<TTSRequest> entity = new HttpEntity<>(ttsRequest);
        ResponseEntity<byte[]> response = httpRequestSender.sendRequest(ttsUrl, HttpMethod.POST, entity, byte[].class);

        if (response == null || response.getBody() == null)
        {
            audioPlayer.play(audioFilesPathManager.getAudioPathFromResources("tts_module_error_sound.mp3"), true);
            return null;
        }

        return createTempAudioFile(response);
    }

    private Path createTempAudioFile(ResponseEntity<byte[]> response)
    {
        try
        {
            Path tempFile = Files.createTempFile("audio_", ".mp3");
            Files.write(tempFile, response.getBody(), StandardOpenOption.WRITE);
            return tempFile;
        }
        catch (IOException e)
        {
            audioPlayer.play(audioFilesPathManager.getAudioPathFromResources("tts_module_error_sound.mp3"), true);
            return null;
        }
    }
}
