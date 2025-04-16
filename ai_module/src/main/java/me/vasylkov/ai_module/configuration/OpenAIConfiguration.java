package me.vasylkov.ai_module.configuration;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenAIConfiguration {
    private final OpenAiProperties openAiProperties;

    @Bean
    public OpenAIClient buildOpenAI()
    {
        return OpenAIOkHttpClient.builder().apiKey(openAiProperties.getApiKey()).build();
    }
}
