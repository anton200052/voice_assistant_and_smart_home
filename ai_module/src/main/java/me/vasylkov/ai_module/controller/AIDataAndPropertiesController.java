package me.vasylkov.ai_module.controller;

import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.dto.PropertiesResponse;
import me.vasylkov.ai_module.entity.Message;
import me.vasylkov.ai_module.entity.Property;
import me.vasylkov.ai_module.service.AIMessagesService;
import me.vasylkov.ai_module.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class AIDataAndPropertiesController
{
    private final PropertyService propertyService;
    private final AIMessagesService messagesService;

    @GetMapping("/property/list")
    public ResponseEntity<PropertiesResponse> getProperties()
    {
        return ResponseEntity.ok(new PropertiesResponse(propertyService.findAll()));
    }

    @GetMapping("/property/single")
    public ResponseEntity<Property> getPropertyByKey(@RequestParam(value = "propertyKey") String key)
    {
        return ResponseEntity.ok(propertyService.findByKey(key));
    }

    @PostMapping("/property/save")
    public ResponseEntity<Property> saveProperty(@RequestBody Property property)
    {
        return ResponseEntity.ok(propertyService.save(property));
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
