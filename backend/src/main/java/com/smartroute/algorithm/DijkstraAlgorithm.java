package com.smartroute.algorithm;

import java.util.*;

/**
 * DijkstraAlgorithm - Dijkstra's Shortest Path Algorithm Implementation
 * Uses a PriorityQueue (Min-Heap) to compute optimal paths for arbitrary cost weightings.
 * Computes exact cumulative metrics: distance, time, toll, fuel, safety score, EV availability, etc.
 */
public class DijkstraAlgorithm {

    public static class DijkstraResult {
        private final List<String> path;
        private final List<Road> roadSegments;
        private final double totalCost;
        private final double totalDistance;
        private final double totalTravelTime;
        private final double totalToll;
        private final double totalFuel;
        private final double averageSafetyScore;
        private final double averageScenicScore;
        private final int evChargingStops;
        private final String primaryTrafficLevel;
        private final String primaryWeatherCondition;
        private final List<DijkstraStep> steps;
        private final boolean pathFound;

        public DijkstraResult(List<String> path, List<Road> roadSegments, double totalCost,
                              double totalDistance, double totalTravelTime, double totalToll,
                              double totalFuel, double averageSafetyScore, double averageScenicScore,
                              int evChargingStops, String primaryTrafficLevel, String primaryWeatherCondition,
                              List<DijkstraStep> steps, boolean pathFound) {
            this.path = path;
            this.roadSegments = roadSegments;
            this.totalCost = totalCost;
            this.totalDistance = Math.round(totalDistance * 10.0) / 10.0;
            this.totalTravelTime = Math.round(totalTravelTime * 10.0) / 10.0;
            this.totalToll = Math.round(totalToll * 10.0) / 10.0;
            this.totalFuel = Math.round(totalFuel * 10.0) / 10.0;
            this.averageSafetyScore = Math.round(averageSafetyScore * 10.0) / 10.0;
            this.averageScenicScore = Math.round(averageScenicScore * 10.0) / 10.0;
            this.evChargingStops = evChargingStops;
            this.primaryTrafficLevel = primaryTrafficLevel;
            this.primaryWeatherCondition = primaryWeatherCondition;
            this.steps = steps;
            this.pathFound = pathFound;
        }

        public List<String> getPath() { return path; }
        public List<Road> getRoadSegments() { return roadSegments; }
        public double getTotalCost() { return totalCost; }
        public double getTotalDistance() { return totalDistance; }
        public double getTotalTravelTime() { return totalTravelTime; }
        public double getTotalToll() { return totalToll; }
        public double getTotalFuel() { return totalFuel; }
        public double getAverageSafetyScore() { return averageSafetyScore; }
        public double getAverageScenicScore() { return averageScenicScore; }
        public int getEvChargingStops() { return evChargingStops; }
        public String getPrimaryTrafficLevel() { return primaryTrafficLevel; }
        public String getPrimaryWeatherCondition() { return primaryWeatherCondition; }
        public List<DijkstraStep> getSteps() { return steps; }
        public boolean isPathFound() { return pathFound; }
    }

    public static class DijkstraStep {
        private final int stepNumber;
        private final String vertex;
        private final double currentCost;
        private final String action;

        public DijkstraStep(int stepNumber, String vertex, double currentCost, String action) {
            this.stepNumber = stepNumber;
            this.vertex = vertex;
            this.currentCost = Math.round(currentCost * 100.0) / 100.0;
            this.action = action;
        }

        public int getStepNumber() { return stepNumber; }
        public String getVertex() { return vertex; }
        public double getCurrentCost() { return currentCost; }
        public String getAction() { return action; }
    }

    private static class NodeEntry implements Comparable<NodeEntry> {
        final String name;
        final double cost;

        NodeEntry(String name, double cost) {
            this.name = name;
            this.cost = cost;
        }

        @Override
        public int compareTo(NodeEntry o) {
            return Double.compare(this.cost, o.cost);
        }
    }

    /**
     * Executes Dijkstra's algorithm from source to destination for a chosen routing preference.
     * Time Complexity: O((V + E) log V)
     * Space Complexity: O(V)
     */
    public static DijkstraResult findShortestPath(Graph graph, String source, String destination, String preference) {
        List<DijkstraStep> steps = new ArrayList<>();

        if (graph == null || !graph.hasVertex(source) || !graph.hasVertex(destination)) {
            return new DijkstraResult(Collections.emptyList(), Collections.emptyList(), 0, 0, 0, 0, 0, 0, 0, 0, "LOW", "CLEAR", steps, false);
        }

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();
        Map<String, Road> predecessorRoads = new HashMap<>();
        Set<String> settled = new HashSet<>();
        PriorityQueue<NodeEntry> pq = new PriorityQueue<>();

        for (String vertex : graph.getVertices()) {
            distances.put(vertex, Double.POSITIVE_INFINITY);
        }

        distances.put(source, 0.0);
        pq.add(new NodeEntry(source, 0.0));

        int stepCounter = 1;
        steps.add(new DijkstraStep(stepCounter++, source, 0.0,
                "Initialized Dijkstra with source node '" + source + "' (Cost: 0.0, Preference: " + preference + ")"));

        boolean reached = false;

        while (!pq.isEmpty()) {
            NodeEntry current = pq.poll();
            String u = current.name;

            if (settled.contains(u)) continue;
            settled.add(u);

            steps.add(new DijkstraStep(stepCounter++, u, current.cost,
                    "Selected vertex '" + u + "' with lowest accumulated cost: " + String.format("%.2f", current.cost)));

            if (u.equalsIgnoreCase(destination)) {
                reached = true;
                steps.add(new DijkstraStep(stepCounter++, u, current.cost,
                        "Target destination '" + destination + "' reached with optimal cost!"));
                break;
            }

            for (Road road : graph.getNeighbors(u)) {
                String v = road.getDestination();
                if (settled.contains(v)) continue;

                double edgeWeight = road.calculateCostForPreference(preference);
                double newDist = distances.get(u) + edgeWeight;

                if (newDist < distances.get(v)) {
                    distances.put(v, newDist);
                    predecessors.put(v, u);
                    predecessorRoads.put(v, road);
                    pq.add(new NodeEntry(v, newDist));

                    steps.add(new DijkstraStep(stepCounter++, v, newDist,
                            "Relaxed edge (" + u + " -> " + v + ") with edge weight " + String.format("%.2f", edgeWeight) +
                            ", new path cost: " + String.format("%.2f", newDist)));
                }
            }
        }

        if (!reached || distances.get(destination) == Double.POSITIVE_INFINITY) {
            return new DijkstraResult(Collections.emptyList(), Collections.emptyList(), 0, 0, 0, 0, 0, 0, 0, 0, "LOW", "CLEAR", steps, false);
        }

        // Reconstruct path
        List<String> path = new ArrayList<>();
        List<Road> roadSegments = new ArrayList<>();
        String curr = destination;
        while (curr != null) {
            path.add(0, curr);
            Road road = predecessorRoads.get(curr);
            if (road != null) {
                roadSegments.add(0, road);
            }
            curr = predecessors.get(curr);
        }

        // Compute accumulated statistics
        double totalDist = 0;
        double totalTime = 0;
        double totalToll = 0;
        double totalFuel = 0;
        double totalSafety = 0;
        double totalScenic = 0;
        int evStops = 0;
        Map<String, Integer> trafficCount = new HashMap<>();
        Map<String, Integer> weatherCount = new HashMap<>();

        for (Road r : roadSegments) {
            totalDist += r.getDistance();
            totalTime += (r.getTravelTime() * r.getTrafficMultiplier() * r.getWeatherMultiplier());
            totalToll += r.getToll();
            totalFuel += r.getFuelConsumption();
            totalSafety += r.getSafetyScore();
            totalScenic += r.getScenicScore();
            if (r.isEvChargingAvailable()) evStops++;

            String traf = r.getTrafficLevel() != null ? r.getTrafficLevel() : "LOW";
            trafficCount.put(traf, trafficCount.getOrDefault(traf, 0) + 1);

            String w = r.getWeatherCondition() != null ? r.getWeatherCondition() : "CLEAR";
            weatherCount.put(w, weatherCount.getOrDefault(w, 0) + 1);
        }

        int count = Math.max(1, roadSegments.size());
        double avgSafety = totalSafety / count;
        double avgScenic = totalScenic / count;

        String dominantTraffic = getDominantCategory(trafficCount, "LOW");
        String dominantWeather = getDominantCategory(weatherCount, "CLEAR");

        return new DijkstraResult(path, roadSegments, distances.get(destination), totalDist, totalTime,
                totalToll, totalFuel, avgSafety, avgScenic, evStops, dominantTraffic, dominantWeather, steps, true);
    }

    private static String getDominantCategory(Map<String, Integer> countMap, String defaultVal) {
        String dominant = defaultVal;
        int max = -1;
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                dominant = entry.getKey();
            }
        }
        return dominant;
    }
}
