package me.vasylkov.smart_home_module.listener;

import lombok.RequiredArgsConstructor;
import me.vasylkov.smart_home_module.component.GettableExposeValuesManager;
import me.vasylkov.smart_home_module.model.GettableExposeValue;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MqttGettableExposeValuesListener implements IMqttMessageListener {

    private final GettableExposeValuesManager exposeStatesManager;

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String ieeeAddress = topic.substring(1, "/device/".indexOf('/', 1));
        exposeStatesManager.addExposeState(new GettableExposeValue(ieeeAddress, LocalDateTime.now(), mqttMessage.toString()));
    }
}
