package me.vasylkov.smart_home_module.service;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;

public interface MqttService
{
    void subscribe(String topicFilter, IMqttMessageListener listener);
    String setExposeValue(String deviceIEEAddress, String payload);
    String updateExposeValue(String deviceIEEAddress, String payload);
}
