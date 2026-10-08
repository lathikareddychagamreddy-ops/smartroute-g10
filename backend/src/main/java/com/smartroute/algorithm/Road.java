package com.smartroute.algorithm;

/**
 * Road - Graph Edge Representation with Multi-Criteria Attributes
 * Connects two locations with real-world road parameters.
 */
public class Road {
    private Long id;
    private String source;
    private String destination;
    private double distance;          // in kilometers
    private double travelTime;       // in minutes
    private double toll;              // in INR
    private double fuelConsumption;  // in liters
    private double safetyScore;      // 1.0 (unsafe) to 10.0 (safest)
    private String trafficLevel;     // "LOW", "MEDIUM", "HIGH", "SEVERE"
    private String weatherCondition; // "CLEAR", "RAINY", "FOGGY", "STORMY"
    private double scenicScore;      // 1.0 to 10.0
    private boolean evChargingAvailable;
    private boolean emergencyLane;

    public Road() {}

    public Road(Long id, String source, String destination, double distance, double travelTime,
                double toll, double fuelConsumption, double safetyScore, String trafficLevel,
                String weatherCondition, double scenicScore, boolean evChargingAvailable, boolean emergencyLane) {
        this.id = id;
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

    // Getters and Setters
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

    /**
     * Calculates the dynamic weighted cost of traversing this road based on user preference.
     */
    public double calculateCostForPreference(String preference) {
        if (preference == null) preference = "FASTEST";
        String pref = preference.toUpperCase().trim();

        double trafficFactor = getTrafficMultiplier();
        double weatherFactor = getWeatherMultiplier();

        switch (pref) {
            case "SHORTEST":
                // Pure distance minimization
                return distance;

            case "FASTEST":
                // Travel time scaled by real-time traffic
                return travelTime * trafficFactor;

            case "SAFEST":
                // Invert safety score (high safety = lower cost) + consider weather
                double safetyPenalty = Math.max(0.1, (10.5 - safetyScore));
                return distance * safetyPenalty * (weatherFactor > 1.2 ? 1.4 : 1.0);

            case "FUEL_EFFICIENT":
            case "LOWEST_FUEL":
                // Fuel consumption + stop-and-go traffic penalty
                return fuelConsumption * 15.0 + (distance * 0.2) * trafficFactor;

            case "LOWEST_TOLL":
                // Heavy penalty on toll prices
                return (toll * 2.5) + distance;

            case "EV_FRIENDLY":
            case "EV_CHARGING":
                // Strong bonus for roads with EV chargers, penalty if no charger
                double evMultiplier = evChargingAvailable ? 0.35 : 2.8;
                return distance * evMultiplier;

            case "TRAFFIC_AWARE":
            case "TRAFFIC_AVOIDANCE":
                // Severe penalty on high/severe traffic
                return travelTime * Math.pow(trafficFactor, 2.2);

            case "WEATHER_AWARE":
                // Severe penalty on rain/storm/fog
                return travelTime * Math.pow(weatherFactor, 2.5);

            case "SCENIC":
                // Higher scenic score produces lower traversal cost
                double scenicInversion = Math.max(0.1, (11.0 - scenicScore));
                return distance * (scenicInversion / 5.0);

            case "EMERGENCY":
                // Emergency vehicles prioritize fast travel, wide/emergency lanes, low traffic, safe roads
                double emerFactor = emergencyLane ? 0.5 : 1.5;
                return travelTime * trafficFactor * emerFactor * (10.0 / Math.max(4.0, safetyScore));

            default:
                return travelTime * trafficFactor;
        }
    }

    public double getTrafficMultiplier() {
        if (trafficLevel == null) return 1.0;
        switch (trafficLevel.toUpperCase()) {
            case "SEVERE": return 2.8;
            case "HIGH": return 2.0;
            case "MEDIUM": return 1.35;
            case "LOW":
            default: return 1.0;
        }
    }

    public double getWeatherMultiplier() {
        if (weatherCondition == null) return 1.0;
        switch (weatherCondition.toUpperCase()) {
            case "STORMY": return 2.5;
            case "FOGGY": return 1.8;
            case "RAINY": return 1.4;
            case "CLEAR":
            default: return 1.0;
        }
    }
}
