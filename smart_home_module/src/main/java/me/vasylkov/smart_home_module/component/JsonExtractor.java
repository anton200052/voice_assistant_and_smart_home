package me.vasylkov.smart_home_module.component;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class JsonExtractor {
    public Map<String, Object> extractKeysWithValues(JsonNode jsonNode) {
        Map<String, Object> keysMap = new HashMap<>();
        extractKeysToMapRecursively(jsonNode, keysMap);
        return keysMap;
    }

    private void extractKeysToMapRecursively(JsonNode jsonNode, Map<String, Object> keysMap) {
        Iterator<String> fieldNames = jsonNode.fieldNames();

        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode childNode = jsonNode.get(fieldName);

            if (childNode.isObject()) {
                extractKeysToMapRecursively(childNode, keysMap);
            }
            else {
                keysMap.put(fieldName, childNode.asText());
            }
        }
    }
}