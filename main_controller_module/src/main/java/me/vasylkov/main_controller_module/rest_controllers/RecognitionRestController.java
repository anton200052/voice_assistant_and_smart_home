package me.vasylkov.main_controller_module.rest_controllers;

import me.vasylkov.main_controller_module.entities.RecognitionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recognition")
public class RecognitionRestController
{
    @PostMapping("recognized-text")
    @ResponseStatus(HttpStatus.OK)
    public void recognizeText(@RequestBody RecognitionRequest request)
    {
        System.out.println(request.getText());
    }
}
