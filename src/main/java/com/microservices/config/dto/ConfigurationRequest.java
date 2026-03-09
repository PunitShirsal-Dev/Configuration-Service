package com.microservices.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationRequest {
    private String key;
    private String value;
    private String environment;
    private String description;
    private Boolean encrypted;
}

