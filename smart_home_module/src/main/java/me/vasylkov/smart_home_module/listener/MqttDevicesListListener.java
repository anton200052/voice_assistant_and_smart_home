package me.vasylkov.smart_home_module.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.vasylkov.smart_home_module.component.MqttDevicesManager;
import me.vasylkov.smart_home_module.dto.MqttDevice;
import me.vasylkov.smart_home_module.service.MqttService;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MqttDevicesListListener implements IMqttMessageListener {
    private final MqttService mqttService;
    private final MqttDevicesManager mqttDevicesManager;
    private final MqttPublishedDevicesListener mqttPublishedDevicesListener;

    private final String mqttName = "zigbee2mqtt";

    @PostConstruct
    public void subscribe() {
        mqttService.subscribe(mqttName + "/bridge/devices", this);
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        List<MqttDevice> newDevices = new ObjectMapper().readValue(mqttMessage.toString(), new TypeReference<>() {});
        updateDevices(newDevices);
    }

    private void updateDevices(List<MqttDevice> newDevices) {
        for (int i = 0; i < newDevices.size(); i++) {
            MqttDevice receivedDevice = newDevices.get(i);
            MqttDevice device = mqttDevicesManager.getDeviceByFriendlyName(receivedDevice.getFriendlyName());
            if (device != null) {
                newDevices.set(i, device);
                continue;
            }
            mqttService.subscribe(mqttName + "/" + receivedDevice.getFriendlyName(), mqttPublishedDevicesListener);
        }
        mqttDevicesManager.setDevices(newDevices);
    }
}
