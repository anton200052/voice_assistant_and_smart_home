package me.vasylkov.smart_home_module.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.vasylkov.smart_home_module.component.JsonExtractor;
import me.vasylkov.smart_home_module.component.MqttDevicesManager;
import me.vasylkov.smart_home_module.dto.MqttDevice;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MqttPublishedDevicesListener implements IMqttMessageListener {
    private final JsonExtractor jsonExtractor;
    private final MqttDevicesManager mqttDevicesManager;
    private final Logger logger;

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String friendlyName = topic.substring(topic.lastIndexOf("/") + 1);
        JsonNode jsonNode = new ObjectMapper().readTree(mqttMessage.toString());
        Map<String, Object> properties = jsonExtractor.extractKeysWithValues(jsonNode);

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            MqttDevice mqttDevice = mqttDevicesManager.getDeviceByFriendlyName(friendlyName);
            MqttDevice.Definition.Expose expose = mqttDevicesManager.getDeviceExposeByPropertyName(mqttDevice, entry.getKey());
            String textValue = (String) entry.getValue();
            if (expose != null) {
                try {
                    if (expose instanceof MqttDevice.Definition.BinaryExpose binaryExpose) {
                        binaryExpose.setCurrentValue(textValue);
                    }
                    else if (expose instanceof MqttDevice.Definition.NumericExpose numericExpose) {
                        Double currentValue = Double.valueOf(textValue);
                        numericExpose.setCurrentValue(currentValue);
                    }
                    else if (expose instanceof MqttDevice.Definition.EnumExpose enumExpose) {
                        enumExpose.setCurrentValue(textValue);
                    }
                    else if (expose instanceof MqttDevice.Definition.TextExpose textExpose) {
                        textExpose.setCurrentValue(textValue);
                    }
                }
                catch (NumberFormatException e) {
                    logger.error("Ошибка парсинга значения!", e);
                }
            }
        }
    }
}
