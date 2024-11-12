package me.vasylkov.ai_module.controller;

import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.dto.RegOptions;
import me.vasylkov.ai_module.exception.BadRequestException;
import me.vasylkov.ai_module.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final ClientService clientService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void registerClient(@RequestBody RegOptions options)
    {
        String clientUUID = options.getUuid();
        String smartHomeUrl = options.getSmartHomeUrl();

        if (clientUUID == null || smartHomeUrl == null) {
            throw new BadRequestException("Wrong format.");
        }

        clientService.registerClient(clientUUID, smartHomeUrl);
    }
}
