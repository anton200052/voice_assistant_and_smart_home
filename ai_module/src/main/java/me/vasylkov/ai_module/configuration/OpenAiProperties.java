package me.vasylkov.ai_module.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "openai")
public class OpenAiProperties {
    private String model;
    private String apiKey;
    private Double temperature;
    private Integer maxTokens;
    private Integer contextLimit;
    private String instructions;
}
