package me.vasylkov.smart_home_module.json_deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.vasylkov.smart_home_module.dto.MqttDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExposeDeserializer extends JsonDeserializer<MqttDevice.Definition.Expose> {
    private final Logger logger = LoggerFactory.getLogger(ExposeDeserializer.class);

    @Override
    public MqttDevice.Definition.Expose deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        String type = getNodeTextValue(node, "type");

        return deserializeExpose(mapper, node, type);
    }

    private String getNodeTextValue(JsonNode node, String fieldName) {
        return node.has(fieldName) ? node.get(fieldName).asText() : null;
    }

    private double getNodeDoubleValue(JsonNode node, String fieldName) {
        return node.has(fieldName) ? node.get(fieldName).asDouble() : 0;
    }

    private int getNodeIntValue(JsonNode node, String fieldName) {
        return node.has(fieldName) ? node.get(fieldName).asInt() : 0;
    }

    private JsonNode getChildNode(JsonNode parentNode, String fieldName) {
        return parentNode.has(fieldName) ? parentNode.get(fieldName) : null;
    }

    private MqttDevice.Definition.Expose deserializeExpose(ObjectMapper mapper, JsonNode node, String type) {
        MqttDevice.Definition.Expose typedExpose = null;
        try {
            typedExpose = deserializeTypedExposeFields(mapper, node, type);
        }
        catch (JsonProcessingException e) {
            logger.error("Ошибка получения данных про возможности девайса", e);
        }

        if (typedExpose != null) {
            String description = getNodeTextValue(node, "description");
            String name = getNodeTextValue(node, "name");
            String label = getNodeTextValue(node, "label");
            String property = getNodeTextValue(node, "property");
            int access = getNodeIntValue(node, "access");

            if (name != null) {
                typedExpose.setName(name);
            }

            if (label != null) {
                typedExpose.setLabel(label);
            }

            if (access != 0) {
                typedExpose.setIsPublished((access & 1) != 0);
                typedExpose.setIsSettable((access & 2) != 0);
                typedExpose.setIsGettable((access & 4) != 0);
            }

            if (property != null) {
                typedExpose.setProperty(property);
            }

            if (description != null) {
                typedExpose.setDescription(description);
            }

            typedExpose.setType(type);
        }

        return typedExpose;
    }

    private MqttDevice.Definition.Expose deserializeTypedExposeFields(ObjectMapper mapper, JsonNode node, String type) throws JsonProcessingException {
        MqttDevice.Definition.Expose expose = null;

        if (type != null) {
            switch (type) {
                case "binary" -> {
                    MqttDevice.Definition.BinaryExpose binaryExpose = new MqttDevice.Definition.BinaryExpose();
                    binaryExpose.setValueOn(getNodeTextValue(node, "value_on"));
                    binaryExpose.setValueOff(getNodeTextValue(node, "value_off"));
                    expose = binaryExpose;
                }
                case "numeric" -> {
                    MqttDevice.Definition.NumericExpose numericExpose = new MqttDevice.Definition.NumericExpose();
                    numericExpose.setValueMin(getNodeDoubleValue(node, "value_min"));
                    numericExpose.setValueMax(getNodeDoubleValue(node, "value_max"));
                    numericExpose.setUnit(getNodeTextValue(node, "unit"));

                    JsonNode presetsNode = getChildNode(node, "presets");
                    if (presetsNode != null) {
                        List<MqttDevice.Definition.Preset> presets = new ArrayList<>();

                        for (JsonNode presetNode : presetsNode) {
                            MqttDevice.Definition.Preset preset = new MqttDevice.Definition.Preset();
                            preset.setName(getNodeTextValue(presetNode, "name"));
                            preset.setValue(getNodeDoubleValue(presetNode, "value"));
                            preset.setDescription(getNodeTextValue(presetNode, "description"));

                            presets.add(preset);
                        }
                        numericExpose.setPresets(presets);
                    }
                    expose = numericExpose;
                }
                case "enum" -> {
                    MqttDevice.Definition.EnumExpose enumExpose = new MqttDevice.Definition.EnumExpose();

                    JsonNode valuesNode = getChildNode(node, "values");
                    if (valuesNode != null) {
                        List<String> values = new ArrayList<>();
                        for (JsonNode valueNode : valuesNode) {
                            values.add(valueNode.asText());
                            enumExpose.setValues(values);
                        }
                    }
                    expose = enumExpose;
                }
                case "text" -> expose = new MqttDevice.Definition.TextExpose();
                case "composite" -> {
                    MqttDevice.Definition.CompositeExpose compositeExpose = new MqttDevice.Definition.CompositeExpose();
                    List<MqttDevice.Definition.Expose> features = mapper.treeToValue(getChildNode(node, "features"), new TypeReference<>() {
                    });
                    compositeExpose.setFeatures(features);
                    expose = compositeExpose;
                }
                case "list" -> {
                    MqttDevice.Definition.ListExpose listExpose = new MqttDevice.Definition.ListExpose();
                    MqttDevice.Definition.Expose itemType = mapper.treeToValue(getChildNode(node, "item_type"), MqttDevice.Definition.Expose.class);
                    listExpose.setItemType(itemType);
                    expose = listExpose;
                }
                case "light", "switch", "fan", "cover", "lock", "climate" -> {
                    MqttDevice.Definition.SpecificExpose specificExpose = new MqttDevice.Definition.SpecificExpose();
                    List<MqttDevice.Definition.Expose> features = mapper.treeToValue(getChildNode(node, "features"), new TypeReference<>() {
                    });
                    specificExpose.setFeatures(features);
                    expose = specificExpose;
                }
            }
        }

        return expose;
    }
}

