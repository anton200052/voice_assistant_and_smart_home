package me.vasylkov.smart_home_module.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttDevicesResponse {
    private List<MqttDevice> devices;
}
