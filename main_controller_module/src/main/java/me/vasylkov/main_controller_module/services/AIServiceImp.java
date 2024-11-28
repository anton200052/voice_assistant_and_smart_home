package me.vasylkov.main_controller_module.services;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.component.*;
import me.vasylkov.main_controller_module.dto.AIRequest;
import me.vasylkov.main_controller_module.dto.AIResponse;
import me.vasylkov.main_controller_module.dto.RegOptions;
import me.vasylkov.main_controller_module.properties.ModulesProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIServiceImp implements AIService {
    private final HttpRequestSender httpRequestManager;
    private final ModulesProperties modulesProperties;
    private final AudioPlayer audioPlayer;
    private final AudioFilesPathManager audioFilesPathManager;
    private final ClientUUIDManager clientUUIDManager;


    @Override
    public String requestToAIModel(String request) {
        String aiUrl = modulesProperties.getAiModuleAddress();
        String smartHomeUrl = modulesProperties.getSmartHomeModuleAddress();
        String uuid = clientUUIDManager.getClientUUID();

        AIRequest aiRequest = new AIRequest(uuid, request);
        HttpEntity<AIRequest> requestEntity = new HttpEntity<>(aiRequest);

        ResponseEntity<AIResponse> response = sendAIRequest(aiUrl, requestEntity);
        if (response != null) {
            if (isUnauthorized(response)) {
                registerClient(aiUrl, uuid, smartHomeUrl);
                response = sendAIRequest(aiUrl, requestEntity);
            }

            AIResponse body = response.getBody();
            if (isSuccessful(response) && body != null) {
                return body.getText();
            }
        }

        audioPlayer.play(audioFilesPathManager.getAudioPathFromResources("ai_module_error_sound.mp3"), true);
        return null;
    }

    private void registerClient(String aiUrl, String uuid, String smartHomeUrl) {
        String regUrl = aiUrl + "/auth/register";
        RegOptions regOptions = new RegOptions(uuid, smartHomeUrl);
        httpRequestManager.sendPostRequest(regUrl, new HttpEntity<>(regOptions));
    }

    private ResponseEntity<AIResponse> sendAIRequest(String url, HttpEntity<AIRequest> requestEntity) {
        return httpRequestManager.sendPostForEntityRequest(url + "/ai-model/request", requestEntity, AIResponse.class);
    }

    private boolean isUnauthorized(ResponseEntity<?> response) {
        return response.getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    private boolean isSuccessful(ResponseEntity<?> response) {
        return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
    }

}
