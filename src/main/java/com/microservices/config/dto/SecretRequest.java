package com.microservices.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecretRequest {
    private String secretKey;
    private String secretValue;
    private String environment;
    private String description;
    private String secretType;
}

