package me.vasylkov.main_controller_module.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.component.*;
import me.vasylkov.main_controller_module.dto.AIRequest;
import me.vasylkov.main_controller_module.dto.AIResponse;
import me.vasylkov.main_controller_module.dto.RegOptions;
import me.vasylkov.main_controller_module.properties.ModulesProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIServiceImp implements AIService {
    private final HttpRequestSender httpRequestSender;
    private final ModulesProperties modulesProperties;
    private final AudioPlayer audioPlayer;
    private final AudioFilesPathManager audioFilesPathManager;
    private final ClientUUIDManager clientUUIDManager;

    private String aiUrl;
    private String clientUUID;
    private String smartHomeUrl;


    @Override
    public String requestToAIModel(String request) { //todo refactoring + create audio
        AIRequest aiRequest = new AIRequest(clientUUID, request);
        HttpEntity<AIRequest> requestEntity = new HttpEntity<>(aiRequest);

        ResponseEntity<AIResponse> response = sendAIRequest(requestEntity);
        if (response != null) {
            if (isUnauthorized(response)) {
                audioPlayer.play(audioFilesPathManager.getAudioPathFromResources("ai_module_error_sound.mp3"), true);
                return null;
            }

            AIResponse body = response.getBody();
            if (isSuccessful(response) && body != null) {
                return body.getText();
            }
        }

        audioPlayer.play(audioFilesPathManager.getAudioPathFromResources("ai_module_error_sound.mp3"), true);
        return null;
    }

    @Override
    public void registerClient() {
        RegOptions regOptions = new RegOptions(clientUUID, smartHomeUrl);
        httpRequestSender.sendRequest(aiUrl + "/client/register", HttpMethod.POST, new HttpEntity<>(regOptions), null);
    }

    @Override
    public void deleteClient() {
        httpRequestSender.sendRequest(aiUrl + "/client/delete?clientUUID=" + clientUUID, HttpMethod.DELETE, null, Void.class);
    }

    private ResponseEntity<AIResponse> sendAIRequest(HttpEntity<AIRequest> requestEntity) {
        return httpRequestSender.sendRequest(aiUrl + "/ai-model/request", HttpMethod.POST, requestEntity, AIResponse.class);
    }

    private boolean isUnauthorized(ResponseEntity<?> response) {
        return response.getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    private boolean isSuccessful(ResponseEntity<?> response) {
        return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
    }

    @PostConstruct
    public void initVariables() {
        this.aiUrl = modulesProperties.getAiModuleAddress();
        this.clientUUID = clientUUIDManager.getClientUUID();
        this.smartHomeUrl = modulesProperties.getSmartHomeModuleAddress();
    }
}
