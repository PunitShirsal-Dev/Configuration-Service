package com.microservices.config.controller;

import com.microservices.config.dto.SecretRequest;
import com.microservices.config.dto.SecretResponse;
import com.microservices.config.service.SecretService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/secrets")
@RequiredArgsConstructor
@Slf4j
public class SecretController {

    private final SecretService secretService;

    @PostMapping
    public ResponseEntity<SecretResponse> create(@RequestBody SecretRequest request) {
        log.info("POST /api/secrets - Create secret: {}", request.getSecretKey());
        return ResponseEntity.status(HttpStatus.CREATED).body(secretService.create(request));
    }

    @PostMapping("/{secretKey}/{environment}/rotate")
    public ResponseEntity<SecretResponse> rotate(
            @PathVariable String secretKey,
            @PathVariable String environment,
            @RequestBody SecretRequest request) {
        log.info("POST /api/secrets/{}/{}/rotate - Rotate secret", secretKey, environment);
        return ResponseEntity.ok(secretService.rotate(secretKey, environment, request.getSecretValue()));
    }

    @GetMapping("/{secretKey}/{environment}")
    public ResponseEntity<SecretResponse> get(
            @PathVariable String secretKey,
            @PathVariable String environment) {
        log.info("GET /api/secrets/{}/{} - Get secret", secretKey, environment);
        return ResponseEntity.ok(secretService.get(secretKey, environment));
    }

    @GetMapping("/environment/{environment}")
    public ResponseEntity<List<SecretResponse>> getByEnvironment(
            @PathVariable String environment) {
        log.info("GET /api/secrets/environment/{} - Get secrets by environment", environment);
        return ResponseEntity.ok(secretService.getByEnvironment(environment));
    }

    @GetMapping("/type/{secretType}")
    public ResponseEntity<List<SecretResponse>> getBySecretType(
            @PathVariable String secretType) {
        log.info("GET /api/secrets/type/{} - Get secrets by type", secretType);
        return ResponseEntity.ok(secretService.getBySecretType(secretType));
    }

    @DeleteMapping("/{secretKey}/{environment}")
    public ResponseEntity<Void> delete(
            @PathVariable String secretKey,
            @PathVariable String environment) {
        log.info("DELETE /api/secrets/{}/{} - Delete secret", secretKey, environment);
        secretService.delete(secretKey, environment);
        return ResponseEntity.noContent().build();
    }
}

