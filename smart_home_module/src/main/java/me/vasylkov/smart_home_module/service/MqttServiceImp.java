package me.vasylkov.smart_home_module.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import me.vasylkov.smart_home_module.component.GettableExposeValuesManager;
import me.vasylkov.smart_home_module.model.GettableExposeValue;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MqttServiceImp implements MqttService {
    private MqttClient mqttClient;

    private final PropertyService propertyService;
    private final Logger logger;
    private final GettableExposeValuesManager gettableExposeValuesManager;

    @Override
    public void subscribe(String topic, IMqttMessageListener listener) {
        String mqttName = propertyService.findByKey("mqtt-client-name").getValue();
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
    public String setExposeValue(String deviceIEEAddress, String payload) {
        String mqttName = propertyService.findByKey("mqtt-client-name").getValue();
        String topic = mqttName + "/" + deviceIEEAddress + "/set";

        try {
            mqttClient.publish(topic, new MqttMessage(payload.getBytes()));
        }
        catch (MqttException e) {
            logger.error("Ошибка изменения состояния устройства!", e);
        }

        return "Value has been successfully set";
    }

    @Override
    public String getExposeValue(String deviceIEEAddress, String payload) {
        String mqttName = propertyService.findByKey("mqtt-client-name").getValue();
        String topic = mqttName + "/" + deviceIEEAddress + "/get";

        LocalDateTime timeBeforePublish = LocalDateTime.now();
        try {
            mqttClient.publish(topic, new MqttMessage(payload.getBytes()));
        }
        catch (MqttException e) {
            logger.error("Ошибка получения состояния устройства!", e);
        }

        try {
            int attemptTimes = 5;
            int timeBetweenAttempts = 1000;
            while (attemptTimes > 0) {
                GettableExposeValue value = gettableExposeValuesManager.getLastStateByIEEAddress(deviceIEEAddress);
                if (value != null && value.getReceivedTime().isAfter(timeBeforePublish)) {
                    return value.getPayload();
                }
                attemptTimes--;
                Thread.sleep(timeBetweenAttempts);
            }
        }
        catch (InterruptedException e) {
            logger.error("Ошибка получения состояния устройства!", e);
        }

        return "Error while attempting to get expose value!";
    }


    @PostConstruct
    public void initClient() {
        String brokerUrl = propertyService.findByKey("broker-address").getValue();

        try {
            mqttClient = new MqttClient("tcp://" + brokerUrl, MqttClient.generateClientId(), null);
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
