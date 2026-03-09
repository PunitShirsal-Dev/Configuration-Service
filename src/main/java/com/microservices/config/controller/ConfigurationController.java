package com.microservices.config.controller;

import com.microservices.config.dto.ConfigurationRequest;
import com.microservices.config.dto.ConfigurationResponse;
import com.microservices.config.entity.ConfigurationHistory;
import com.microservices.config.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
@Slf4j
public class ConfigurationController {

    private final ConfigurationService configService;

    @PostMapping
    public ResponseEntity<ConfigurationResponse> create(@RequestBody ConfigurationRequest request) {
        log.info("POST /api/config - Create configuration: {}", request.getKey());
        return ResponseEntity.status(HttpStatus.CREATED).body(configService.create(request));
    }

    @PutMapping("/{key}/{environment}")
    public ResponseEntity<ConfigurationResponse> update(
            @PathVariable String key,
            @PathVariable String environment,
            @RequestBody ConfigurationRequest request) {
        log.info("PUT /api/config/{}/{} - Update configuration", key, environment);
        return ResponseEntity.ok(configService.update(key, environment, request));
    }

    @GetMapping("/{key}/{environment}")
    public ResponseEntity<ConfigurationResponse> get(
            @PathVariable String key,
            @PathVariable String environment) {
        log.info("GET /api/config/{}/{} - Get configuration", key, environment);
        return ResponseEntity.ok(configService.get(key, environment));
    }

    @GetMapping("/environment/{environment}")
    public ResponseEntity<List<ConfigurationResponse>> getByEnvironment(
            @PathVariable String environment) {
        log.info("GET /api/config/environment/{} - Get configurations by environment", environment);
        return ResponseEntity.ok(configService.getByEnvironment(environment));
    }

    @GetMapping("/key/{key}")
    public ResponseEntity<List<ConfigurationResponse>> getByKey(@PathVariable String key) {
        log.info("GET /api/config/key/{} - Get configurations by key", key);
        return ResponseEntity.ok(configService.getByKey(key));
    }

    @DeleteMapping("/{key}/{environment}")
    public ResponseEntity<Void> delete(
            @PathVariable String key,
            @PathVariable String environment) {
        log.info("DELETE /api/config/{}/{} - Delete configuration", key, environment);
        configService.delete(key, environment);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history/{key}/{environment}")
    public ResponseEntity<List<ConfigurationHistory>> getHistory(
            @PathVariable String key,
            @PathVariable String environment) {
        log.info("GET /api/config/history/{}/{} - Get configuration history", key, environment);
        return ResponseEntity.ok(configService.getHistory(key, environment));
    }
}

