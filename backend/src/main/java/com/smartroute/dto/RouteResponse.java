package com.smartroute.dto;

import com.smartroute.model.Route;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteResponse {
    private String startLocation;
    private String destination;
    private Route shortestRoute;      // Dijkstra
    private Route fastestRoute;       // A*
    private Route recommendedRoute;   // Modified Dijkstra
    private Route evChargingRoute;    // BFS
    private Route scenicRoute;        // DFS
    private List<Route> allRoutes = new ArrayList<>();
    private Map<String, Object> comparison = new HashMap<>();
    private Map<String, Double> executionTimes = new HashMap<>();
    private boolean success = true;
    private String message;

    public RouteResponse() {}

    public RouteResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Route getShortestRoute() { return shortestRoute; }
    public void setShortestRoute(Route shortestRoute) { this.shortestRoute = shortestRoute; }

    public Route getFastestRoute() { return fastestRoute; }
    public void setFastestRoute(Route fastestRoute) { this.fastestRoute = fastestRoute; }

    public Route getRecommendedRoute() { return recommendedRoute; }
    public void setRecommendedRoute(Route recommendedRoute) { this.recommendedRoute = recommendedRoute; }

    public Route getEvChargingRoute() { return evChargingRoute; }
    public void setEvChargingRoute(Route evChargingRoute) { this.evChargingRoute = evChargingRoute; }

    public Route getScenicRoute() { return scenicRoute; }
    public void setScenicRoute(Route scenicRoute) { this.scenicRoute = scenicRoute; }

    public List<Route> getAllRoutes() { return allRoutes; }
    public void setAllRoutes(List<Route> allRoutes) { this.allRoutes = allRoutes; }

    public Map<String, Object> getComparison() { return comparison; }
    public void setComparison(Map<String, Object> comparison) { this.comparison = comparison; }

    public Map<String, Double> getExecutionTimes() { return executionTimes; }
    public void setExecutionTimes(Map<String, Double> executionTimes) { this.executionTimes = executionTimes; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
