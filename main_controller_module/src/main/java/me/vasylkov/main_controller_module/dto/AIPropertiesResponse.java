package me.vasylkov.main_controller_module.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AIPropertiesResponse
{
    private List<AIProperty> properties;
}
