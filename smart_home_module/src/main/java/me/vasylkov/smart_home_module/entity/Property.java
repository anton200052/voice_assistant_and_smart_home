package me.vasylkov.smart_home_module.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "property")
public class Property
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "property_key")
    private String key;
    @Column(name = "property_value")
    private String value;

    public Property(String key, String value)
    {
        this.key = key;
        this.value = value;
    }
}
