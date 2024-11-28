package me.vasylkov.main_controller_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class MainControllerModuleApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MainControllerModuleApplication.class, args); //1
    }
}
