package com.microservices.config.repository;

import com.microservices.config.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    Optional<Configuration> findByKeyAndEnvironment(String key, String environment);
    List<Configuration> findByEnvironment(String environment);
    List<Configuration> findByKey(String key);
}

