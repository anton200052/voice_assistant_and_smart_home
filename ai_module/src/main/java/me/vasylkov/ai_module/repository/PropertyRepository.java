package me.vasylkov.ai_module.repository;

import me.vasylkov.ai_module.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>
{
    Property findByKey(String key);
}
