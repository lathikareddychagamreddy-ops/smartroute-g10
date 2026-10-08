package com.smartroute.model;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private String id;
    private String algorithm;          // "Dijkstra", "A*", "Modified Dijkstra", "BFS", "DFS"
    private String name;               // "Shortest Distance", "Fastest Route", "Recommended Multi-Criteria", "EV Charging Route", "Scenic Corridor"
    private List<String> path = new ArrayList<>();
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();
    private RouteMetrics metrics;
    private List<ChargingStation> chargingStops = new ArrayList<>();
    private List<String> scenicHighlights = new ArrayList<>();
    private String recommendationReason;
    private double executionTimeMs;
    private boolean recommended;

    public Route() {}

    public Route(String id, String algorithm, String name) {
        this.id = id;
        this.algorithm = algorithm;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getPath() { return path; }
    public void setPath(List<String> path) { this.path = path; }

    public List<Node> getNodes() { return nodes; }
    public void setNodes(List<Node> nodes) { this.nodes = nodes; }

    public List<Edge> getEdges() { return edges; }
    public void setEdges(List<Edge> edges) { this.edges = edges; }

    public RouteMetrics getMetrics() { return metrics; }
    public void setMetrics(RouteMetrics metrics) { this.metrics = metrics; }

    public List<ChargingStation> getChargingStops() { return chargingStops; }
    public void setChargingStops(List<ChargingStation> chargingStops) { this.chargingStops = chargingStops; }

    public List<String> getScenicHighlights() { return scenicHighlights; }
    public void setScenicHighlights(List<String> scenicHighlights) { this.scenicHighlights = scenicHighlights; }

    public String getRecommendationReason() { return recommendationReason; }
    public void setRecommendationReason(String recommendationReason) { this.recommendationReason = recommendationReason; }

    public double getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(double executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public boolean isRecommended() { return recommended; }
    public void setRecommended(boolean recommended) { this.recommended = recommended; }
}
