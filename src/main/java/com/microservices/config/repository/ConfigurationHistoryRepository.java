package com.microservices.config.repository;

import com.microservices.config.entity.ConfigurationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConfigurationHistoryRepository extends JpaRepository<ConfigurationHistory, Long> {
    List<ConfigurationHistory> findByConfigKeyAndEnvironment(String configKey, String environment);
    List<ConfigurationHistory> findByEnvironment(String environment);
    List<ConfigurationHistory> findByConfigKey(String configKey);
}

