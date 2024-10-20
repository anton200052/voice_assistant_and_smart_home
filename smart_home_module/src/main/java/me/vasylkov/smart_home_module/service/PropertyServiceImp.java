package me.vasylkov.smart_home_module.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.vasylkov.smart_home_module.entity.Property;
import me.vasylkov.smart_home_module.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImp implements PropertyService
{
    private final PropertyRepository propertyRepository;

    @Override
    @Transactional
    public Property findByKey(String key)
    {
        return propertyRepository.findByKey(key);
    }

    @Override
    @Transactional
    public Property save(Property property)
    {
        return propertyRepository.save(property);
    }

    @Override
    @Transactional
    public List<Property> findAll()
    {
        return propertyRepository.findAll();
    }
}
