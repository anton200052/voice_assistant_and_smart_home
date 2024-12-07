package me.vasylkov.ai_module.controller;

import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.dto.RegOptions;
import me.vasylkov.ai_module.entity.Client;
import me.vasylkov.ai_module.exception.BadRequestException;
import me.vasylkov.ai_module.exception.ClientRegistrationException;
import me.vasylkov.ai_module.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
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

        Client registeredClient = clientService.registerClient(clientUUID, smartHomeUrl);
        if (registeredClient == null) {
            throw new ClientRegistrationException("Unexpected error. Could not register client.");
        }
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteClient(@RequestParam(value = "clientUUID") String clientUUID) {
        clientService.deleteByUUID(clientUUID);
    }
}
