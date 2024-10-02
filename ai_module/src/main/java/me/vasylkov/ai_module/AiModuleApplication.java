package me.vasylkov.ai_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class AiModuleApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(AiModuleApplication.class, args);
    }
}
