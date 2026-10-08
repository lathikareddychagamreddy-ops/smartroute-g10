package com.smartroute.util;

import com.smartroute.model.Edge;
import com.smartroute.model.Node;
import com.smartroute.model.RouteMetrics;
import com.smartroute.model.UserPreferences;

import java.util.List;

public class RouteScorer {

    public static RouteMetrics calculateMetrics(List<Edge> edges, List<Node> nodes, String vehicleType, String trafficCondition, String weatherCondition) {
        if (edges == null || edges.isEmpty()) {
            return new RouteMetrics(0, 0, 0, 0, 10.0, "LOW", 0, 0, 0, 100.0);
        }

        double totalDist = 0;
        double totalTime = 0;
        double totalFuelCost = 0;
        double totalTollCost = 0;
        double sumSafety = 0;
        double sumScenic = 0;
        double sumWeather = 0;
        int maxTrafficLevelCode = 1; // 1: LOW, 2: MODERATE, 3: HEAVY

        double trafficMultiplier = getTrafficMultiplier(trafficCondition);
        double weatherMultiplier = getWeatherMultiplier(weatherCondition);

        for (Edge edge : edges) {
            totalDist += edge.getDistance();

            // Adjust travel time by edge traffic factor and global condition
            double effectiveTrafficFactor = edge.getTrafficFactor() * (trafficMultiplier / 1.0);
            totalTime += edge.getTravelTime() * (effectiveTrafficFactor / edge.getTrafficFactor());

            // Fuel cost based on vehicle type
            double baseFuel = edge.getFuelCost();
            if ("Electric".equalsIgnoreCase(vehicleType)) {
                // EV uses electricity: ~ ₹2.2 per km vs ₹8-9 per km petrol
                totalFuelCost += edge.getDistance() * 2.2;
            } else if ("Diesel".equalsIgnoreCase(vehicleType)) {
                totalFuelCost += baseFuel * 0.88;
            } else {
                totalFuelCost += baseFuel;
            }

            totalTollCost += edge.getTollCost();
            sumSafety += edge.getSafetyScore();
            sumScenic += edge.getScenicScore();
            sumWeather += (edge.getWeatherImpact() * weatherMultiplier);

            int code = "HEAVY".equalsIgnoreCase(edge.getTrafficLevel()) ? 3 :
                       "MODERATE".equalsIgnoreCase(edge.getTrafficLevel()) ? 2 : 1;
            if (code > maxTrafficLevelCode) {
                maxTrafficLevelCode = code;
            }
        }

        int count = edges.size();
        double avgSafety = sumSafety / count;
        double avgScenic = sumScenic / count;
        double avgWeather = sumWeather / count;

        String trafficLevelStr = maxTrafficLevelCode == 3 ? "HEAVY" : (maxTrafficLevelCode == 2 ? "MODERATE" : "LOW");

        // Count EV charging stations along the route nodes
        int evChargingCount = 0;
        if (nodes != null) {
            for (Node n : nodes) {
                if (n.isHasEvCharging() && n.getChargingStations() != null) {
                    evChargingCount += n.getChargingStations().size();
                }
            }
        }

        RouteMetrics metrics = new RouteMetrics();
        metrics.setDistance(Math.round(totalDist * 10.0) / 10.0);
        metrics.setTravelTime(Math.round(totalTime * 10.0) / 10.0);
        metrics.setFuelCost(Math.round(totalFuelCost * 10.0) / 10.0);
        metrics.setTollCost(Math.round(totalTollCost * 10.0) / 10.0);
        metrics.setSafetyScore(Math.round(avgSafety * 10.0) / 10.0);
        metrics.setTrafficLevel(trafficLevelStr);
        metrics.setWeatherImpact(Math.round(avgWeather * 10.0) / 10.0);
        metrics.setScenicScore(Math.round(avgScenic * 10.0) / 10.0);
        metrics.setEvChargingStationsCount(evChargingCount);

        return metrics;
    }

    public static double getTrafficMultiplier(String trafficCondition) {
        if (trafficCondition == null) return 1.0;
        switch (trafficCondition.toUpperCase()) {
            case "HEAVY": return 1.6;
            case "MODERATE": return 1.25;
            case "NORMAL":
            default: return 1.0;
        }
    }

    public static double getWeatherMultiplier(String weatherCondition) {
        if (weatherCondition == null) return 1.0;
        switch (weatherCondition.toUpperCase()) {
            case "STORM":
            case "BAD WEATHER": return 2.2;
            case "RAIN": return 1.5;
            case "FOG": return 1.4;
            case "CLEAR":
            case "NORMAL":
            default: return 1.0;
        }
    }

    public static double calculateOverallScore(RouteMetrics metrics, UserPreferences prefs) {
        if (metrics == null || prefs == null) return 80.0;

        // Normalize metrics into 0 - 100 positive utility scale
        // Distance utility: 100 at 0km, decreases as distance grows (typical trip 5 - 40km)
        double distUtility = Math.max(0, 100 - (metrics.getDistance() * 2.0));

        // Time utility: 100 at 0 mins, decreases as time grows (typical trip 5 - 60 mins)
        double timeUtility = Math.max(0, 100 - (metrics.getTravelTime() * 1.5));

        // Safety utility: (safety / 10.0) * 100
        double safetyUtility = (metrics.getSafetyScore() / 10.0) * 100.0;

        // Fuel utility: 100 - (fuel / 3.0)
        double fuelUtility = Math.max(0, 100 - (metrics.getFuelCost() * 0.3));

        // Toll utility: 100 if 0 toll, decreases with tolls
        double tollUtility = Math.max(0, 100 - (metrics.getTollCost() * 0.8));

        // Traffic utility: LOW = 95, MODERATE = 70, HEAVY = 35
        double trafficUtility = "LOW".equalsIgnoreCase(metrics.getTrafficLevel()) ? 95.0 :
                               ("MODERATE".equalsIgnoreCase(metrics.getTrafficLevel()) ? 70.0 : 35.0);

        // Weather safety utility: 100 - (weatherImpact * 8)
        double weatherUtility = Math.max(0, 100 - (metrics.getWeatherImpact() * 8.0));

        // Scenic utility: (scenicScore / 10.0) * 100
        double scenicUtility = (metrics.getScenicScore() / 10.0) * 100.0;

        double wDist = prefs.getDistanceWeight();
        double wTime = prefs.getTimeWeight();
        double wSafety = prefs.getSafetyWeight() + (prefs.isPreferSafeRoads() ? 0.4 : 0.0);
        double wFuel = prefs.getFuelWeight();
        double wToll = prefs.getTollWeight() + (prefs.isAvoidTolls() ? 0.8 : 0.0);
        double wTraffic = prefs.getTrafficWeight() + (prefs.isAvoidHeavyTraffic() ? 0.5 : 0.0);
        double wWeather = prefs.getWeatherWeight() + (prefs.isWeatherSensitive() ? 0.5 : 0.0);
        double wScenic = prefs.getScenicWeight() + (prefs.isPreferScenicRoads() ? 0.6 : 0.0);

        double totalWeight = wDist + wTime + wSafety + wFuel + wToll + wTraffic + wWeather + wScenic;
        if (totalWeight <= 0) totalWeight = 1.0;

        double weightedSum = (distUtility * wDist) +
                             (timeUtility * wTime) +
                             (safetyUtility * wSafety) +
                             (fuelUtility * wFuel) +
                             (tollUtility * wToll) +
                             (trafficUtility * wTraffic) +
                             (weatherUtility * wWeather) +
                             (scenicUtility * wScenic);

        double finalScore = weightedSum / totalWeight;
        return Math.round(Math.min(100.0, Math.max(0.0, finalScore)) * 10.0) / 10.0;
    }

    public static String generateRecommendationReason(RouteMetrics recMetrics, RouteMetrics shortestMetrics, RouteMetrics fastestMetrics, UserPreferences prefs) {
        StringBuilder sb = new StringBuilder();
        sb.append("Recommended by Multi-Criteria Optimization engine: ");

        if (recMetrics.getTollCost() == 0 && (fastestMetrics != null && fastestMetrics.getTollCost() > 0)) {
            sb.append("Avoids ₹").append((int)fastestMetrics.getTollCost()).append(" in highway tolls ");
        }

        if (recMetrics.getSafetyScore() >= 8.8) {
            sb.append("prioritizes high-safety multi-lane divided roads (Safety: ").append(recMetrics.getSafetyScore()).append("/10) ");
        }

        if ("LOW".equalsIgnoreCase(recMetrics.getTrafficLevel())) {
            sb.append("with free-flowing traffic, ");
        }

        if (recMetrics.getScenicScore() >= 8.0) {
            sb.append("traversing scenic corridors like KBR Park & Durgam Cheruvu bridge, ");
        }

        if ("Electric".equalsIgnoreCase(prefs.getVehicleType()) || prefs.isRequireEvCharging()) {
            sb.append("with ").append(recMetrics.getEvChargingStationsCount()).append(" high-speed EV charging bays available along route. ");
        }

        sb.append(String.format("Delivers an optimal multi-criteria utility score of %.1f/100.", recMetrics.getOverallScore()));
        return sb.toString();
    }
}
