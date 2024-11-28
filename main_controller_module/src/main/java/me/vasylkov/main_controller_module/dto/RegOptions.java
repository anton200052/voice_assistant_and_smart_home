package me.vasylkov.main_controller_module.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegOptions {
    private String uuid;
    private String smartHomeUrl;
}