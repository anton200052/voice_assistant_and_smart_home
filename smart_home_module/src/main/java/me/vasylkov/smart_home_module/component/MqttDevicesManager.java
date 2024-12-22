package me.vasylkov.smart_home_module.component;

import lombok.Getter;
import lombok.Setter;
import me.vasylkov.smart_home_module.dto.MqttDevice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
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
            MqttDevice.Definition.Expose targetExpose = findInnerExposeRecursively(expose, propertyName);
            if (targetExpose != null) {
                return targetExpose;
            }
        }
        return null;
    }

    private MqttDevice.Definition.Expose findInnerExposeRecursively(MqttDevice.Definition.Expose expose, String propertyName) {
        if (!hasExposeInnerExposes(expose)) { // endpoint
            if (hasExposeTargetProperty(expose, propertyName)) {
                return expose;
            }
            return null;
        }

        List<MqttDevice.Definition.Expose> innerExposes = getInnerExposes(expose);
        return findTargetExposeInInnerExposes(innerExposes, propertyName);
    }

    private boolean hasExposeInnerExposes(MqttDevice.Definition.Expose expose) {
        return expose instanceof MqttDevice.Definition.FeaturesExpose;
    }

    private boolean hasExposeTargetProperty(MqttDevice.Definition.Expose expose, String propertyName) {
        return expose.getProperty().equals(propertyName);
    }

    private List<MqttDevice.Definition.Expose> getInnerExposes(MqttDevice.Definition.Expose expose) {
        if (expose instanceof MqttDevice.Definition.FeaturesExpose featuresExpose) {
            return featuresExpose.getFeatures();
        }
        return Collections.emptyList();
    }

    private MqttDevice.Definition.Expose findTargetExposeInInnerExposes(List<MqttDevice.Definition.Expose> innerExposes, String propertyName) {
        if (innerExposes != null && !innerExposes.isEmpty()) {
            for (MqttDevice.Definition.Expose feature : innerExposes) {
                MqttDevice.Definition.Expose innerExpose = findInnerExposeRecursively(feature, propertyName);
                if (innerExpose != null && innerExpose.getProperty().equals(propertyName)) {
                    return innerExpose;
                }
            }
        }
        return null;
    }
}
