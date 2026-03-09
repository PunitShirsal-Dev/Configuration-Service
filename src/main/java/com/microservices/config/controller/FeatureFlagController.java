package com.microservices.config.controller;

import com.microservices.config.dto.FeatureFlagRequest;
import com.microservices.config.dto.FeatureFlagResponse;
import com.microservices.config.service.FeatureFlagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/features")
@RequiredArgsConstructor
@Slf4j
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;

    @PostMapping
    public ResponseEntity<FeatureFlagResponse> create(@RequestBody FeatureFlagRequest request) {
        log.info("POST /api/features - Create feature flag: {}", request.getFlagName());
        return ResponseEntity.status(HttpStatus.CREATED).body(featureFlagService.create(request));
    }

    @PutMapping("/{flagName}/{environment}")
    public ResponseEntity<FeatureFlagResponse> update(
            @PathVariable String flagName,
            @PathVariable String environment,
            @RequestBody FeatureFlagRequest request) {
        log.info("PUT /api/features/{}/{} - Update feature flag", flagName, environment);
        return ResponseEntity.ok(featureFlagService.update(flagName, environment, request));
    }

    @GetMapping("/{flagName}/{environment}")
    public ResponseEntity<FeatureFlagResponse> get(
            @PathVariable String flagName,
            @PathVariable String environment) {
        log.info("GET /api/features/{}/{} - Get feature flag", flagName, environment);
        return ResponseEntity.ok(featureFlagService.get(flagName, environment));
    }

    @GetMapping("/environment/{environment}")
    public ResponseEntity<List<FeatureFlagResponse>> getByEnvironment(
            @PathVariable String environment) {
        log.info("GET /api/features/environment/{} - Get feature flags by environment", environment);
        return ResponseEntity.ok(featureFlagService.getByEnvironment(environment));
    }

    @GetMapping("/enabled")
    public ResponseEntity<List<FeatureFlagResponse>> getEnabled() {
        log.info("GET /api/features/enabled - Get enabled feature flags");
        return ResponseEntity.ok(featureFlagService.getEnabled());
    }

    @DeleteMapping("/{flagName}/{environment}")
    public ResponseEntity<Void> delete(
            @PathVariable String flagName,
            @PathVariable String environment) {
        log.info("DELETE /api/features/{}/{} - Delete feature flag", flagName, environment);
        featureFlagService.delete(flagName, environment);
        return ResponseEntity.noContent().build();
    }
}

