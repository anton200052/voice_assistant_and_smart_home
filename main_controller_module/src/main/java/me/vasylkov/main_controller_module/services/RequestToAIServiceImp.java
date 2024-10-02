package me.vasylkov.main_controller_module.services;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.component.AudioFilesPathManager;
import me.vasylkov.main_controller_module.component.ModulesManager;
import me.vasylkov.main_controller_module.dto.AIRequest;
import me.vasylkov.main_controller_module.dto.AIResponse;
import me.vasylkov.main_controller_module.enums.HealthState;
import me.vasylkov.main_controller_module.enums.ModuleType;
import me.vasylkov.main_controller_module.properties.ModulesProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RequestToAIServiceImp implements RequestToAIService
{
    private final HttpClientService httpClientService;
    private final ModulesProperties modulesProperties;
    private final ModulesManager modulesManager;
    private final AudioPlayerService audioPlayerService;
    private final AudioFilesPathManager audioFilesPathManager;

    @Override
    public String requestToAI(String request)
    {
        if (modulesManager.getModule(ModuleType.AI).getHealthState() != HealthState.UP)
        {
            audioPlayerService.play(audioFilesPathManager.getAudioPathFromResources("ai_module_error.mp3"), true);
            return null;
        }

        String url = modulesProperties.getAiModuleAddress() + "/ai-model/request";
        AIRequest aiRequest = new AIRequest(request);
        HttpEntity<AIRequest> requestEntity = new HttpEntity<>(aiRequest);

        ResponseEntity<AIResponse> response = httpClientService.sendPostRequest(url, requestEntity, AIResponse.class);
        if (response == null || response.getBody() == null)
        {
            audioPlayerService.play(audioFilesPathManager.getAudioPathFromResources("ai_module_error_sound.mp3"), true);
            return null;
        }

        return response.getBody().getResponse();
    }
}
