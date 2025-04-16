package me.vasylkov.smart_home_module.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MqttServiceImp implements MqttService {
    private final Logger logger;
    private final String mqttName = "zigbee2mqtt";
    private MqttClient mqttClient;
    @Value("${broker.address}")
    private String brokerAddress;

    @Override
    public void subscribe(String topic, IMqttMessageListener listener) throws MqttException {
        String topicFilter = topic.startsWith(mqttName) ? topic : mqttName + topic;
        mqttClient.subscribe(topicFilter, listener);
    }

    @Override
    public String setExposeValue(String friendlyName, String payload) throws MqttException {
        String topic = mqttName + "/" + friendlyName + "/set";
        mqttClient.publish(topic, new MqttMessage(payload.getBytes()));
        return "Value has been successfully set";
    }

    @Override
    public String updateExposeValue(String friendlyName, String payload) throws MqttException {
        String topic = mqttName + "/" + friendlyName + "/get";
        mqttClient.publish(topic, new MqttMessage(payload.getBytes()));
        return "Request has been successfully sent";
    }

    @PostConstruct
    public void initClient() throws MqttException {
        try {
            mqttClient = new MqttClient(brokerAddress, MqttClient.generateClientId(), null);
            mqttClient.connect();
        }
        catch (MqttException e) {
            logger.error("Критическая ошибка (ошибка инициализации клиента). Приложение НЕ сможет функционировать нормально");
            throw new MqttException(e);
        }
    }

    @PreDestroy
    public void closeClient() throws MqttException {
        try {
            mqttClient.disconnect();
            mqttClient.close();
        }
        catch (MqttException e) {
            logger.error("Ошибка закрытия клиента!");
            throw new MqttException(e);
        }
    }
}
