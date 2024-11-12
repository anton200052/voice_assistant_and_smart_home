package me.vasylkov.smart_home_module.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MqttServiceImp implements MqttService {
    private MqttClient mqttClient;
    private final Logger logger;
    private final String mqttName = "zigbee2mqtt";
    @Value("${broker.address}")
    private String brokerAddress;

    @Override
    public void subscribe(String topic, IMqttMessageListener listener) {
        String topicFilter = topic.startsWith(mqttName) ? topic : mqttName + topic;
        try {
            mqttClient.subscribe(topicFilter, listener);
        }
        catch (MqttException e) {
            logger.error("Критическая ошибка (ошибка подписки на топик). Приложение НЕ сможет функционировать нормально", e);

            try {
                if (mqttClient != null) {
                    mqttClient.close();
                    mqttClient = null;
                }
            }
            catch (MqttException ignore) {
            }
        }
    }

    @Override
    public String setExposeValue(String friendlyName, String payload) {
        String topic = mqttName + "/" + friendlyName + "/set";

        try {
            mqttClient.publish(topic, new MqttMessage(payload.getBytes()));
        }
        catch (MqttException e) {
            logger.error("Ошибка изменения состояния устройства!", e);
        }

        return "Value has been successfully set";
    }

    @Override
    public String updateExposeValue(String friendlyName, String payload) {
        String topic = mqttName + "/" + friendlyName + "/get";

        try {
            mqttClient.publish(topic, new MqttMessage(payload.getBytes()));
        }
        catch (MqttException e) {
            logger.error("Ошибка обновления состояния устройства!", e);
        }

        return "Request has been successfully sent";
    }


    @PostConstruct
    public void initClient() {
        try {
            mqttClient = new MqttClient(brokerAddress, MqttClient.generateClientId(), null);
            mqttClient.connect();
        }
        catch (MqttException e) {
            logger.error("Критическая ошибка (ошибка инициализации клиента). Приложение НЕ сможет функционировать нормально", e);

            try {
                if (mqttClient != null) {
                    mqttClient.close();
                    mqttClient = null;
                }
            }
            catch (MqttException ignore) {
            }
        }
    }

    @PreDestroy
    public void closeClient() throws MqttException {
        mqttClient.disconnect();
        mqttClient.close();
    }
}
