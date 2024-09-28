package me.vasylkov.main_controller_module.controller;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.dto.RecognitionRequest;
import me.vasylkov.main_controller_module.services.RequestToAIService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recognition")
public class RecognitionRestController
{
    private final RequestToAIService requestToAIService;

    @PostMapping("/recognized-text")
    @ResponseStatus(HttpStatus.OK)
    public void recognizedText(@RequestBody RecognitionRequest request)
    {
        String text = request.getText();
        System.out.println(requestToAIService.requestToAI(text));
    }
}
