package com.microservices.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureFlagRequest {
    private String flagName;
    private Boolean enabled;
    private String description;
    private String environment;
    private Integer percentageRollout;
}

