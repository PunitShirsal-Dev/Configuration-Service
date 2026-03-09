package com.microservices.config.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "feature_flags")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String flagName;

    @Column(nullable = false)
    private Boolean enabled;

    private String description;

    @Column(nullable = false)
    private String environment;

    private Integer percentageRollout;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;
}

