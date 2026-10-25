package com.security.honeypot.repository;

import com.security.honeypot.model.AttackLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttackLogRepository extends JpaRepository<AttackLog, Long> {
    // NAYA FEATURE: Ek IP address ne kitni baar attack kiya, yeh count karega
    long countByIpAddress(String ipAddress);
}