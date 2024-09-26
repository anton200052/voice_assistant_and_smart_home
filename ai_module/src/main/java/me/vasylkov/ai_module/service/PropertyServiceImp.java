package me.vasylkov.ai_module.service;

import lombok.RequiredArgsConstructor;
import me.vasylkov.ai_module.entity.Property;
import me.vasylkov.ai_module.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImp implements PropertyService
{
    private final PropertyRepository propertyRepository;

    @Override
    public Property findByKey(String key)
    {
        return propertyRepository.findByKey(key);
    }

    @Override
    public void save(Property property)
    {
        propertyRepository.save(property);
    }

    @Override
    public List<Property> findAll()
    {
        return propertyRepository.findAll();
    }
}
