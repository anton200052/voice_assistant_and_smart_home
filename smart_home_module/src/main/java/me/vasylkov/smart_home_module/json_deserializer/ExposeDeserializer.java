package me.vasylkov.smart_home_module.json_deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.vasylkov.smart_home_module.dto.MqttDevice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExposeDeserializer extends JsonDeserializer<MqttDevice.Definition.Expose> {

    @Override
    public MqttDevice.Definition.Expose deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);
        String type = node.get("type").asText();
        String description = node.has("description") ? node.get("description").asText() : null;
        ;
        String name = node.has("name") ? node.get("name").asText() : null;
        String label = node.has("label") ? node.get("label").asText() : null;
        String property = node.has("property") ? node.get("property").asText() : null;
        int access = node.has("access") ? node.get("access").asInt() : 0;

        MqttDevice.Definition.Expose finalExpose = null;

        switch (type) {
            case "binary" -> {
                MqttDevice.Definition.BinaryExpose binaryExpose = new MqttDevice.Definition.BinaryExpose();
                if (node.has("value_on")) {
                    binaryExpose.setValueOn(node.get("value_on").asText());
                }

                if (node.has("value_off")) {
                    binaryExpose.setValueOff(node.get("value_off").asText());
                }

                finalExpose = binaryExpose;
            }
            case "numeric" -> {
                MqttDevice.Definition.NumericExpose numericExpose = new MqttDevice.Definition.NumericExpose();

                if (node.has("value_min")) {
                    numericExpose.setValueMin(node.get("value_min").asDouble());
                }

                if (node.has("value_max")) {
                    numericExpose.setValueMax(node.get("value_max").asDouble());
                }

                if (node.has("unit")) {
                    numericExpose.setUnit(node.get("unit").asText());
                }

                if (node.has("presets")) {
                    List<MqttDevice.Definition.Preset> presets = new ArrayList<>();
                    JsonNode presetsNode = node.get("presets");

                    for (JsonNode presetNode : presetsNode) {
                        MqttDevice.Definition.Preset preset = new MqttDevice.Definition.Preset();
                        if (presetNode.has("name")) {
                            preset.setName(presetNode.get("name").asText());
                        }
                        if (presetNode.has("value")) {
                            preset.setValue(presetNode.get("value").asDouble());
                        }
                        if (presetNode.has("description")) {
                            preset.setDescription(presetNode.get("description").asText());
                        }
                        presets.add(preset);
                    }

                    numericExpose.setPresets(presets);
                }

                finalExpose = numericExpose;
            }
            case "enum" -> {
                MqttDevice.Definition.EnumExpose enumExpose = new MqttDevice.Definition.EnumExpose();

                if (node.has("values")) {
                    List<String> values = new ArrayList<>();
                    for (JsonNode valueNode : node.get("values")) {
                        values.add(valueNode.asText());
                    }
                    enumExpose.setValues(values);
                }

                finalExpose = enumExpose;
            }
            case "text" -> finalExpose = new MqttDevice.Definition.TextExpose();
            case "composite" -> {
                MqttDevice.Definition.CompositeExpose compositeExpose = new MqttDevice.Definition.CompositeExpose();
                if (node.has("features")) {
                    List<MqttDevice.Definition.Expose> features = mapper.treeToValue(node.get("features"), new TypeReference<>() {
                    });
                    compositeExpose.setFeatures(features);
                }

                finalExpose = compositeExpose;
            }
            case "list" -> {
                MqttDevice.Definition.ListExpose listExpose = new MqttDevice.Definition.ListExpose();
                if (node.has("item_type")) {
                    MqttDevice.Definition.Expose itemType = mapper.treeToValue(node.get("item_type"), MqttDevice.Definition.Expose.class);
                    listExpose.setItemType(itemType);
                }

                finalExpose = listExpose;
            }
            case "light", "switch", "fan", "cover", "lock", "climate" -> {
                MqttDevice.Definition.SpecificExpose specificExpose = new MqttDevice.Definition.SpecificExpose();
                if (node.has("features")) {
                    List<MqttDevice.Definition.Expose> features = mapper.treeToValue(node.get("features"), new TypeReference<>() {
                    });
                    specificExpose.setFeatures(features);
                }

                finalExpose = specificExpose;
            }
        }

        if (finalExpose != null) {
            if (name != null) {
                finalExpose.setName(name);
            }

            if (label != null) {
                finalExpose.setLabel(label);
            }

            if (access != 0) {
                finalExpose.setIsPublished((access & 1) != 0);
                finalExpose.setIsSettable((access & 2) != 0);
                finalExpose.setIsGettable((access & 4) != 0);
            }

            if (property != null) {
                finalExpose.setProperty(property);
            }

            if (description != null) {
                finalExpose.setDescription(description);
            }

            finalExpose.setType(type);
        }

        return finalExpose;
    }

}

