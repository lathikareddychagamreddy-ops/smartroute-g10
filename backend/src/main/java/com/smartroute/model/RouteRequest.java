package com.smartroute.model;

public class RouteRequest {
    private String source;
    private String destination;
    private String preference; // FASTEST, SHORTEST, SAFEST, FUEL_EFFICIENT, LOWEST_TOLL, EV_FRIENDLY, TRAFFIC_AWARE, WEATHER_AWARE, SCENIC, EMERGENCY
    private String algorithm;  // DIJKSTRA, ASTAR, BFS, DFS (optional override, defaults by recommendation)
    private Long userId;

    public RouteRequest() {}

    public RouteRequest(String source, String destination, String preference) {
        this.source = source;
        this.destination = destination;
        this.preference = preference;
    }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getPreference() { return preference; }
    public void setPreference(String preference) { this.preference = preference; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
