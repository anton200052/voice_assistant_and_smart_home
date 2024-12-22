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
        JsonNode jsonNode = new ObjectMapper().readTree(mqttMessage.toString());
        String friendlyName = getFriendlyNameFromTopic(topic);
        Map<String, Object> properties = jsonExtractor.extractKeysWithValues(jsonNode);

        updateDeviceProperties(properties, friendlyName);
    }

    private String getFriendlyNameFromTopic(String topic) {
        return topic.substring(topic.lastIndexOf("/") + 1);
    }

    private void updateDeviceProperties(Map<String, Object> properties, String friendlyName) {
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            MqttDevice mqttDevice = mqttDevicesManager.getDeviceByFriendlyName(friendlyName);
            String propertyName = entry.getKey();
            MqttDevice.Definition.Expose expose = mqttDevicesManager.getDeviceExposeByPropertyName(mqttDevice, propertyName);
            String textPropertyValue = (String) entry.getValue();

            if (expose != null) {
                if (expose instanceof MqttDevice.Definition.BinaryExpose binaryExpose) {
                    binaryExpose.setCurrentValue(textPropertyValue);
                }
                else if (expose instanceof MqttDevice.Definition.EnumExpose enumExpose) {
                    enumExpose.setCurrentValue(textPropertyValue);
                }
                else if (expose instanceof MqttDevice.Definition.TextExpose textExpose) {
                    textExpose.setCurrentValue(textPropertyValue);
                }
                else if (expose instanceof MqttDevice.Definition.NumericExpose numericExpose) {
                    try {
                        Double currentValue = Double.valueOf(textPropertyValue);
                        numericExpose.setCurrentValue(currentValue);
                    }
                    catch (NumberFormatException e) {
                        logger.error("Ошибка парсинга значения!", e);
                    }
                }
            }
        }
    }
}
