package me.vasylkov.ai_module.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.vasylkov.ai_module.entity.Property;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertiesResponse
{
    private List<Property> properties;
}
