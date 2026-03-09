package com.microservices.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecretResponse {
    private Long id;
    private String secretKey;
    private String secretValue;
    private String environment;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime rotatedAt;
    private LocalDateTime expiresAt;
    private String secretType;
    private String createdBy;
}

