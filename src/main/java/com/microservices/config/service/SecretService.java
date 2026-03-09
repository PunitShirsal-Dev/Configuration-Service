package com.microservices.config.service;

import com.microservices.config.dto.SecretRequest;
import com.microservices.config.dto.SecretResponse;
import com.microservices.config.entity.Secret;
import com.microservices.config.exception.ConfigurationNotFoundException;
import com.microservices.config.repository.SecretRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecretService {

    private final SecretRepository secretRepository;

    @Transactional
    public SecretResponse create(SecretRequest request) {
        log.info("Creating secret: secretKey={}, environment={}", request.getSecretKey(), request.getEnvironment());

        Secret secret = new Secret();
        secret.setSecretKey(request.getSecretKey());
        secret.setSecretValue(request.getSecretValue());
        secret.setEnvironment(request.getEnvironment());
        secret.setDescription(request.getDescription());
        secret.setSecretType(request.getSecretType());
        secret.setCreatedAt(LocalDateTime.now());
        secret.setRotatedAt(LocalDateTime.now());
        secret.setExpiresAt(LocalDateTime.now().plus(90, ChronoUnit.DAYS));
        secret.setCreatedBy("system");

        Secret saved = secretRepository.save(secret);
        return mapToResponse(saved);
    }

    @Transactional
    public SecretResponse rotate(String secretKey, String environment, String newSecretValue) {
        log.info("Rotating secret: secretKey={}, environment={}", secretKey, environment);

        Secret secret = secretRepository.findBySecretKeyAndEnvironment(secretKey, environment)
                .orElseThrow(() -> new ConfigurationNotFoundException("Secret not found"));

        secret.setSecretValue(newSecretValue);
        secret.setRotatedAt(LocalDateTime.now());
        secret.setExpiresAt(LocalDateTime.now().plus(90, ChronoUnit.DAYS));

        Secret updated = secretRepository.save(secret);
        return mapToResponse(updated);
    }

    public SecretResponse get(String secretKey, String environment) {
        Secret secret = secretRepository.findBySecretKeyAndEnvironment(secretKey, environment)
                .orElseThrow(() -> new ConfigurationNotFoundException("Secret not found"));
        return mapToResponse(secret);
    }

    public List<SecretResponse> getByEnvironment(String environment) {
        return secretRepository.findByEnvironment(environment).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<SecretResponse> getBySecretType(String secretType) {
        return secretRepository.findBySecretType(secretType).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(String secretKey, String environment) {
        Secret secret = secretRepository.findBySecretKeyAndEnvironment(secretKey, environment)
                .orElseThrow(() -> new ConfigurationNotFoundException("Secret not found"));
        secretRepository.delete(secret);
        log.info("Secret deleted: secretKey={}, environment={}", secretKey, environment);
    }

    private SecretResponse mapToResponse(Secret secret) {
        return new SecretResponse(
                secret.getId(),
                secret.getSecretKey(),
                secret.getSecretValue(),
                secret.getEnvironment(),
                secret.getDescription(),
                secret.getCreatedAt(),
                secret.getRotatedAt(),
                secret.getExpiresAt(),
                secret.getSecretType(),
                secret.getCreatedBy()
        );
    }
}

