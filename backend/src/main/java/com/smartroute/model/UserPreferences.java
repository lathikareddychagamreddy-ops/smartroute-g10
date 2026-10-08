package com.smartroute.model;

public class UserPreferences {
    private double distanceWeight = 0.5;
    private double timeWeight = 0.8;
    private double safetyWeight = 0.7;
    private double fuelWeight = 0.5;
    private double tollWeight = 0.4;
    private double trafficWeight = 0.8;
    private double weatherWeight = 0.6;
    private double scenicWeight = 0.3;

    private boolean avoidTolls = false;
    private boolean avoidHeavyTraffic = true;
    private boolean preferSafeRoads = false;
    private boolean preferScenicRoads = false;
    private boolean requireEvCharging = false;
    private boolean weatherSensitive = false;

    private String trafficCondition = "Normal";  // "Normal", "Moderate", "Heavy"
    private String weatherCondition = "Clear";   // "Clear", "Rain", "Storm", "Fog"
    private String vehicleType = "Petrol";       // "Petrol", "Diesel", "Electric"

    public UserPreferences() {}

    public double getDistanceWeight() { return distanceWeight; }
    public void setDistanceWeight(double distanceWeight) { this.distanceWeight = distanceWeight; }

    public double getTimeWeight() { return timeWeight; }
    public void setTimeWeight(double timeWeight) { this.timeWeight = timeWeight; }

    public double getSafetyWeight() { return safetyWeight; }
    public void setSafetyWeight(double safetyWeight) { this.safetyWeight = safetyWeight; }

    public double getFuelWeight() { return fuelWeight; }
    public void setFuelWeight(double fuelWeight) { this.fuelWeight = fuelWeight; }

    public double getTollWeight() { return tollWeight; }
    public void setTollWeight(double tollWeight) { this.tollWeight = tollWeight; }

    public double getTrafficWeight() { return trafficWeight; }
    public void setTrafficWeight(double trafficWeight) { this.trafficWeight = trafficWeight; }

    public double getWeatherWeight() { return weatherWeight; }
    public void setWeatherWeight(double weatherWeight) { this.weatherWeight = weatherWeight; }

    public double getScenicWeight() { return scenicWeight; }
    public void setScenicWeight(double scenicWeight) { this.scenicWeight = scenicWeight; }

    public boolean isAvoidTolls() { return avoidTolls; }
    public void setAvoidTolls(boolean avoidTolls) { this.avoidTolls = avoidTolls; }

    public boolean isAvoidHeavyTraffic() { return avoidHeavyTraffic; }
    public void setAvoidHeavyTraffic(boolean avoidHeavyTraffic) { this.avoidHeavyTraffic = avoidHeavyTraffic; }

    public boolean isPreferSafeRoads() { return preferSafeRoads; }
    public void setPreferSafeRoads(boolean preferSafeRoads) { this.preferSafeRoads = preferSafeRoads; }

    public boolean isPreferScenicRoads() { return preferScenicRoads; }
    public void setPreferScenicRoads(boolean preferScenicRoads) { this.preferScenicRoads = preferScenicRoads; }

    public boolean isRequireEvCharging() { return requireEvCharging; }
    public void setRequireEvCharging(boolean requireEvCharging) { this.requireEvCharging = requireEvCharging; }

    public boolean isWeatherSensitive() { return weatherSensitive; }
    public void setWeatherSensitive(boolean weatherSensitive) { this.weatherSensitive = weatherSensitive; }

    public String getTrafficCondition() { return trafficCondition; }
    public void setTrafficCondition(String trafficCondition) { this.trafficCondition = trafficCondition; }

    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
}
