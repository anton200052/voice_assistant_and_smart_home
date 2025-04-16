package me.vasylkov.ai_module.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRequest
{
    @JsonProperty("text")
    private String text;

    @JsonProperty("uuid")
    private String uuid;
}
