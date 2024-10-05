package me.vasylkov.ai_module.service;

import me.vasylkov.ai_module.entity.Property;

import java.util.List;

public interface PropertyService
{
    Property findByKey(String key);

    Property save(Property property);

    List<Property> findAll();
}
