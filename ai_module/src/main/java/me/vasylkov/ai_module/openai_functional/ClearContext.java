package me.vasylkov.ai_module.openai_functional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.sashirestela.openai.common.function.Functional;
import me.vasylkov.ai_module.service.AIMessagesService;
import me.vasylkov.ai_module.spring_context.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

// todo: Use AOP instead Spring Context
public class ClearContext implements Functional {
    @Override
    public String execute() {
        AIMessagesService aiMessagesService = SpringContext.getBean(AIMessagesService.class);
        aiMessagesService.clearAllExceptInstructions();
        return "Успех";
    }
}
