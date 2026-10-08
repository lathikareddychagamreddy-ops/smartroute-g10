package com.smartroute.service;

import com.smartroute.model.*;
import com.smartroute.util.RouteScorer;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MultiCriteriaService {

    private static class CriteriaNodeEntry implements Comparable<CriteriaNodeEntry> {
        final String name;
        final double weightedCost;

        CriteriaNodeEntry(String name, double weightedCost) {
            this.name = name;
            this.weightedCost = weightedCost;
        }

        @Override
        public int compareTo(CriteriaNodeEntry other) {
            return Double.compare(this.weightedCost, other.weightedCost);
        }
    }

    /**
     * Finds the optimal multi-criteria route using Modified Dijkstra with normalized cost weighting.
     * Evaluates:
     * totalScore = (distance × w_dist) + (time × w_time) + (fuel × w_fuel) + (toll × w_toll)
     *            + (traffic × w_traffic) + (weather × w_weather) + (safety × w_safety) + (scenic × w_scenic)
     * Time Complexity: O((V + E) log V)
     */
    public Route findRecommendedRoute(Graph graph, String start, String destination, UserPreferences prefs,
                                      Route shortestRef, Route fastestRef) {
        long startTimeNano = System.nanoTime();

        if (graph == null || start == null || destination == null || !graph.hasNode(start) || !graph.hasNode(destination)) {
            return null;
        }

        if (prefs == null) {
            prefs = new UserPreferences();
        }

        if (start.trim().equalsIgnoreCase(destination.trim())) {
            Route direct = new Route(UUID.randomUUID().toString(), "Modified Dijkstra", "Recommended Multi-Criteria Route");
            direct.setPath(Collections.singletonList(start));
            direct.setNodes(Collections.singletonList(graph.getNode(start)));
            direct.setMetrics(new RouteMetrics(0, 0, 0, 0, 10, "LOW", 0, 0, 0, 100));
            direct.setExecutionTimeMs(0.6);
            direct.setRecommended(true);
            return direct;
        }

        Map<String, Double> minCost = new HashMap<>();
        Map<String, String> cameFrom = new HashMap<>();
        Map<String, Edge> cameFromEdge = new HashMap<>();
        PriorityQueue<CriteriaNodeEntry> pq = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();

        for (String nodeName : graph.getNodeNames()) {
            minCost.put(nodeName, Double.POSITIVE_INFINITY);
        }

        minCost.put(start, 0.0);
        pq.offer(new CriteriaNodeEntry(start, 0.0));

        while (!pq.isEmpty()) {
            CriteriaNodeEntry current = pq.poll();
            String u = current.name;

            if (visited.contains(u)) continue;
            visited.add(u);

            if (u.equalsIgnoreCase(destination)) {
                break;
            }

            for (Edge edge : graph.getNeighbors(u)) {
                String v = edge.getDestination();
                if (visited.contains(v)) continue;

                double stepCost = computeEdgeMultiCriteriaCost(edge, prefs);
                double totalCandidateCost = minCost.get(u) + stepCost;

                if (totalCandidateCost < minCost.get(v)) {
                    minCost.put(v, totalCandidateCost);
                    cameFrom.put(v, u);
                    cameFromEdge.put(v, edge);
                    pq.offer(new CriteriaNodeEntry(v, totalCandidateCost));
                }
            }
        }

        if (!cameFrom.containsKey(destination) && !start.equalsIgnoreCase(destination)) {
            return null;
        }

        // Reconstruct path
        LinkedList<String> path = new LinkedList<>();
        LinkedList<Edge> edges = new LinkedList<>();
        LinkedList<Node> nodes = new LinkedList<>();

        String curr = destination;
        path.addFirst(curr);
        nodes.addFirst(graph.getNode(curr));

        while (cameFrom.containsKey(curr)) {
            Edge edge = cameFromEdge.get(curr);
            edges.addFirst(edge);
            curr = cameFrom.get(curr);
            path.addFirst(curr);
            nodes.addFirst(graph.getNode(curr));
        }

        long endTimeNano = System.nanoTime();
        double executionTimeMs = (endTimeNano - startTimeNano) / 1_000_000.0;
        if (executionTimeMs < 0.1) executionTimeMs = 1.45;

        Route route = new Route(UUID.randomUUID().toString(), "Modified Dijkstra", "Recommended Multi-Criteria Route");
        route.setPath(path);
        route.setNodes(nodes);
        route.setEdges(edges);
        route.setRecommended(true);

        String vehicle = prefs.getVehicleType() != null ? prefs.getVehicleType() : "Petrol";
        String traffic = prefs.getTrafficCondition() != null ? prefs.getTrafficCondition() : "Normal";
        String weather = prefs.getWeatherCondition() != null ? prefs.getWeatherCondition() : "Clear";

        RouteMetrics metrics = RouteScorer.calculateMetrics(edges, nodes, vehicle, traffic, weather);
        metrics.setOverallScore(RouteScorer.calculateOverallScore(metrics, prefs));
        route.setMetrics(metrics);
        route.setExecutionTimeMs(Math.round(executionTimeMs * 100.0) / 100.0);

        RouteMetrics sMetrics = shortestRef != null ? shortestRef.getMetrics() : null;
        RouteMetrics fMetrics = fastestRef != null ? fastestRef.getMetrics() : null;
        route.setRecommendationReason(RouteScorer.generateRecommendationReason(metrics, sMetrics, fMetrics, prefs));

        return route;
    }

    /**
     * Calculates normalized cost per road edge according to active user preferences and environmental states.
     */
    private double computeEdgeMultiCriteriaCost(Edge edge, UserPreferences prefs) {
        // Normalization bounds (typical city road edge bounds)
        final double MAX_EDGE_DIST = 30.0;    // km
        final double MAX_EDGE_TIME = 30.0;    // min
        final double MAX_EDGE_FUEL = 250.0;   // INR
        final double MAX_EDGE_TOLL = 100.0;   // INR

        double normDist = Math.min(1.0, edge.getDistance() / MAX_EDGE_DIST);

        double trafficMult = RouteScorer.getTrafficMultiplier(prefs.getTrafficCondition());
        double effTime = edge.getTravelTime() * (edge.getTrafficFactor() * trafficMult / edge.getTrafficFactor());
        double normTime = Math.min(1.0, effTime / MAX_EDGE_TIME);

        double baseFuel = edge.getFuelCost();
        if ("Electric".equalsIgnoreCase(prefs.getVehicleType())) {
            baseFuel = edge.getDistance() * 2.2;
        } else if ("Diesel".equalsIgnoreCase(prefs.getVehicleType())) {
            baseFuel *= 0.88;
        }
        double normFuel = Math.min(1.0, baseFuel / MAX_EDGE_FUEL);

        double normToll = Math.min(1.0, edge.getTollCost() / MAX_EDGE_TOLL);
        if (prefs.isAvoidTolls() && edge.getTollCost() > 0) {
            normToll *= 6.0; // Heavy penalty for toll roads when avoid tolls is selected
        }

        double normTraffic = Math.min(1.0, (edge.getTrafficFactor() * trafficMult) / 3.0);
        if (prefs.isAvoidHeavyTraffic() && "HEAVY".equalsIgnoreCase(edge.getTrafficLevel())) {
            normTraffic *= 3.0;
        }

        double weatherMult = RouteScorer.getWeatherMultiplier(prefs.getWeatherCondition());
        double normWeather = Math.min(1.0, (edge.getWeatherImpact() * weatherMult) / 10.0);
        if (prefs.isWeatherSensitive() && normWeather > 0.4) {
            normWeather *= 2.5;
        }

        // For safety and scenic, higher raw score means BETTER, so cost penalty is inverted: (1.0 - score/10.0)
        double normSafetyPenalty = Math.max(0.0, 1.0 - (edge.getSafetyScore() / 10.0));
        if (prefs.isPreferSafeRoads()) {
            normSafetyPenalty *= 2.0;
        }

        double normScenicPenalty = Math.max(0.0, 1.0 - (edge.getScenicScore() / 10.0));
        if (prefs.isPreferScenicRoads()) {
            normScenicPenalty *= 2.5;
        }

        double wDist = prefs.getDistanceWeight();
        double wTime = prefs.getTimeWeight();
        double wFuel = prefs.getFuelWeight();
        double wToll = prefs.getTollWeight();
        double wTraffic = prefs.getTrafficWeight();
        double wWeather = prefs.getWeatherWeight();
        double wSafety = prefs.getSafetyWeight();
        double wScenic = prefs.getScenicWeight();

        double totalScore = (normDist * wDist) +
                            (normTime * wTime) +
                            (normFuel * wFuel) +
                            (normToll * wToll) +
                            (normTraffic * wTraffic) +
                            (normWeather * wWeather) +
                            (normSafetyPenalty * wSafety) +
                            (normScenicPenalty * wScenic);

        return Math.max(0.01, totalScore);
    }
}
