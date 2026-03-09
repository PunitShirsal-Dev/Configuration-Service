package com.microservices.config.repository;

import com.microservices.config.entity.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
    Optional<FeatureFlag> findByFlagNameAndEnvironment(String flagName, String environment);
    List<FeatureFlag> findByEnvironment(String environment);
    List<FeatureFlag> findByEnabledTrue();
}

