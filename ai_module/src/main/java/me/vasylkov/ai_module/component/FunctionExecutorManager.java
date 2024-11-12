package me.vasylkov.ai_module.component;

import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import me.vasylkov.ai_module.openai_functional.ClearContext;
import me.vasylkov.ai_module.openai_functional.GetSmartHomeDevices;
import me.vasylkov.ai_module.openai_functional.UpdateSmartHomeExposeValue;
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
                .description("This function should be used after you receive a potential command to change the state of a smart home device. Before using this function, first call get_smart_home_devices to retrieve a list of all devices. Based on the list of devices and the user's request, you need to identify the correct expose by 'friendly_name' field. Next you need to check if the device has 'is_settable' field, if it doesn't you should answer that you can't change the state of this expose. Then after all checks create a Zigbee2MQTT payload to modify the state. You can find examples in function description")
                .functionalClass(SetSmartHomeExposeValue.class)
                .strict(Boolean.TRUE)
                .build();
        functionExecutor.enrollFunction(setExposeValue);

        FunctionDef updateExposeValue = FunctionDef.builder()
                .name("update_expose_value")
                .description("This function should be used after you receive a potential command to get the state of a smart home device. Use this function if a smart home device's 'current_value' is null or absent. First, call 'get_smart_home_devices' to get the list of devices. Identify the correct 'expose' by its 'friendly_name'. If 'is_gettable' is missing, inform the user that the state can't be retrieved. Call this function only if the 'expose' has no 'current_value'. After execution, call 'get_smart_home_devices' again to check if 'current_value' is updated. If it's still missing and the 'expose' has 'is_settable',  your task is to offer the user a solution to the problem by suggesting they try changing the field's value so that it updates in the system, and tell the user which values or range of values are available for the requested expose.")
                .functionalClass(UpdateSmartHomeExposeValue.class)
                .strict(Boolean.TRUE)
                .build();
        functionExecutor.enrollFunction(updateExposeValue);
    }
}
