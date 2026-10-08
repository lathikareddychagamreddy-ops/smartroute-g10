package com.smartroute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "route_history")
public class RouteHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String userName;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    private String preference;
    private String algorithm;
    private double distance;
    private double estimatedTime;
    private double toll;
    private double fuelConsumption;
    private double safetyScore;
    private int evChargingStops;

    @Lob
    @Column(length = 2048)
    private String routePath;

    private LocalDateTime timestamp;

    public RouteHistoryEntity() {
        this.timestamp = LocalDateTime.now();
    }

    public RouteHistoryEntity(Long userId, String userName, String source, String destination,
                              String preference, String algorithm, double distance,
                              double estimatedTime, double toll, double fuelConsumption,
                              double safetyScore, int evChargingStops, String routePath) {
        this.userId = userId;
        this.userName = userName;
        this.source = source;
        this.destination = destination;
        this.preference = preference;
        this.algorithm = algorithm;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.toll = toll;
        this.fuelConsumption = fuelConsumption;
        this.safetyScore = safetyScore;
        this.evChargingStops = evChargingStops;
        this.routePath = routePath;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getPreference() { return preference; }
    public void setPreference(String preference) { this.preference = preference; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public double getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(double estimatedTime) { this.estimatedTime = estimatedTime; }

    public double getToll() { return toll; }
    public void setToll(double toll) { this.toll = toll; }

    public double getFuelConsumption() { return fuelConsumption; }
    public void setFuelConsumption(double fuelConsumption) { this.fuelConsumption = fuelConsumption; }

    public double getSafetyScore() { return safetyScore; }
    public void setSafetyScore(double safetyScore) { this.safetyScore = safetyScore; }

    public int getEvChargingStops() { return evChargingStops; }
    public void setEvChargingStops(int evChargingStops) { this.evChargingStops = evChargingStops; }

    public String getRoutePath() { return routePath; }
    public void setRoutePath(String routePath) { this.routePath = routePath; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
