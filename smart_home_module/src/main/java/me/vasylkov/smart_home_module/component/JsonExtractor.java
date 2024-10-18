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
        extractKeysRecursively(jsonNode, keysMap);
        return keysMap;
    }

    private void extractKeysRecursively(JsonNode jsonNode, Map<String, Object> keysMap) {
        Iterator<String> fieldNames = jsonNode.fieldNames();

        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode childNode = jsonNode.get(fieldName);

            if (childNode.isObject()) {
                extractKeysRecursively(childNode, keysMap);
            }
            else {
                keysMap.put(fieldName, childNode.asText());
            }
        }
    }
}