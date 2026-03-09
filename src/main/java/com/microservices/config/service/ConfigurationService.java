package com.microservices.config.service;

import com.microservices.config.dto.ConfigurationRequest;
import com.microservices.config.dto.ConfigurationResponse;
import com.microservices.config.entity.Configuration;
import com.microservices.config.entity.ConfigurationHistory;
import com.microservices.config.exception.ConfigurationNotFoundException;
import com.microservices.config.repository.ConfigurationRepository;
import com.microservices.config.repository.ConfigurationHistoryRepository;
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
public class ConfigurationService {

    private final ConfigurationRepository configRepository;
    private final ConfigurationHistoryRepository historyRepository;

    @Transactional
    public ConfigurationResponse create(ConfigurationRequest request) {
        log.info("Creating configuration: key={}, environment={}", request.getKey(), request.getEnvironment());

        Configuration config = new Configuration();
        config.setKey(request.getKey());
        config.setValue(request.getValue());
        config.setEnvironment(request.getEnvironment());
        config.setDescription(request.getDescription());
        config.setEncrypted(request.getEncrypted() != null ? request.getEncrypted() : false);
        config.setVersion(1L);
        config.setCreatedAt(LocalDateTime.now());
        config.setUpdatedAt(LocalDateTime.now());
        config.setCreatedBy("system");
        config.setUpdatedBy("system");

        Configuration saved = configRepository.save(config);
        recordHistory(saved, null, "CREATE");
        return mapToResponse(saved);
    }

    @Transactional
    public ConfigurationResponse update(String key, String environment, ConfigurationRequest request) {
        log.info("Updating configuration: key={}, environment={}", key, environment);

        Configuration config = configRepository.findByKeyAndEnvironment(key, environment)
                .orElseThrow(() -> new ConfigurationNotFoundException("Configuration not found"));

        String oldValue = config.getValue();
        config.setValue(request.getValue());
        config.setDescription(request.getDescription());
        config.setEncrypted(request.getEncrypted() != null ? request.getEncrypted() : false);
        config.setVersion(config.getVersion() + 1);
        config.setUpdatedAt(LocalDateTime.now());
        config.setUpdatedBy("system");

        Configuration updated = configRepository.save(config);
        recordHistory(updated, oldValue, "UPDATE");
        return mapToResponse(updated);
    }

    public ConfigurationResponse get(String key, String environment) {
        Configuration config = configRepository.findByKeyAndEnvironment(key, environment)
                .orElseThrow(() -> new ConfigurationNotFoundException("Configuration not found"));
        return mapToResponse(config);
    }

    public List<ConfigurationResponse> getByEnvironment(String environment) {
        return configRepository.findByEnvironment(environment).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ConfigurationResponse> getByKey(String key) {
        return configRepository.findByKey(key).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(String key, String environment) {
        Configuration config = configRepository.findByKeyAndEnvironment(key, environment)
                .orElseThrow(() -> new ConfigurationNotFoundException("Configuration not found"));
        configRepository.delete(config);
        recordHistory(config, config.getValue(), "DELETE");
        log.info("Configuration deleted: key={}, environment={}", key, environment);
    }

    public List<ConfigurationHistory> getHistory(String key, String environment) {
        return historyRepository.findByConfigKeyAndEnvironment(key, environment);
    }

    private void recordHistory(Configuration config, String oldValue, String changeType) {
        ConfigurationHistory history = new ConfigurationHistory();
        history.setConfigKey(config.getKey());
        history.setOldValue(oldValue);
        history.setNewValue(config.getValue());
        history.setEnvironment(config.getEnvironment());
        history.setChangeType(changeType);
        history.setDescription("Configuration " + changeType.toLowerCase());
        history.setChangedAt(LocalDateTime.now());
        history.setChangedBy("system");
        historyRepository.save(history);
    }

    private ConfigurationResponse mapToResponse(Configuration config) {
        return new ConfigurationResponse(
                config.getId(),
                config.getKey(),
                config.getValue(),
                config.getEnvironment(),
                config.getVersion(),
                config.getDescription(),
                config.getEncrypted(),
                config.getCreatedAt(),
                config.getUpdatedAt(),
                config.getCreatedBy(),
                config.getUpdatedBy()
        );
    }
}

