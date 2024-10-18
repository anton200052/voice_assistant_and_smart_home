package me.vasylkov.smart_home_module.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GettableExposeValue {
    private String deviceIEEAddress;
    private LocalDateTime receivedTime;
    private String payload;
}
