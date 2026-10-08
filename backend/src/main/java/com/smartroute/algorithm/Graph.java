package com.smartroute.algorithm;

import java.util.*;

/**
 * Graph - Adjacency List Weighted Graph Representation
 * Manages vertices (Locations) and directed/undirected weighted edges (Roads).
 */
public class Graph {
    private final Map<String, Location> locationMap = new HashMap<>();
    private final Map<String, List<Road>> adjacencyList = new HashMap<>();

    public Graph() {}

    /**
     * Adds a vertex (Location) to the graph.
     */
    public void addVertex(Location location) {
        if (location == null || location.getName() == null) return;
        String key = location.getName().trim();
        locationMap.put(key, location);
        adjacencyList.putIfAbsent(key, new ArrayList<>());
    }

    /**
     * Adds an edge (Road) to the graph. If bidirectional, adds reverse direction edge as well.
     */
    public void addEdge(Road road, boolean bidirectional) {
        if (road == null || road.getSource() == null || road.getDestination() == null) return;

        String src = road.getSource().trim();
        String dest = road.getDestination().trim();

        adjacencyList.putIfAbsent(src, new ArrayList<>());
        adjacencyList.putIfAbsent(dest, new ArrayList<>());

        adjacencyList.get(src).add(road);

        if (bidirectional) {
            // Create symmetric reverse road edge
            Road reverseRoad = new Road(
                road.getId() != null ? -road.getId() : null,
                dest,
                src,
                road.getDistance(),
                road.getTravelTime(),
                road.getToll(),
                road.getFuelConsumption(),
                road.getSafetyScore(),
                road.getTrafficLevel(),
                road.getWeatherCondition(),
                road.getScenicScore(),
                road.isEvChargingAvailable(),
                road.isEmergencyLane()
            );
            adjacencyList.get(dest).add(reverseRoad);
        }
    }

    public List<Road> getNeighbors(String locationName) {
        if (locationName == null) return Collections.emptyList();
        return adjacencyList.getOrDefault(locationName.trim(), Collections.emptyList());
    }

    public boolean hasVertex(String locationName) {
        return locationName != null && adjacencyList.containsKey(locationName.trim());
    }

    public Location getLocation(String locationName) {
        if (locationName == null) return null;
        return locationMap.get(locationName.trim());
    }

    public Set<String> getVertices() {
        return Collections.unmodifiableSet(adjacencyList.keySet());
    }

    public Map<String, Location> getLocationMap() {
        return Collections.unmodifiableMap(locationMap);
    }

    public Map<String, List<Road>> getAdjacencyList() {
        return Collections.unmodifiableMap(adjacencyList);
    }

    public int getVertexCount() {
        return adjacencyList.size();
    }

    public int getEdgeCount() {
        int total = 0;
        for (List<Road> edges : adjacencyList.values()) {
            total += edges.size();
        }
        return total;
    }

    /**
     * Computes direct geographic distance between two nodes using Haversine formula (in km).
     */
    public double calculateHaversineDistance(String srcName, String destName) {
        Location src = getLocation(srcName);
        Location dest = getLocation(destName);
        if (src == null || dest == null) return 0.0;

        final int R = 6371; // Earth radius in km
        double lat1 = Math.toRadians(src.getLatitude());
        double lon1 = Math.toRadians(src.getLongitude());
        double lat2 = Math.toRadians(dest.getLatitude());
        double lon2 = Math.toRadians(dest.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
