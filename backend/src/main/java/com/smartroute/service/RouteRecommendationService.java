package com.smartroute.service;

import com.smartroute.algorithm.*;
import com.smartroute.algorithm.Graph;
import com.smartroute.model.*;
import com.smartroute.repository.LocationRepository;
import com.smartroute.repository.RoadRepository;
import com.smartroute.repository.RouteHistoryRepository;
import com.smartroute.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RouteRecommendationService {

    private final LocationRepository locationRepository;
    private final RoadRepository roadRepository;
    private final RouteHistoryRepository routeHistoryRepository;
    private final UserRepository userRepository;

    public RouteRecommendationService(LocationRepository locationRepository,
                                      RoadRepository roadRepository,
                                      RouteHistoryRepository routeHistoryRepository,
                                      UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.roadRepository = roadRepository;
        this.routeHistoryRepository = routeHistoryRepository;
        this.userRepository = userRepository;
    }

    /**
     * Builds the in-memory Graph from current database locations and roads.
     */
    public Graph buildGraph() {
        Graph graph = new Graph();

        List<LocationEntity> locEntities = locationRepository.findAll();
        for (LocationEntity le : locEntities) {
            graph.addVertex(new Location(le.getId(), le.getName(), le.getLatitude(), le.getLongitude(), le.getState(), le.getDescription()));
        }

        List<RoadEntity> roadEntities = roadRepository.findAll();
        for (RoadEntity re : roadEntities) {
            Road road = new Road(re.getId(), re.getSource(), re.getDestination(), re.getDistance(),
                    re.getTravelTime(), re.getToll(), re.getFuelConsumption(), re.getSafetyScore(),
                    re.getTrafficLevel(), re.getWeatherCondition(), re.getScenicScore(),
                    re.isEvChargingAvailable(), re.isEmergencyLane());
            graph.addEdge(road, true);
        }

        return graph;
    }

    /**
     * Computes the recommended route and comparison alternatives.
     */
    public RouteResponse calculateRoute(RouteRequest request) {
        Graph graph = buildGraph();

        String src = request.getSource() != null ? request.getSource().trim() : "";
        String dest = request.getDestination() != null ? request.getDestination().trim() : "";
        String pref = request.getPreference() != null ? request.getPreference().trim().toUpperCase() : "FASTEST";
        String chosenAlgo = request.getAlgorithm() != null ? request.getAlgorithm().trim().toUpperCase() : "AUTO";

        // Fuzzy match source/destination if exact match is not found
        src = resolveLocationName(src);
        dest = resolveLocationName(dest);

        if (!graph.hasVertex(src) || !graph.hasVertex(dest)) {
            RouteResponse err = new RouteResponse();
            err.setRationale("Error: Source '" + src + "' or Destination '" + dest + "' could not be resolved in the road network.");
            return err;
        }

        RouteResponse response = new RouteResponse();
        response.setPreference(pref);

        // Algorithm Selection Logic
        String algorithmUsed;
        if ("ASTAR".equalsIgnoreCase(chosenAlgo)) {
            algorithmUsed = "A* Search";
            AStarAlgorithm.AStarResult astarRes = AStarAlgorithm.findPath(graph, src, dest, pref);
            populateFromAStar(response, astarRes, graph);
        } else if ("BFS".equalsIgnoreCase(chosenAlgo)) {
            algorithmUsed = "BFS (Breadth First Search)";
            BFS.BFSResult bfsRes = BFS.findPath(graph, src, dest);
            populateFromBFS(response, bfsRes, graph);
        } else if ("DFS".equalsIgnoreCase(chosenAlgo)) {
            algorithmUsed = "DFS (Depth First Search)";
            DFS.DFSResult dfsRes = DFS.findPath(graph, src, dest, 1);
            populateFromDFS(response, dfsRes, graph);
        } else {
            // Default intelligent routing: A* for distance/fastest, Dijkstra for multi-criteria
            if ("SHORTEST".equalsIgnoreCase(pref) || "FASTEST".equalsIgnoreCase(pref)) {
                algorithmUsed = "A* Pathfinding";
                AStarAlgorithm.AStarResult astarRes = AStarAlgorithm.findPath(graph, src, dest, pref);
                populateFromAStar(response, astarRes, graph);
            } else {
                algorithmUsed = "Dijkstra's Algorithm";
                DijkstraAlgorithm.DijkstraResult dijkstraRes = DijkstraAlgorithm.findShortestPath(graph, src, dest, pref);
                populateFromDijkstra(response, dijkstraRes, graph);
            }
        }
        response.setAlgorithm(algorithmUsed);
        response.setRationale(generateRationale(pref, response));

        // Generate Alternative Comparison Routes (Fastest, Shortest, Safest, Fuel Efficient, Lowest Toll, EV Friendly, Scenic, Emergency)
        List<RouteResponse.RouteOption> alternatives = generateAlternatives(graph, src, dest, pref);
        response.setAlternativeRoutes(alternatives);

        // Save to History
        saveHistory(request, response, src, dest, algorithmUsed);

        return response;
    }

    private void populateFromDijkstra(RouteResponse response, DijkstraAlgorithm.DijkstraResult res, Graph graph) {
        response.setRoute(res.getPath());
        response.setDistance(res.getTotalDistance());
        response.setEstimatedTime(res.getTotalTravelTime());
        response.setToll(res.getTotalToll());
        response.setFuelConsumption(res.getTotalFuel());
        response.setSafetyScore(res.getAverageSafetyScore());
        response.setScenicScore(res.getAverageScenicScore());
        response.setEvChargingStops(res.getEvChargingStops());
        response.setTrafficLevel(res.getPrimaryTrafficLevel());
        response.setWeatherCondition(res.getPrimaryWeatherCondition());

        List<Location> coords = new ArrayList<>();
        for (String node : res.getPath()) {
            Location loc = graph.getLocation(node);
            if (loc != null) coords.add(loc);
        }
        response.setRouteCoordinates(coords);
    }

    private void populateFromAStar(RouteResponse response, AStarAlgorithm.AStarResult res, Graph graph) {
        response.setRoute(res.getPath());
        response.setDistance(res.getTotalDistance());
        response.setEstimatedTime(res.getTotalTravelTime());
        response.setToll(res.getTotalToll());
        response.setFuelConsumption(res.getTotalFuel());
        response.setSafetyScore(res.getAverageSafetyScore());
        response.setScenicScore(res.getAverageScenicScore());
        response.setEvChargingStops(res.getEvChargingStops());
        response.setTrafficLevel(res.getPrimaryTrafficLevel());
        response.setWeatherCondition(res.getPrimaryWeatherCondition());

        List<Location> coords = new ArrayList<>();
        for (String node : res.getPath()) {
            Location loc = graph.getLocation(node);
            if (loc != null) coords.add(loc);
        }
        response.setRouteCoordinates(coords);
    }

    private void populateFromBFS(RouteResponse response, BFS.BFSResult res, Graph graph) {
        response.setRoute(res.getPath());
        double dist = 0, time = 0, toll = 0, fuel = 0, safety = 0;
        int evStops = 0;
        for (Road r : res.getRoadSegments()) {
            dist += r.getDistance();
            time += (r.getTravelTime() * r.getTrafficMultiplier());
            toll += r.getToll();
            fuel += r.getFuelConsumption();
            safety += r.getSafetyScore();
            if (r.isEvChargingAvailable()) evStops++;
        }
        int count = Math.max(1, res.getRoadSegments().size());
        response.setDistance(Math.round(dist * 10.0) / 10.0);
        response.setEstimatedTime(Math.round(time * 10.0) / 10.0);
        response.setToll(Math.round(toll * 10.0) / 10.0);
        response.setFuelConsumption(Math.round(fuel * 10.0) / 10.0);
        response.setSafetyScore(Math.round((safety / count) * 10.0) / 10.0);
        response.setEvChargingStops(evStops);

        List<Location> coords = new ArrayList<>();
        for (String node : res.getPath()) {
            Location loc = graph.getLocation(node);
            if (loc != null) coords.add(loc);
        }
        response.setRouteCoordinates(coords);
    }

    private void populateFromDFS(RouteResponse response, DFS.DFSResult res, Graph graph) {
        response.setRoute(res.getPath());
        double dist = 0, time = 0, toll = 0, fuel = 0, safety = 0;
        int evStops = 0;

        List<Road> segments = new ArrayList<>();
        for (int i = 0; i < res.getPath().size() - 1; i++) {
            String u = res.getPath().get(i);
            String v = res.getPath().get(i + 1);
            for (Road r : graph.getNeighbors(u)) {
                if (r.getDestination().equalsIgnoreCase(v)) {
                    segments.add(r);
                    dist += r.getDistance();
                    time += r.getTravelTime();
                    toll += r.getToll();
                    fuel += r.getFuelConsumption();
                    safety += r.getSafetyScore();
                    if (r.isEvChargingAvailable()) evStops++;
                    break;
                }
            }
        }

        int count = Math.max(1, segments.size());
        response.setDistance(Math.round(dist * 10.0) / 10.0);
        response.setEstimatedTime(Math.round(time * 10.0) / 10.0);
        response.setToll(Math.round(toll * 10.0) / 10.0);
        response.setFuelConsumption(Math.round(fuel * 10.0) / 10.0);
        response.setSafetyScore(Math.round((safety / count) * 10.0) / 10.0);
        response.setEvChargingStops(evStops);

        List<Location> coords = new ArrayList<>();
        for (String node : res.getPath()) {
            Location loc = graph.getLocation(node);
            if (loc != null) coords.add(loc);
        }
        response.setRouteCoordinates(coords);
    }

    private List<RouteResponse.RouteOption> generateAlternatives(Graph graph, String src, String dest, String selectedPref) {
        String[] prefsToEvaluate = {"FASTEST", "SHORTEST", "SAFEST", "FUEL_EFFICIENT", "LOWEST_TOLL", "EV_FRIENDLY", "SCENIC", "EMERGENCY"};
        Map<String, String> prefLabels = Map.of(
            "FASTEST", "Route A – Fastest",
            "SHORTEST", "Route B – Shortest",
            "SAFEST", "Route C – Safest",
            "FUEL_EFFICIENT", "Route D – Fuel Efficient",
            "LOWEST_TOLL", "Route E – Lowest Toll",
            "EV_FRIENDLY", "Route F – EV Friendly",
            "SCENIC", "Route G – Scenic",
            "EMERGENCY", "Route H – Emergency"
        );

        List<RouteResponse.RouteOption> options = new ArrayList<>();

        for (String p : prefsToEvaluate) {
            DijkstraAlgorithm.DijkstraResult res = DijkstraAlgorithm.findShortestPath(graph, src, dest, p);
            if (res.isPathFound() && !res.getPath().isEmpty()) {
                String tag = p.equalsIgnoreCase(selectedPref) ? "SELECTED PREFERENCE" : "ALTERNATIVE";
                options.add(new RouteResponse.RouteOption(
                    prefLabels.getOrDefault(p, "Route – " + p),
                    p,
                    "Dijkstra",
                    res.getPath(),
                    res.getTotalDistance(),
                    res.getTotalTravelTime(),
                    res.getTotalToll(),
                    res.getTotalFuel(),
                    res.getAverageSafetyScore(),
                    res.getEvChargingStops(),
                    tag
                ));
            }
        }
        return options;
    }

    private String generateRationale(String pref, RouteResponse res) {
        switch (pref.toUpperCase()) {
            case "FASTEST":
                return "Optimized to minimize total travel time (" + res.getEstimatedTime() + " mins) incorporating real-time highway speeds and traffic congestion flow.";
            case "SHORTEST":
                return "Calculated to strictly minimize geographical road distance (" + res.getDistance() + " km) using direct connected road segments.";
            case "SAFEST":
                return "Prioritizes high-rated multi-lane expressways with safety score of " + res.getSafetyScore() + "/10 and emergency lane provisions.";
            case "FUEL_EFFICIENT":
                return "Configured to minimize diesel/petrol burn (" + res.getFuelConsumption() + " L) by avoiding severe stop-and-go bottlenecks.";
            case "LOWEST_TOLL":
                return "Optimized to minimize toll tax expenditure (₹" + res.getToll() + ") across state and national toll plazas.";
            case "EV_FRIENDLY":
                return "Routes through fast EV charging plazas (" + res.getEvChargingStops() + " charging stations available on this path) for uninterrupted EV travel.";
            case "TRAFFIC_AWARE":
                return "Dynamically penalizes heavily congested arterial corridors and routes through free-flowing bypass links.";
            case "WEATHER_AWARE":
                return "Avoids roads affected by intense rainstorms and heavy fog for maximum driving clarity.";
            case "SCENIC":
                return "Selects visually scenic, green corridor routes with high scenic index rating.";
            case "EMERGENCY":
                return "Prioritizes immediate clearance, wide emergency lanes, and lowest congestion for ambulance/first-responder dispatch.";
            default:
                return "Optimized route computed according to selected multi-criteria parameters.";
        }
    }

    private String resolveLocationName(String query) {
        if (query == null || query.trim().isEmpty()) return "";
        Optional<LocationEntity> exact = locationRepository.findByNameIgnoreCase(query.trim());
        if (exact.isPresent()) {
            return exact.get().getName();
        }
        // Fallback to Edit Distance suggestion
        List<String> allNames = locationRepository.findAll().stream().map(LocationEntity::getName).collect(Collectors.toList());
        List<EditDistance.SuggestionResult> suggestions = EditDistance.findRankedSuggestions(query, allNames, 1);
        if (!suggestions.isEmpty()) {
            return suggestions.get(0).getLocation();
        }
        return query.trim();
    }

    private void saveHistory(RouteRequest req, RouteResponse res, String src, String dest, String algo) {
        try {
            String userName = "Guest User";
            Long userId = req.getUserId();
            if (userId != null) {
                Optional<User> u = userRepository.findById(userId);
                if (u.isPresent()) userName = u.get().getName();
            }
            String pathJson = res.getRoute() != null ? String.join(" → ", res.getRoute()) : "";
            RouteHistoryEntity entity = new RouteHistoryEntity(
                    userId, userName, src, dest, res.getPreference(), algo,
                    res.getDistance(), res.getEstimatedTime(), res.getToll(),
                    res.getFuelConsumption(), res.getSafetyScore(), res.getEvChargingStops(),
                    pathJson
            );
            routeHistoryRepository.save(entity);
        } catch (Exception e) {
            // Non-critical logging
            System.err.println("Failed to record history: " + e.getMessage());
        }
    }
}
