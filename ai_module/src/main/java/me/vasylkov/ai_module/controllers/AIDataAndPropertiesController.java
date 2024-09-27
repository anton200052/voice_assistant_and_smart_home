package me.vasylkov.ai_module.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.entity.Message;
import me.vasylkov.ai_module.entity.Property;
import me.vasylkov.ai_module.service.AIMessagesService;
import me.vasylkov.ai_module.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class AIDataAndPropertiesController
{
    private final PropertyService propertyService;
    private final AIMessagesService messagesService;

    @GetMapping("/property/list")
    public ResponseEntity<List<Property>> getProperties()
    {
        return ResponseEntity.ok(propertyService.findAll());
    }

    @PostMapping("/property/save")
    @ResponseStatus(HttpStatus.OK)
    public void saveProperty(@RequestBody Property property)
    {
        propertyService.save(property);
    }

    @GetMapping("/message/system")
    public ResponseEntity<Message> getSystemMessage()
    {
        return ResponseEntity.ok(messagesService.findInstructions());
    }

    @PostMapping("/message/clear-all")
    @ResponseStatus(HttpStatus.OK)
    public void clearMessagesList()
    {
        messagesService.clearAllExceptInstructions();
    }
}
