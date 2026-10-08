package com.smartroute.model;

import java.util.*;

public class Graph {
    private final Map<String, Node> nodeMap = new HashMap<>();
    private final Map<String, List<Edge>> adjacencyList = new HashMap<>();

    public Graph() {}

    public void addNode(Node node) {
        if (node == null || node.getName() == null) return;
        String key = node.getName().trim();
        nodeMap.put(key, node);
        adjacencyList.putIfAbsent(key, new ArrayList<>());
    }

    public void addEdge(Edge edge, boolean bidirectional) {
        if (edge == null || edge.getSource() == null || edge.getDestination() == null) return;

        String src = edge.getSource().trim();
        String dest = edge.getDestination().trim();

        adjacencyList.putIfAbsent(src, new ArrayList<>());
        adjacencyList.putIfAbsent(dest, new ArrayList<>());

        adjacencyList.get(src).add(edge);

        if (bidirectional) {
            Edge reverseEdge = new Edge(
                edge.getId() != null ? -edge.getId() : null,
                dest,
                src,
                edge.getDistance(),
                edge.getTravelTime(),
                edge.getFuelCost(),
                edge.getTollCost(),
                edge.getSafetyScore(),
                edge.getTrafficFactor(),
                edge.getTrafficLevel(),
                edge.getWeatherImpact(),
                edge.getWeatherCondition(),
                edge.getScenicScore(),
                edge.isEvChargingAvailable(),
                edge.getRoadName(),
                edge.getRoadType()
            );
            adjacencyList.get(dest).add(reverseEdge);
        }
    }

    public List<Edge> getNeighbors(String nodeName) {
        if (nodeName == null) return Collections.emptyList();
        return adjacencyList.getOrDefault(nodeName.trim(), Collections.emptyList());
    }

    public boolean hasNode(String nodeName) {
        return nodeName != null && adjacencyList.containsKey(nodeName.trim());
    }

    public Node getNode(String nodeName) {
        if (nodeName == null) return null;
        return nodeMap.get(nodeName.trim());
    }

    public Set<String> getNodeNames() {
        return Collections.unmodifiableSet(adjacencyList.keySet());
    }

    public Map<String, Node> getNodeMap() {
        return Collections.unmodifiableMap(nodeMap);
    }

    public Map<String, List<Edge>> getAdjacencyList() {
        return Collections.unmodifiableMap(adjacencyList);
    }

    public int getNodeCount() {
        return adjacencyList.size();
    }

    public int getEdgeCount() {
        int total = 0;
        for (List<Edge> edges : adjacencyList.values()) {
            total += edges.size();
        }
        return total;
    }

    /**
     * Computes direct geographic distance between two nodes using Haversine formula (in km).
     */
    public double calculateHaversineDistance(String srcName, String destName) {
        Node src = getNode(srcName);
        Node dest = getNode(destName);
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
