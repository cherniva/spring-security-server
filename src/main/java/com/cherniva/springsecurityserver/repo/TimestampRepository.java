package com.cherniva.springsecurityserver.repo;

import com.cherniva.springsecurityserver.model.Timestamp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimestampRepository extends JpaRepository<Timestamp, Long> {
    Timestamp findByTimestamp(String timestamp);
}