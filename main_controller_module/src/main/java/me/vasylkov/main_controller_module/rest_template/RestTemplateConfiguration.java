package me.vasylkov.main_controller_module.rest_template;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration
{
    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplateBuilder().build();
    }
}
