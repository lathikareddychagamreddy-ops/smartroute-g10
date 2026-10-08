package com.smartroute.model;

public class Edge {
    private Long id;
    private String source;
    private String destination;
    private double distance;           // in km
    private double travelTime;        // in minutes
    private double fuelCost;          // in INR
    private double tollCost;          // in INR
    private double safetyScore;       // 1-10 (10 = safest expressway, divided lanes)
    private double trafficFactor;     // 1.0 (free flow) to 3.0 (heavy congestion)
    private String trafficLevel;      // "LOW", "MODERATE", "HEAVY"
    private double weatherImpact;     // 1-10 (10 = severe weather risk/water logging)
    private String weatherCondition;  // "CLEAR", "RAIN", "STORM", "FOG"
    private double scenicScore;       // 1-10 (10 = green corridor / lake view)
    private boolean evChargingAvailable;
    private String roadName;
    private String roadType;          // "Expressway", "Arterial", "City Road", "Scenic Corridor"

    public Edge() {}

    public Edge(Long id, String source, String destination, double distance, double travelTime,
                double fuelCost, double tollCost, double safetyScore, double trafficFactor,
                String trafficLevel, double weatherImpact, String weatherCondition,
                double scenicScore, boolean evChargingAvailable, String roadName, String roadType) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.travelTime = travelTime;
        this.fuelCost = fuelCost;
        this.tollCost = tollCost;
        this.safetyScore = safetyScore;
        this.trafficFactor = trafficFactor;
        this.trafficLevel = trafficLevel;
        this.weatherImpact = weatherImpact;
        this.weatherCondition = weatherCondition;
        this.scenicScore = scenicScore;
        this.evChargingAvailable = evChargingAvailable;
        this.roadName = roadName;
        this.roadType = roadType;
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

    public double getFuelCost() { return fuelCost; }
    public void setFuelCost(double fuelCost) { this.fuelCost = fuelCost; }

    public double getTollCost() { return tollCost; }
    public void setTollCost(double tollCost) { this.tollCost = tollCost; }

    public double getSafetyScore() { return safetyScore; }
    public void setSafetyScore(double safetyScore) { this.safetyScore = safetyScore; }

    public double getTrafficFactor() { return trafficFactor; }
    public void setTrafficFactor(double trafficFactor) { this.trafficFactor = trafficFactor; }

    public String getTrafficLevel() { return trafficLevel; }
    public void setTrafficLevel(String trafficLevel) { this.trafficLevel = trafficLevel; }

    public double getWeatherImpact() { return weatherImpact; }
    public void setWeatherImpact(double weatherImpact) { this.weatherImpact = weatherImpact; }

    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }

    public double getScenicScore() { return scenicScore; }
    public void setScenicScore(double scenicScore) { this.scenicScore = scenicScore; }

    public boolean isEvChargingAvailable() { return evChargingAvailable; }
    public void setEvChargingAvailable(boolean evChargingAvailable) { this.evChargingAvailable = evChargingAvailable; }

    public String getRoadName() { return roadName; }
    public void setRoadName(String roadName) { this.roadName = roadName; }

    public String getRoadType() { return roadType; }
    public void setRoadType(String roadType) { this.roadType = roadType; }
}
