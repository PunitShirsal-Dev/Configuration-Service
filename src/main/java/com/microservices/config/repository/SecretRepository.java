package com.microservices.config.repository;

import com.microservices.config.entity.Secret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SecretRepository extends JpaRepository<Secret, Long> {
    Optional<Secret> findBySecretKeyAndEnvironment(String secretKey, String environment);
    List<Secret> findByEnvironment(String environment);
    List<Secret> findBySecretType(String secretType);
}

