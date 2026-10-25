package com.security.honeypot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class AttackLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ipAddress;
    private String userAgent;
    private String endpointAccessed;
    private LocalDateTime attackTime;

    // NAYE RESEARCH COLUMNS
    private int threatScore;
    private String attackType;

    // 🌍 NAYA LOCATION COLUMN
    private String location;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public String getEndpointAccessed() { return endpointAccessed; }
    public void setEndpointAccessed(String endpointAccessed) { this.endpointAccessed = endpointAccessed; }

    public LocalDateTime getAttackTime() { return attackTime; }
    public void setAttackTime(LocalDateTime attackTime) { this.attackTime = attackTime; }

    public int getThreatScore() { return threatScore; }
    public void setThreatScore(int threatScore) { this.threatScore = threatScore; }

    public String getAttackType() { return attackType; }
    public void setAttackType(String attackType) { this.attackType = attackType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}