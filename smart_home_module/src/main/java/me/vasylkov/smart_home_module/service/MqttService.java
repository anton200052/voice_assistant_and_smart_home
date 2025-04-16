package me.vasylkov.smart_home_module.service;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface MqttService
{
    void subscribe(String topicFilter, IMqttMessageListener listener) throws MqttException;
    String setExposeValue(String deviceIEEAddress, String payload) throws MqttException;
    String updateExposeValue(String deviceIEEAddress, String payload) throws MqttException;
}
