package me.vasylkov.main_controller_module.component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Setter
@Component
public class ClientUUIDManager {
    private String clientUUID;

    @PostConstruct
    public void generateClientUUID() {
        clientUUID = UUID.randomUUID().toString();
    }
}
