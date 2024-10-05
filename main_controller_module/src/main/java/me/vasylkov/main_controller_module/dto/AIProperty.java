package me.vasylkov.main_controller_module.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIProperty
{
    private Long id;
    private String key;
    private String value;
}
