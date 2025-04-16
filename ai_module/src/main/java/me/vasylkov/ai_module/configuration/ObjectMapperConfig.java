package me.vasylkov.ai_module.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.core.ObjectMappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper jsonMapper() {
        return ObjectMappers.jsonMapper();
    }
}
