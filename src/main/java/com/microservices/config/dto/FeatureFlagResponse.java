package com.microservices.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureFlagResponse {
    private Long id;
    private String flagName;
    private Boolean enabled;
    private String description;
    private String environment;
    private Integer percentageRollout;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
}

