package me.vasylkov.smart_home_module.component;

import lombok.Data;
import me.vasylkov.smart_home_module.dto.MqttDevice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class MqttDevicesManager {
    private List<MqttDevice> devices = new ArrayList<>();

    public MqttDevice getDeviceByFriendlyName(String friendlyName) {
        for (MqttDevice device : devices) {
            if (device.getFriendlyName().equals(friendlyName)) {
                return device;
            }
        }
        return null;
    }

    public MqttDevice.Definition.Expose getDeviceExposeByPropertyName(MqttDevice mqttDevice, String propertyName) {
        for (MqttDevice.Definition.Expose expose : mqttDevice.getDefinition().getExposes()) {
            MqttDevice.Definition.Expose targetExpose = findDeviceExposeRecursively(expose, propertyName);
            if (targetExpose != null) {
                return targetExpose;
            }
        }
        return null;
    }

    private MqttDevice.Definition.Expose findDeviceExposeRecursively(MqttDevice.Definition.Expose expose, String propertyName) {
        if (!(expose instanceof MqttDevice.Definition.CompositeExpose || expose instanceof MqttDevice.Definition.SpecificExpose)) {
            if (expose.getProperty().equals(propertyName)) {
                return expose;
            }
            return null;
        }

        List<MqttDevice.Definition.Expose> features;
        if (expose instanceof MqttDevice.Definition.SpecificExpose specificExpose) {
            features = specificExpose.getFeatures();
        }
        else {
            MqttDevice.Definition.CompositeExpose compositeExpose = (MqttDevice.Definition.CompositeExpose) expose;
            features = compositeExpose.getFeatures();
        }

        if (features != null) {
            for (MqttDevice.Definition.Expose feature : features) {
                MqttDevice.Definition.Expose compositeExpose = findDeviceExposeRecursively(feature, propertyName);
                if (compositeExpose != null && compositeExpose.getProperty().equals(propertyName)) {
                    return compositeExpose;
                }
            }
        }
        return null;
    }
}
