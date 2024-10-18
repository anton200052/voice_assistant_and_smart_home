package me.vasylkov.smart_home_module.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.vasylkov.smart_home_module.component.MqttDevicesManager;
import me.vasylkov.smart_home_module.dto.MqttDevice;
import me.vasylkov.smart_home_module.service.MqttService;
import me.vasylkov.smart_home_module.service.PropertyService;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MqttDevicesListListener implements IMqttMessageListener {
    private final MqttService mqttService;
    private final PropertyService propertyService;
    private final MqttDevicesManager mqttDevicesManager;
    private final MqttPublishedDevicesListener mqttPublishedDevicesListener;
    private final MqttGettableExposeValuesListener mqttGettableExposeValuesListener;

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        List<MqttDevice> devices = new ObjectMapper().readValue(mqttMessage.toString(), new TypeReference<>() {});

        for (int i = 0; i < devices.size(); i++) {
            MqttDevice receivedDevice = devices.get(i);
            MqttDevice device = mqttDevicesManager.getDeviceByIEEEAddress(receivedDevice.getIeeeAddress());
            if (device != null) {
                devices.set(i, device);
                continue;
            }
            String mqttName = propertyService.findByKey("mqtt-client-name").getValue();
            mqttService.subscribe(mqttName + "/" + receivedDevice.getIeeeAddress(), mqttPublishedDevicesListener);
            mqttService.subscribe(mqttName + "/" + receivedDevice.getIeeeAddress() + "/get", mqttGettableExposeValuesListener);
        }
        mqttDevicesManager.setDevices(devices);
    }

    @PostConstruct
    public void subscribe() throws InterruptedException {
        String mqttName = propertyService.findByKey("mqtt-client-name").getValue();
        mqttService.subscribe(mqttName + "/bridge/devices", this);
    }
}
