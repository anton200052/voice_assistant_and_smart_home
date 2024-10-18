package me.vasylkov.ai_module.controller;

import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.dto.AIRequest;
import me.vasylkov.ai_module.dto.AIResponse;
import me.vasylkov.ai_module.service.AIRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai-model")
@RequiredArgsConstructor
public class AIRequestController
{
    private final AIRequestService requestService;

    @PostMapping("/request")
    public ResponseEntity<AIResponse> requestAI(@RequestBody AIRequest request)
    {
        AIResponse response = new AIResponse(requestService.requestToAI(request.getRequest()));
        return ResponseEntity.ok(response);
    }
}
