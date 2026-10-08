package com.smartroute.algorithm;

import java.util.*;

/**
 * AStarAlgorithm - A* Pathfinding Algorithm Implementation
 * Uses admissible heuristic h(n) (straight-line Haversine distance to destination)
 * with f(n) = g(n) + h(n) for guided shortest-path search.
 */
public class AStarAlgorithm {

    public static class AStarResult {
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
        private final List<AStarStep> steps;
        private final boolean pathFound;

        public AStarResult(List<String> path, List<Road> roadSegments, double totalCost,
                           double totalDistance, double totalTravelTime, double totalToll,
                           double totalFuel, double averageSafetyScore, double averageScenicScore,
                           int evChargingStops, String primaryTrafficLevel, String primaryWeatherCondition,
                           List<AStarStep> steps, boolean pathFound) {
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
        public List<AStarStep> getSteps() { return steps; }
        public boolean isPathFound() { return pathFound; }
    }

    public static class AStarStep {
        private final int stepNumber;
        private final String vertex;
        private final double gScore;
        private final double hScore;
        private final double fScore;
        private final String action;

        public AStarStep(int stepNumber, String vertex, double gScore, double hScore, double fScore, String action) {
            this.stepNumber = stepNumber;
            this.vertex = vertex;
            this.gScore = Math.round(gScore * 100.0) / 100.0;
            this.hScore = Math.round(hScore * 100.0) / 100.0;
            this.fScore = Math.round(fScore * 100.0) / 100.0;
            this.action = action;
        }

        public int getStepNumber() { return stepNumber; }
        public String getVertex() { return vertex; }
        public double getGScore() { return gScore; }
        public double getHScore() { return hScore; }
        public double getFScore() { return fScore; }
        public String getAction() { return action; }
    }

    private static class AStarNode implements Comparable<AStarNode> {
        final String name;
        final double fScore;

        AStarNode(String name, double fScore) {
            this.name = name;
            this.fScore = fScore;
        }

        @Override
        public int compareTo(AStarNode o) {
            return Double.compare(this.fScore, o.fScore);
        }
    }

    /**
     * Executes A* algorithm from source to destination.
     * Time Complexity: O(E) in best case with good heuristic, bounded by O((V + E) log V)
     */
    public static AStarResult findPath(Graph graph, String source, String destination, String preference) {
        List<AStarStep> steps = new ArrayList<>();

        if (graph == null || !graph.hasVertex(source) || !graph.hasVertex(destination)) {
            return new AStarResult(Collections.emptyList(), Collections.emptyList(), 0, 0, 0, 0, 0, 0, 0, 0, "LOW", "CLEAR", steps, false);
        }

        Map<String, Double> gScores = new HashMap<>();
        Map<String, Double> fScores = new HashMap<>();
        Map<String, String> cameFrom = new HashMap<>();
        Map<String, Road> cameFromRoad = new HashMap<>();
        Set<String> closedSet = new HashSet<>();
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>();

        for (String v : graph.getVertices()) {
            gScores.put(v, Double.POSITIVE_INFINITY);
            fScores.put(v, Double.POSITIVE_INFINITY);
        }

        gScores.put(source, 0.0);
        double initialH = graph.calculateHaversineDistance(source, destination);
        fScores.put(source, initialH);
        openSet.add(new AStarNode(source, initialH));

        int stepCounter = 1;
        steps.add(new AStarStep(stepCounter++, source, 0.0, initialH, initialH,
                "Initialized A* search from source '" + source + "' towards target '" + destination + "' (Heuristic h: " + String.format("%.2f", initialH) + " km)"));

        boolean reached = false;

        while (!openSet.isEmpty()) {
            AStarNode current = openSet.poll();
            String u = current.name;

            if (closedSet.contains(u)) continue;
            closedSet.add(u);

            double currentG = gScores.get(u);
            double currentH = graph.calculateHaversineDistance(u, destination);

            steps.add(new AStarStep(stepCounter++, u, currentG, currentH, currentG + currentH,
                    "Expanding node '" + u + "' with f(n) = g(" + String.format("%.1f", currentG) + ") + h(" + String.format("%.1f", currentH) + ") = " + String.format("%.1f", currentG + currentH)));

            if (u.equalsIgnoreCase(destination)) {
                reached = true;
                steps.add(new AStarStep(stepCounter++, u, currentG, 0.0, currentG,
                        "Target destination '" + destination + "' reached by A*!"));
                break;
            }

            for (Road road : graph.getNeighbors(u)) {
                String neighbor = road.getDestination();
                if (closedSet.contains(neighbor)) continue;

                double edgeCost = road.calculateCostForPreference(preference);
                double tentativeG = currentG + edgeCost;

                if (tentativeG < gScores.get(neighbor)) {
                    cameFrom.put(neighbor, u);
                    cameFromRoad.put(neighbor, road);
                    gScores.put(neighbor, tentativeG);

                    double h = graph.calculateHaversineDistance(neighbor, destination);
                    double f = tentativeG + h;
                    fScores.put(neighbor, f);

                    openSet.add(new AStarNode(neighbor, f));

                    steps.add(new AStarStep(stepCounter++, neighbor, tentativeG, h, f,
                            "Evaluated neighbor '" + neighbor + "': edge weight " + String.format("%.2f", edgeCost) +
                            ", g(n)=" + String.format("%.1f", tentativeG) + ", h(n)=" + String.format("%.1f", h) + ", f(n)=" + String.format("%.1f", f)));
                }
            }
        }

        if (!reached) {
            return new AStarResult(Collections.emptyList(), Collections.emptyList(), 0, 0, 0, 0, 0, 0, 0, 0, "LOW", "CLEAR", steps, false);
        }

        // Reconstruct path
        List<String> path = new ArrayList<>();
        List<Road> roadSegments = new ArrayList<>();
        String curr = destination;
        while (curr != null) {
            path.add(0, curr);
            Road road = cameFromRoad.get(curr);
            if (road != null) {
                roadSegments.add(0, road);
            }
            curr = cameFrom.get(curr);
        }

        // Accumulate statistics
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

        return new AStarResult(path, roadSegments, gScores.get(destination), totalDist, totalTime,
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
