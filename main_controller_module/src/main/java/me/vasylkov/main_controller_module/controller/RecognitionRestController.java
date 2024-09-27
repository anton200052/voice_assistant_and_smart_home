package me.vasylkov.main_controller_module.controller;

import me.vasylkov.main_controller_module.dto.RecognitionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recognition")
public class RecognitionRestController
{
    @PostMapping("recognized-text")
    @ResponseStatus(HttpStatus.OK)
    public void recognizedText(@RequestBody RecognitionRequest request)
    {
        System.out.println(request.getText());
    }
}
