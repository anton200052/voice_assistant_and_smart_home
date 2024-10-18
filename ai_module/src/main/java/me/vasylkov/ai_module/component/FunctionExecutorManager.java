package me.vasylkov.ai_module.component;

import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import me.vasylkov.ai_module.openai_functional.ClearContext;
import me.vasylkov.ai_module.openai_functional.GetSmartHomeDevices;
import me.vasylkov.ai_module.openai_functional.SetSmartHomeExposeValue;
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
                .description("Delete the context of communication, forget what you talked about, etc...")
                .functionalClass(ClearContext.class)
                .strict(Boolean.TRUE)
                .build();
        functionExecutor.enrollFunction(clearContext);

        FunctionDef getSmartHomeDevices = FunctionDef.builder()
                .name("get_smart_home_devices")
                .description("When you detect a potential smart home command, use this function to retrieve a list of all devices connected to the Zigbee network via the Zigbee2MQTT service. The output will be in JSON format.")
                .functionalClass(GetSmartHomeDevices.class)
                .strict(Boolean.TRUE)
                .build();
        functionExecutor.enrollFunction(getSmartHomeDevices);

        FunctionDef setExposeValue = FunctionDef.builder()
                .name("set_expose_value")
                .description("This function should be used after you receive a potential command to change the state of a smart home device. Before using this function, first call get_smart_home_devices to retrieve a list of all devices. Based on the list of devices and the user's request, you need to identify the correct ieeeaddress of the device and create a Zigbee2MQTT payload to modify the state.\n" + "\n" + "For example: {\"state\": \"ON\", \"brightness\": 254}\n" + "\n")
                .functionalClass(SetSmartHomeExposeValue.class)
                .strict(Boolean.TRUE)
                .build();
        functionExecutor.enrollFunction(setExposeValue);
    }
}
