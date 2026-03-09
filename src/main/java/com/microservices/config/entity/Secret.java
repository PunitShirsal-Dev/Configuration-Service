package com.microservices.config.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "secrets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Secret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String secretKey;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String secretValue;

    @Column(nullable = false)
    private String environment;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime rotatedAt;

    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private String secretType;

    private String createdBy;
}

