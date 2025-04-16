package me.vasylkov.ai_module.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegOptions {
    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("smartHomeUrl")
    private String smartHomeUrl;
}
