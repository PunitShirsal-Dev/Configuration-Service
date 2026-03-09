package com.microservices.config.service;

import com.microservices.config.dto.FeatureFlagRequest;
import com.microservices.config.dto.FeatureFlagResponse;
import com.microservices.config.entity.FeatureFlag;
import com.microservices.config.exception.ConfigurationNotFoundException;
import com.microservices.config.repository.FeatureFlagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureFlagService {

    private final FeatureFlagRepository featureFlagRepository;

    @Transactional
    public FeatureFlagResponse create(FeatureFlagRequest request) {
        log.info("Creating feature flag: flagName={}, environment={}", request.getFlagName(), request.getEnvironment());

        FeatureFlag flag = new FeatureFlag();
        flag.setFlagName(request.getFlagName());
        flag.setEnabled(request.getEnabled());
        flag.setDescription(request.getDescription());
        flag.setEnvironment(request.getEnvironment());
        flag.setPercentageRollout(request.getPercentageRollout() != null ? request.getPercentageRollout() : 100);
        flag.setCreatedAt(LocalDateTime.now());
        flag.setUpdatedAt(LocalDateTime.now());
        flag.setCreatedBy("system");

        FeatureFlag saved = featureFlagRepository.save(flag);
        return mapToResponse(saved);
    }

    @Transactional
    public FeatureFlagResponse update(String flagName, String environment, FeatureFlagRequest request) {
        log.info("Updating feature flag: flagName={}, environment={}", flagName, environment);

        FeatureFlag flag = featureFlagRepository.findByFlagNameAndEnvironment(flagName, environment)
                .orElseThrow(() -> new ConfigurationNotFoundException("Feature flag not found"));

        flag.setEnabled(request.getEnabled());
        flag.setDescription(request.getDescription());
        flag.setPercentageRollout(request.getPercentageRollout() != null ? request.getPercentageRollout() : 100);
        flag.setUpdatedAt(LocalDateTime.now());

        FeatureFlag updated = featureFlagRepository.save(flag);
        return mapToResponse(updated);
    }

    public FeatureFlagResponse get(String flagName, String environment) {
        FeatureFlag flag = featureFlagRepository.findByFlagNameAndEnvironment(flagName, environment)
                .orElseThrow(() -> new ConfigurationNotFoundException("Feature flag not found"));
        return mapToResponse(flag);
    }

    public List<FeatureFlagResponse> getByEnvironment(String environment) {
        return featureFlagRepository.findByEnvironment(environment).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<FeatureFlagResponse> getEnabled() {
        return featureFlagRepository.findByEnabledTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(String flagName, String environment) {
        FeatureFlag flag = featureFlagRepository.findByFlagNameAndEnvironment(flagName, environment)
                .orElseThrow(() -> new ConfigurationNotFoundException("Feature flag not found"));
        featureFlagRepository.delete(flag);
        log.info("Feature flag deleted: flagName={}, environment={}", flagName, environment);
    }

    private FeatureFlagResponse mapToResponse(FeatureFlag flag) {
        return new FeatureFlagResponse(
                flag.getId(),
                flag.getFlagName(),
                flag.getEnabled(),
                flag.getDescription(),
                flag.getEnvironment(),
                flag.getPercentageRollout(),
                flag.getCreatedAt(),
                flag.getUpdatedAt(),
                flag.getCreatedBy()
        );
    }
}

