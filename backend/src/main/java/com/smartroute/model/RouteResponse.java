package com.smartroute.model;

import com.smartroute.algorithm.Location;
import java.util.List;

public class RouteResponse {
    private String algorithm;
    private String preference;
    private List<String> route;
    private List<Location> routeCoordinates;
    private double distance;           // in km
    private double estimatedTime;      // in minutes
    private double toll;               // in INR
    private double fuelConsumption;   // in liters
    private double safetyScore;       // 1.0 - 10.0
    private double scenicScore;       // 1.0 - 10.0
    private int evChargingStops;
    private String trafficLevel;
    private String weatherCondition;
    private String rationale;
    private List<RouteOption> alternativeRoutes;

    public RouteResponse() {}

    public static class RouteOption {
        private String name;
        private String preference;
        private String algorithm;
        private List<String> route;
        private double distance;
        private double estimatedTime;
        private double toll;
        private double fuelConsumption;
        private double safetyScore;
        private int evChargingStops;
        private String tag;

        public RouteOption() {}

        public RouteOption(String name, String preference, String algorithm, List<String> route,
                           double distance, double estimatedTime, double toll, double fuelConsumption,
                           double safetyScore, int evChargingStops, String tag) {
            this.name = name;
            this.preference = preference;
            this.algorithm = algorithm;
            this.route = route;
            this.distance = distance;
            this.estimatedTime = estimatedTime;
            this.toll = toll;
            this.fuelConsumption = fuelConsumption;
            this.safetyScore = safetyScore;
            this.evChargingStops = evChargingStops;
            this.tag = tag;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getPreference() { return preference; }
        public void setPreference(String preference) { this.preference = preference; }

        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

        public List<String> getRoute() { return route; }
        public void setRoute(List<String> route) { this.route = route; }

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

        public String getTag() { return tag; }
        public void setTag(String tag) { this.tag = tag; }
    }

    // Getters and Setters
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public String getPreference() { return preference; }
    public void setPreference(String preference) { this.preference = preference; }

    public List<String> getRoute() { return route; }
    public void setRoute(List<String> route) { this.route = route; }

    public List<Location> getRouteCoordinates() { return routeCoordinates; }
    public void setRouteCoordinates(List<Location> routeCoordinates) { this.routeCoordinates = routeCoordinates; }

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

    public double getScenicScore() { return scenicScore; }
    public void setScenicScore(double scenicScore) { this.scenicScore = scenicScore; }

    public int getEvChargingStops() { return evChargingStops; }
    public void setEvChargingStops(int evChargingStops) { this.evChargingStops = evChargingStops; }

    public String getTrafficLevel() { return trafficLevel; }
    public void setTrafficLevel(String trafficLevel) { this.trafficLevel = trafficLevel; }

    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }

    public String getRationale() { return rationale; }
    public void setRationale(String rationale) { this.rationale = rationale; }

    public List<RouteOption> getAlternativeRoutes() { return alternativeRoutes; }
    public void setAlternativeRoutes(List<RouteOption> alternativeRoutes) { this.alternativeRoutes = alternativeRoutes; }
}
