package com.smartroute.model;

public class RouteMetrics {
    private double distance;           // km
    private double travelTime;         // minutes
    private double fuelCost;           // INR
    private double tollCost;           // INR
    private double safetyScore;        // 1-10
    private String trafficLevel;       // "LOW", "MODERATE", "HEAVY"
    private double weatherImpact;      // 1-10
    private double scenicScore;        // 1-10
    private int evChargingStationsCount;
    private double overallScore;       // 0-100 (higher is better for overall desirability)

    public RouteMetrics() {}

    public RouteMetrics(double distance, double travelTime, double fuelCost, double tollCost,
                        double safetyScore, String trafficLevel, double weatherImpact,
                        double scenicScore, int evChargingStationsCount, double overallScore) {
        this.distance = distance;
        this.travelTime = travelTime;
        this.fuelCost = fuelCost;
        this.tollCost = tollCost;
        this.safetyScore = safetyScore;
        this.trafficLevel = trafficLevel;
        this.weatherImpact = weatherImpact;
        this.scenicScore = scenicScore;
        this.evChargingStationsCount = evChargingStationsCount;
        this.overallScore = overallScore;
    }

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

    public String getTrafficLevel() { return trafficLevel; }
    public void setTrafficLevel(String trafficLevel) { this.trafficLevel = trafficLevel; }

    public double getWeatherImpact() { return weatherImpact; }
    public void setWeatherImpact(double weatherImpact) { this.weatherImpact = weatherImpact; }

    public double getScenicScore() { return scenicScore; }
    public void setScenicScore(double scenicScore) { this.scenicScore = scenicScore; }

    public int getEvChargingStationsCount() { return evChargingStationsCount; }
    public void setEvChargingStationsCount(int evChargingStationsCount) { this.evChargingStationsCount = evChargingStationsCount; }

    public double getOverallScore() { return overallScore; }
    public void setOverallScore(double overallScore) { this.overallScore = overallScore; }
}
