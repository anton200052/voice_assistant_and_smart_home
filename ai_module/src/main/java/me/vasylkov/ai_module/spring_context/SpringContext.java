package me.vasylkov.ai_module.spring_context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


//todo: Use AOP instead Spring Context
@Component
public class SpringContext implements ApplicationContextAware
{

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext ctx)
    {
        context = ctx;
    }

    public static <T> T getBean(Class<T> beanClass)
    {
        return context.getBean(beanClass);
    }
}
