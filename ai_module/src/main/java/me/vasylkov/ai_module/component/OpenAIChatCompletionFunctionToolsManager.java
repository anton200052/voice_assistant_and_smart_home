package me.vasylkov.ai_module.component;

import com.openai.core.JsonValue;
import com.openai.models.FunctionDefinition;
import com.openai.models.FunctionParameters;
import com.openai.models.chat.completions.ChatCompletionTool;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Component
public class OpenAIChatCompletionFunctionToolsManager
{
    private List<ChatCompletionTool> functionTools;

    @PostConstruct
    public void initFunctionsDefinitions() {
        this.functionTools = new ArrayList<>();

        /*
         * 1) clear_context
         *   Поля: clientId (string)
         */
        ChatCompletionTool clearContextTool = ChatCompletionTool.builder()
                .function(FunctionDefinition.builder()
                                  .name("clear_context")
                                  .description("Delete the context of communication, forget what you talked about, etc...")
                                  .parameters(FunctionParameters.builder()
                                                      .putAdditionalProperty("type", JsonValue.from("object"))
                                                      .putAdditionalProperty("properties", JsonValue.from(Map.of(
                                                              "clientId", Map.of(
                                                                      "type", "string",
                                                                      "description", "Client unique id which you can find in second SYSTEM instructions message in our dialogue"
                                                                                )
                                                                                                                )))
                                                      .putAdditionalProperty("required", JsonValue.from(List.of("clientId")))
                                                      .putAdditionalProperty("additionalProperties", JsonValue.from(false))
                                                      .build()
                                             )
                                  .build()
                         )
                .build();

        /*
         * 2) get_smart_home_devices
         *   Поля: smartHomeUrl (string)
         */
        ChatCompletionTool getSmartHomeDevicesTool = ChatCompletionTool.builder()
                .function(FunctionDefinition.builder()
                                  .name("get_smart_home_devices")
                                  .description("Retrieve a list of all devices connected to the Zigbee network. The output will be in JSON format.")
                                  .parameters(FunctionParameters.builder()
                                                      .putAdditionalProperty("type", JsonValue.from("object"))
                                                      .putAdditionalProperty("properties", JsonValue.from(Map.of(
                                                              "smartHomeUrl", Map.of(
                                                                      "type", "string",
                                                                      "description", "Smart home url which you can find in second SYSTEM instructions message in our dialogue"
                                                                                    )
                                                                                                                )))
                                                      .putAdditionalProperty("required", JsonValue.from(List.of("smartHomeUrl")))
                                                      .putAdditionalProperty("additionalProperties", JsonValue.from(false))
                                                      .build()
                                             )
                                  .build()
                         )
                .build();

        /*
         * 3) set_expose_value
         *   Поля: smartHomeUrl, friendlyName, payload
         */
        ChatCompletionTool setSmartHomeExposeValueTool = ChatCompletionTool.builder()
                .function(FunctionDefinition.builder()
                                  .name("set_expose_value")
                                  .description("Change the state of a smart home device. You must first call get_smart_home_devices, then use the device's 'friendly_name' and a Zigbee2MQTT payload to modify its state.")
                                  .parameters(FunctionParameters.builder()
                                                      .putAdditionalProperty("type", JsonValue.from("object"))
                                                      .putAdditionalProperty("properties", JsonValue.from(Map.of(
                                                              "smartHomeUrl", Map.of(
                                                                      "type", "string",
                                                                      "description", "Smart home url which you can find in second SYSTEM instructions message in our dialogue"
                                                                                    ),
                                                              "friendlyName", Map.of(
                                                                      "type", "string",
                                                                      "description", "Device 'friendly_name' field which you can find in devices json response."
                                                                                    ),
                                                              "payload", Map.of(
                                                                      "type", "string",
                                                                      "description", "The Zigbee2MQTT payload based on device exposes. Example: {\"state\":\"ON\",\"brightness\":254,\"color\":{\"hue\":50,\"saturation\":100}}"
                                                                               )
                                                                                                                )))
                                                      .putAdditionalProperty("required", JsonValue.from(List.of("smartHomeUrl", "friendlyName", "payload")))
                                                      .putAdditionalProperty("additionalProperties", JsonValue.from(false))
                                                      .build()
                                             )
                                  .build()
                         )
                .build();

        /*
         * 4) update_smart_home_expose_value
         *   Поля: smartHomeUrl, friendlyName, payload
         */
        ChatCompletionTool updateSmartHomeExposeValueTool = ChatCompletionTool.builder()
                .function(FunctionDefinition.builder()
                                  .name("update_smart_home_expose_value")
                                  .description("Retrieve or refresh the state of a specific expose if 'current_value' is missing. You must specify the 'friendly_name' and the desired properties in 'payload'.")
                                  .parameters(FunctionParameters.builder()
                                                      .putAdditionalProperty("type", JsonValue.from("object"))
                                                      .putAdditionalProperty("properties", JsonValue.from(Map.of(
                                                              "smartHomeUrl", Map.of(
                                                                      "type", "string",
                                                                      "description", "Smart home url which you can find in second SYSTEM instructions message in our dialogue"
                                                                                    ),
                                                              "friendlyName", Map.of(
                                                                      "type", "string",
                                                                      "description", "Device 'friendly_name' field which you can find in devices json response."
                                                                                    ),
                                                              "payload", Map.of(
                                                                      "type", "string",
                                                                      "description", "To retrieve value(s), send one or more properties of the expose with an empty string. Example: {\"temperature\": \"\", \"humidity\": \"\"}."
                                                                               )
                                                                                                                )))
                                                      .putAdditionalProperty("required", JsonValue.from(List.of("smartHomeUrl", "friendlyName", "payload")))
                                                      .putAdditionalProperty("additionalProperties", JsonValue.from(false))
                                                      .build()
                                             )
                                  .build()
                         )
                .build();

        functionTools.add(clearContextTool);
        functionTools.add(getSmartHomeDevicesTool);
        functionTools.add(setSmartHomeExposeValueTool);
        functionTools.add(updateSmartHomeExposeValueTool);
    }
}
