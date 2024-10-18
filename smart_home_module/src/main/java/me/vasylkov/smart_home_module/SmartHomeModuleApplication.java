package me.vasylkov.smart_home_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SmartHomeModuleApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(SmartHomeModuleApplication.class, args);
    }
}