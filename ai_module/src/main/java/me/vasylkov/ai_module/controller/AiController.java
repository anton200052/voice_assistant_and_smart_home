package me.vasylkov.ai_module.controller;

import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.dto.AIRequest;
import me.vasylkov.ai_module.dto.AIResponse;
import me.vasylkov.ai_module.entity.Client;
import me.vasylkov.ai_module.exception.BadRequestException;
import me.vasylkov.ai_module.exception.UnauthorizedException;
import me.vasylkov.ai_module.service.AIRequestService;
import me.vasylkov.ai_module.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/ai-model")
@RequiredArgsConstructor
public class AiController {
    private final AIRequestService requestService;
    private final ClientService clientService;

    @PostMapping("/request")
    public ResponseEntity<AIResponse> requestToAI(@RequestBody AIRequest request) {
        String clientId = request.getUuid();
        String requestText = request.getText();

        if (clientId == null || requestText == null) {
            throw new BadRequestException("Wrong format");
        }
        Optional<Client> client = clientService.findByUUID(clientId);

        if (client.isEmpty()) {
            throw new UnauthorizedException("Unauthorized, auth required");
        }
        AIResponse response = new AIResponse(requestService.requestToAI(request.getText(), client.get()));
        return ResponseEntity.ok(response);
    }
}
