package me.vasylkov.ai_module.component;

import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import me.vasylkov.ai_module.openai_functional.ClearContext;
import org.springframework.stereotype.Component;

@Data
@Component
public class FunctionExecutorManager
{
    private FunctionExecutor functionExecutor = new FunctionExecutor();

    @PostConstruct
    public void initFunctions()
    {
        FunctionDef clearContext = FunctionDef.builder()
                .name("clear_context")
                .description("Удалить контекст общения, забыть о чем общались")
                .functionalClass(ClearContext.class)
                .strict(Boolean.TRUE)
                .build();
        functionExecutor.enrollFunction(clearContext);
    }
}
