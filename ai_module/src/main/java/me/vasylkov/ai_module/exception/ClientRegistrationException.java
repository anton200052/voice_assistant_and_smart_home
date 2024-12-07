package me.vasylkov.ai_module.exception;

public class ClientRegistrationException extends RuntimeException {
    public ClientRegistrationException(String message) {
        super(message);
    }
}