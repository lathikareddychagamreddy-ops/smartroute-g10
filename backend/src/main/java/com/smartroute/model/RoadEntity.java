package com.smartroute.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roads")
public class RoadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    private double distance;
    private double travelTime;
    private double toll;
    private double fuelConsumption;
    private double safetyScore;
    private String trafficLevel;
    private String weatherCondition;
    private double scenicScore;
    private boolean evChargingAvailable;
    private boolean emergencyLane;

    public RoadEntity() {}

    public RoadEntity(String source, String destination, double distance, double travelTime,
                      double toll, double fuelConsumption, double safetyScore, String trafficLevel,
                      String weatherCondition, double scenicScore, boolean evChargingAvailable, boolean emergencyLane) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.travelTime = travelTime;
        this.toll = toll;
        this.fuelConsumption = fuelConsumption;
        this.safetyScore = safetyScore;
        this.trafficLevel = trafficLevel;
        this.weatherCondition = weatherCondition;
        this.scenicScore = scenicScore;
        this.evChargingAvailable = evChargingAvailable;
        this.emergencyLane = emergencyLane;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public double getTravelTime() { return travelTime; }
    public void setTravelTime(double travelTime) { this.travelTime = travelTime; }

    public double getToll() { return toll; }
    public void setToll(double toll) { this.toll = toll; }

    public double getFuelConsumption() { return fuelConsumption; }
    public void setFuelConsumption(double fuelConsumption) { this.fuelConsumption = fuelConsumption; }

    public double getSafetyScore() { return safetyScore; }
    public void setSafetyScore(double safetyScore) { this.safetyScore = safetyScore; }

    public String getTrafficLevel() { return trafficLevel; }
    public void setTrafficLevel(String trafficLevel) { this.trafficLevel = trafficLevel; }

    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }

    public double getScenicScore() { return scenicScore; }
    public void setScenicScore(double scenicScore) { this.scenicScore = scenicScore; }

    public boolean isEvChargingAvailable() { return evChargingAvailable; }
    public void setEvChargingAvailable(boolean evChargingAvailable) { this.evChargingAvailable = evChargingAvailable; }

    public boolean isEmergencyLane() { return emergencyLane; }
    public void setEmergencyLane(boolean emergencyLane) { this.emergencyLane = emergencyLane; }
}
