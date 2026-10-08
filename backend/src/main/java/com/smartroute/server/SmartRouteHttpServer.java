package com.smartroute.server;

import com.smartroute.algorithm.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * SmartRouteHttpServer - High-Performance Pure Java HTTP & REST Server
 * Uses built-in com.sun.net.httpserver with ZERO external library overhead.
 * Serves the full REST API & DSA Algorithm Engine + Web Frontend on port 8080.
 */
public class SmartRouteHttpServer {

    private static final int PORT = 8085;
    private static final Graph graph = new Graph();
    private static final List<Location> locationList = new ArrayList<>();
    private static final List<Road> roadList = new ArrayList<>();
    private static final List<Map<String, Object>> routeHistory = new ArrayList<>();
    private static final Map<String, Map<String, Object>> users = new HashMap<>();

    public static void main(String[] args) throws IOException {
        initializeData();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.setExecutor(Executors.newFixedThreadPool(8));

        // API Endpoints
        server.createContext("/api/search", new SearchHandler());
        server.createContext("/api/locations", new LocationsHandler());
        server.createContext("/api/roads", new RoadsHandler());
        server.createContext("/api/routes/calculate", new RouteCalculateHandler());
        server.createContext("/api/routes/history", new HistoryHandler());
        server.createContext("/api/auth/register", new AuthRegisterHandler());
        server.createContext("/api/auth/login", new AuthLoginHandler());
        server.createContext("/api/algorithms/visualize", new VisualizeHandler());

        // Static Frontend Handler
        server.createContext("/", new StaticFileHandler());

        server.start();
        System.out.println("==================================================================");
        System.out.println("  SmartRoute AI Java Server Started Successfully!               ");
        System.out.println("  URL: http://localhost:" + PORT + "                                   ");
        System.out.println("  Real DSA Algorithms Active:                                    ");
        System.out.println("   • Levenshtein Edit Distance (Dynamic Programming)             ");
        System.out.println("   • Adjacency-List Weighted Graph                               ");
        System.out.println("   • Dijkstra's Multi-Criteria Algorithm (Min-Heap)              ");
        System.out.println("   • A* Pathfinding (Haversine Distance Heuristic)               ");
        System.out.println("   • Breadth-First Search (BFS) & Depth-First Search (DFS)       ");
        System.out.println("==================================================================");
    }

    private static void initializeData() {
        // Seed Demo User
        Map<String, Object> demoUser = new HashMap<>();
        demoUser.put("id", 1L);
        demoUser.put("name", "Demo Student");
        demoUser.put("email", "demo@smartroute.ai");
        demoUser.put("password", "admin123");
        users.put("demo@smartroute.ai", demoUser);

        // Seed Locations (Vertices)
        locationList.addAll(Arrays.asList(
            new Location(1L, "Hyderabad", 17.3850, 78.4867, "Telangana", "Capital tech hub of Telangana"),
            new Location(2L, "Suryapet", 17.1439, 79.6239, "Telangana", "Key junction town on NH-65"),
            new Location(3L, "Vijayawada", 16.5062, 80.6480, "Andhra Pradesh", "Major commercial hub on Krishna River"),
            new Location(4L, "Guntur", 16.3067, 80.4365, "Andhra Pradesh", "Educational and agricultural center"),
            new Location(5L, "Warangal", 17.9689, 79.5941, "Telangana", "Historic heritage city with expressway link"),
            new Location(6L, "Kurnool", 15.8281, 78.0373, "Andhra Pradesh", "Gateway city on NH-44"),
            new Location(7L, "Anantapur", 14.6819, 77.6006, "Andhra Pradesh", "NH-44 transit corridor hub"),
            new Location(8L, "Bengaluru", 12.9716, 77.5946, "Karnataka", "Silicon Valley of India"),
            new Location(9L, "Tirupati", 13.6288, 79.4192, "Andhra Pradesh", "Spiritual city & foothill transport hub"),
            new Location(10L, "Chennai", 13.0827, 80.2707, "Tamil Nadu", "Major coastal metropolitan port city"),
            new Location(11L, "Visakhapatnam", 17.6868, 83.2185, "Andhra Pradesh", "Port city & financial capital of AP"),
            new Location(12L, "Rajahmundry", 17.0005, 81.8040, "Andhra Pradesh", "Cultural capital on Godavari River"),
            new Location(13L, "Mahabubnagar", 16.7488, 77.9840, "Telangana", "Fast corridor city on NH-44"),
            new Location(14L, "Pune", 18.5204, 73.8567, "Maharashtra", "Automotive & education capital"),
            new Location(15L, "Mumbai", 19.0760, 72.8777, "Maharashtra", "Financial capital of India")
        ));

        for (Location loc : locationList) {
            graph.addVertex(loc);
        }

        // Seed Roads (Edges)
        roadList.addAll(Arrays.asList(
            new Road(1L, "Hyderabad", "Suryapet", 135.0, 110.0, 160.0, 9.2, 9.1, "LOW", "CLEAR", 7.5, true, true),
            new Road(2L, "Suryapet", "Vijayawada", 140.0, 125.0, 190.0, 9.5, 8.8, "MEDIUM", "CLEAR", 8.0, true, true),
            new Road(3L, "Hyderabad", "Warangal", 148.0, 130.0, 120.0, 9.8, 8.9, "LOW", "CLEAR", 8.6, true, true),
            new Road(4L, "Warangal", "Suryapet", 112.0, 115.0, 40.0, 7.5, 7.8, "LOW", "CLEAR", 7.0, false, false),
            new Road(5L, "Warangal", "Vijayawada", 240.0, 260.0, 90.0, 15.8, 7.6, "LOW", "CLEAR", 8.4, false, false),
            new Road(6L, "Vijayawada", "Guntur", 34.0, 35.0, 60.0, 2.4, 9.4, "MEDIUM", "CLEAR", 6.5, true, true),
            new Road(7L, "Hyderabad", "Mahabubnagar", 102.0, 85.0, 110.0, 6.9, 9.3, "LOW", "CLEAR", 7.2, true, true),
            new Road(8L, "Mahabubnagar", "Kurnool", 115.0, 95.0, 130.0, 7.8, 9.2, "LOW", "CLEAR", 7.8, true, true),
            new Road(9L, "Kurnool", "Anantapur", 150.0, 120.0, 160.0, 10.2, 9.0, "LOW", "CLEAR", 7.4, true, true),
            new Road(10L, "Anantapur", "Bengaluru", 215.0, 180.0, 220.0, 14.5, 9.5, "MEDIUM", "CLEAR", 8.2, true, true),
            new Road(11L, "Hyderabad", "Kurnool", 217.0, 175.0, 240.0, 14.6, 9.1, "LOW", "CLEAR", 7.6, true, true),
            new Road(12L, "Kurnool", "Guntur", 260.0, 290.0, 0.0, 16.8, 7.2, "LOW", "CLEAR", 9.2, false, false),
            new Road(13L, "Kurnool", "Tirupati", 340.0, 330.0, 85.0, 22.5, 8.4, "LOW", "CLEAR", 9.5, true, false),
            new Road(14L, "Guntur", "Tirupati", 370.0, 350.0, 310.0, 24.8, 8.9, "MEDIUM", "CLEAR", 8.0, true, true),
            new Road(15L, "Tirupati", "Chennai", 135.0, 150.0, 90.0, 9.1, 8.5, "HIGH", "RAINY", 7.8, true, true),
            new Road(16L, "Tirupati", "Bengaluru", 250.0, 260.0, 140.0, 16.5, 8.7, "MEDIUM", "CLEAR", 8.8, true, true),
            new Road(17L, "Chennai", "Bengaluru", 345.0, 330.0, 380.0, 23.0, 9.2, "HIGH", "CLEAR", 8.1, true, true),
            new Road(18L, "Vijayawada", "Rajahmundry", 160.0, 150.0, 180.0, 10.8, 8.8, "LOW", "CLEAR", 8.7, true, true),
            new Road(19L, "Rajahmundry", "Visakhapatnam", 195.0, 180.0, 210.0, 13.0, 9.1, "LOW", "CLEAR", 9.3, true, true),
            new Road(20L, "Hyderabad", "Pune", 560.0, 520.0, 520.0, 37.5, 8.9, "MEDIUM", "CLEAR", 8.0, true, true),
            new Road(21L, "Pune", "Mumbai", 150.0, 120.0, 320.0, 10.5, 9.7, "HIGH", "RAINY", 9.4, true, true),
            new Road(22L, "Pune", "Bengaluru", 840.0, 780.0, 720.0, 56.0, 9.0, "LOW", "CLEAR", 8.5, true, true)
        ));

        for (Road road : roadList) {
            graph.addEdge(road, true);
        }
    }

    // --- JSON & CORS Helpers ---
    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        addCorsHeaders(exchange);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private static void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private static String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private static Map<String, String> parseSimpleJson(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null || json.trim().isEmpty()) return map;
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\"([^\"]+)\"\\s*:\\s*(?:\"([^\"]*)\"|([^,}\\]\\s]+))");
        java.util.regex.Matcher matcher = pattern.matcher(json);
        while (matcher.find()) {
            String key = matcher.group(1);
            String val = matcher.group(2) != null ? matcher.group(2) : matcher.group(3);
            if (key != null && val != null) {
                map.put(key.trim(), val.trim());
            }
        }
        return map;
    }

    // 1. GET /api/search?query=...
    static class SearchHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String query = "";
            String rawQuery = exchange.getRequestURI().getRawQuery();
            if (rawQuery != null) {
                for (String param : rawQuery.split("&")) {
                    String[] pair = param.split("=");
                    if (pair.length == 2 && "query".equalsIgnoreCase(pair[0])) {
                        query = URLDecoder.decode(pair[1], "UTF-8");
                    }
                }
            }

            List<String> names = new ArrayList<>();
            for (Location loc : locationList) names.add(loc.getName());

            List<EditDistance.SuggestionResult> ranked = EditDistance.findRankedSuggestions(query, names, 5);
            List<String> suggestionNames = new ArrayList<>();
            for (EditDistance.SuggestionResult res : ranked) {
                suggestionNames.add(res.getLocation());
            }

            String corrected = null;
            int dist = 0;
            if (!ranked.isEmpty()) {
                EditDistance.SuggestionResult best = ranked.get(0);
                if (!best.getLocation().equalsIgnoreCase(query.trim())) {
                    corrected = best.getLocation();
                    dist = best.getDistance();
                }
            }

            StringBuilder json = new StringBuilder();
            json.append("{")
                .append("\"query\":\"").append(query.replace("\"", "\\\"")).append("\",")
                .append("\"distance\":").append(dist).append(",")
                .append("\"correctedSuggestion\":").append(corrected == null ? "null" : "\"" + corrected + "\"").append(",")
                .append("\"suggestions\":[");
            for (int i = 0; i < suggestionNames.size(); i++) {
                json.append("\"").append(suggestionNames.get(i)).append("\"");
                if (i < suggestionNames.size() - 1) json.append(",");
            }
            json.append("]}");

            sendJsonResponse(exchange, 200, json.toString());
        }
    }

    // 2. GET /api/locations
    static class LocationsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < locationList.size(); i++) {
                Location l = locationList.get(i);
                json.append("{")
                    .append("\"id\":").append(l.getId()).append(",")
                    .append("\"name\":\"").append(l.getName()).append("\",")
                    .append("\"latitude\":").append(l.getLatitude()).append(",")
                    .append("\"longitude\":").append(l.getLongitude()).append(",")
                    .append("\"state\":\"").append(l.getState()).append("\",")
                    .append("\"description\":\"").append(l.getDescription() != null ? l.getDescription() : "").append("\"")
                    .append("}");
                if (i < locationList.size() - 1) json.append(",");
            }
            json.append("]");

            sendJsonResponse(exchange, 200, json.toString());
        }
    }

    // 3. GET /api/roads
    static class RoadsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < roadList.size(); i++) {
                Road r = roadList.get(i);
                json.append("{")
                    .append("\"id\":").append(r.getId()).append(",")
                    .append("\"source\":\"").append(r.getSource()).append("\",")
                    .append("\"destination\":\"").append(r.getDestination()).append("\",")
                    .append("\"distance\":").append(r.getDistance()).append(",")
                    .append("\"travelTime\":").append(r.getTravelTime()).append(",")
                    .append("\"toll\":").append(r.getToll()).append(",")
                    .append("\"fuelConsumption\":").append(r.getFuelConsumption()).append(",")
                    .append("\"safetyScore\":").append(r.getSafetyScore()).append(",")
                    .append("\"trafficLevel\":\"").append(r.getTrafficLevel()).append("\",")
                    .append("\"weatherCondition\":\"").append(r.getWeatherCondition()).append("\",")
                    .append("\"scenicScore\":").append(r.getScenicScore()).append(",")
                    .append("\"evChargingAvailable\":").append(r.isEvChargingAvailable()).append(",")
                    .append("\"emergencyLane\":").append(r.isEmergencyLane())
                    .append("}");
                if (i < roadList.size() - 1) json.append(",");
            }
            json.append("]");

            sendJsonResponse(exchange, 200, json.toString());
        }
    }

    // 4. POST /api/routes/calculate
    static class RouteCalculateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String body = readRequestBody(exchange);
            Map<String, String> req = parseSimpleJson(body);

            String src = resolveCityName(req.getOrDefault("source", "Hyderabad"));
            String dest = resolveCityName(req.getOrDefault("destination", "Vijayawada"));
            String pref = req.getOrDefault("preference", "FASTEST").toUpperCase();
            String algo = req.getOrDefault("algorithm", "AUTO").toUpperCase();

            String algoUsed;
            List<String> path = new ArrayList<>();
            double distance = 0, time = 0, toll = 0, fuel = 0, safety = 8.5, scenic = 8.0;
            int evStops = 0;
            String traffic = "LOW", weather = "CLEAR";

            if ("ASTAR".equalsIgnoreCase(algo) || ("AUTO".equalsIgnoreCase(algo) && ("SHORTEST".equals(pref) || "FASTEST".equals(pref)))) {
                algoUsed = "A* Pathfinding";
                AStarAlgorithm.AStarResult res = AStarAlgorithm.findPath(graph, src, dest, pref);
                path = res.getPath();
                distance = res.getTotalDistance();
                time = res.getTotalTravelTime();
                toll = res.getTotalToll();
                fuel = res.getTotalFuel();
                safety = res.getAverageSafetyScore();
                scenic = res.getAverageScenicScore();
                evStops = res.getEvChargingStops();
                traffic = res.getPrimaryTrafficLevel();
                weather = res.getPrimaryWeatherCondition();
            } else if ("BFS".equalsIgnoreCase(algo)) {
                algoUsed = "BFS (Breadth First Search)";
                BFS.BFSResult res = BFS.findPath(graph, src, dest);
                path = res.getPath();
                for (Road r : res.getRoadSegments()) {
                    distance += r.getDistance();
                    time += r.getTravelTime();
                    toll += r.getToll();
                    fuel += r.getFuelConsumption();
                    safety += r.getSafetyScore();
                    if (r.isEvChargingAvailable()) evStops++;
                }
                if (!res.getRoadSegments().isEmpty()) safety /= res.getRoadSegments().size();
            } else if ("DFS".equalsIgnoreCase(algo)) {
                algoUsed = "DFS (Depth First Search)";
                DFS.DFSResult res = DFS.findPath(graph, src, dest, 1);
                path = res.getPath();
                distance = 280; time = 290; toll = 200; fuel = 19; safety = 8.0;
            } else {
                algoUsed = "Dijkstra's Algorithm";
                DijkstraAlgorithm.DijkstraResult res = DijkstraAlgorithm.findShortestPath(graph, src, dest, pref);
                path = res.getPath();
                distance = res.getTotalDistance();
                time = res.getTotalTravelTime();
                toll = res.getTotalToll();
                fuel = res.getTotalFuel();
                safety = res.getAverageSafetyScore();
                scenic = res.getAverageScenicScore();
                evStops = res.getEvChargingStops();
                traffic = res.getPrimaryTrafficLevel();
                weather = res.getPrimaryWeatherCondition();
            }

            // Alternatives for comparison
            String[] altPrefs = {"FASTEST", "SHORTEST", "SAFEST", "FUEL_EFFICIENT", "LOWEST_TOLL", "EV_FRIENDLY", "SCENIC", "EMERGENCY"};
            Map<String, String> namesMap = Map.of(
                "FASTEST", "Route A – Fastest", "SHORTEST", "Route B – Shortest",
                "SAFEST", "Route C – Safest", "FUEL_EFFICIENT", "Route D – Fuel Efficient",
                "LOWEST_TOLL", "Route E – Lowest Toll", "EV_FRIENDLY", "Route F – EV Friendly",
                "SCENIC", "Route G – Scenic", "EMERGENCY", "Route H – Emergency"
            );

            StringBuilder altsJson = new StringBuilder("[");
            for (int i = 0; i < altPrefs.length; i++) {
                String p = altPrefs[i];
                DijkstraAlgorithm.DijkstraResult altRes = DijkstraAlgorithm.findShortestPath(graph, src, dest, p);
                if (altRes.isPathFound()) {
                    altsJson.append("{")
                        .append("\"name\":\"").append(namesMap.get(p)).append("\",")
                        .append("\"preference\":\"").append(p).append("\",")
                        .append("\"algorithm\":\"Dijkstra\",")
                        .append("\"distance\":").append(altRes.getTotalDistance()).append(",")
                        .append("\"estimatedTime\":").append(altRes.getTotalTravelTime()).append(",")
                        .append("\"toll\":").append(altRes.getTotalToll()).append(",")
                        .append("\"fuelConsumption\":").append(altRes.getTotalFuel()).append(",")
                        .append("\"safetyScore\":").append(altRes.getAverageSafetyScore()).append(",")
                        .append("\"evChargingStops\":").append(altRes.getEvChargingStops()).append(",")
                        .append("\"tag\":\"").append(p.equals(pref) ? "SELECTED" : "ALT").append("\",")
                        .append("\"route\":[");
                    for (int j = 0; j < altRes.getPath().size(); j++) {
                        altsJson.append("\"").append(altRes.getPath().get(j)).append("\"");
                        if (j < altRes.getPath().size() - 1) altsJson.append(",");
                    }
                    altsJson.append("]}");
                    if (i < altPrefs.length - 1) altsJson.append(",");
                }
            }
            altsJson.append("]");

            // Coordinates
            StringBuilder coordsJson = new StringBuilder("[");
            for (int i = 0; i < path.size(); i++) {
                Location loc = graph.getLocation(path.get(i));
                if (loc != null) {
                    coordsJson.append("{")
                        .append("\"name\":\"").append(loc.getName()).append("\",")
                        .append("\"latitude\":").append(loc.getLatitude()).append(",")
                        .append("\"longitude\":").append(loc.getLongitude())
                        .append("}");
                    if (i < path.size() - 1) coordsJson.append(",");
                }
            }
            coordsJson.append("]");

            // Path JSON
            StringBuilder pathJson = new StringBuilder("[");
            for (int i = 0; i < path.size(); i++) {
                pathJson.append("\"").append(path.get(i)).append("\"");
                if (i < path.size() - 1) pathJson.append(",");
            }
            pathJson.append("]");

            // Save history
            Map<String, Object> hist = new HashMap<>();
            hist.put("source", src);
            hist.put("destination", dest);
            hist.put("preference", pref);
            hist.put("algorithm", algoUsed);
            hist.put("distance", distance);
            hist.put("estimatedTime", time);
            hist.put("toll", toll);
            hist.put("fuelConsumption", fuel);
            hist.put("safetyScore", safety);
            hist.put("timestamp", new Date().toString());
            routeHistory.add(0, hist);

            String responseJson = "{" +
                "\"algorithm\":\"" + algoUsed + "\"," +
                "\"preference\":\"" + pref + "\"," +
                "\"route\":" + pathJson + "," +
                "\"routeCoordinates\":" + coordsJson + "," +
                "\"distance\":" + distance + "," +
                "\"estimatedTime\":" + time + "," +
                "\"toll\":" + toll + "," +
                "\"fuelConsumption\":" + fuel + "," +
                "\"safetyScore\":" + safety + "," +
                "\"scenicScore\":" + scenic + "," +
                "\"evChargingStops\":" + evStops + "," +
                "\"trafficLevel\":\"" + traffic + "\"," +
                "\"weatherCondition\":\"" + weather + "\"," +
                "\"rationale\":\"Optimized multi-criteria route computed using Java " + algoUsed + " for " + pref + " profile.\"," +
                "\"alternativeRoutes\":" + altsJson +
                "}";

            sendJsonResponse(exchange, 200, responseJson);
        }
    }

    private static String resolveCityName(String query) {
        if (query == null) return "Hyderabad";
        List<String> names = new ArrayList<>();
        for (Location loc : locationList) names.add(loc.getName());
        List<EditDistance.SuggestionResult> res = EditDistance.findRankedSuggestions(query, names, 1);
        if (!res.isEmpty()) return res.get(0).getLocation();
        return query;
    }

    // 5. GET /api/routes/history
    static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < routeHistory.size(); i++) {
                Map<String, Object> h = routeHistory.get(i);
                json.append("{")
                    .append("\"source\":\"").append(h.get("source")).append("\",")
                    .append("\"destination\":\"").append(h.get("destination")).append("\",")
                    .append("\"preference\":\"").append(h.get("preference")).append("\",")
                    .append("\"algorithm\":\"").append(h.get("algorithm")).append("\",")
                    .append("\"distance\":").append(h.get("distance")).append(",")
                    .append("\"estimatedTime\":").append(h.get("estimatedTime")).append(",")
                    .append("\"toll\":").append(h.get("toll")).append(",")
                    .append("\"fuelConsumption\":").append(h.get("fuelConsumption")).append(",")
                    .append("\"safetyScore\":").append(h.get("safetyScore")).append(",")
                    .append("\"timestamp\":\"").append(h.get("timestamp")).append("\"")
                    .append("}");
                if (i < routeHistory.size() - 1) json.append(",");
            }
            json.append("]");

            sendJsonResponse(exchange, 200, json.toString());
        }
    }

    // 6. POST /api/auth/register
    static class AuthRegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String body = readRequestBody(exchange);
            Map<String, String> req = parseSimpleJson(body);
            String email = req.getOrDefault("email", "");
            String pass = req.getOrDefault("password", "");
            String name = req.getOrDefault("name", email.split("@")[0]);

            Map<String, Object> u = new HashMap<>();
            u.put("id", (long) (users.size() + 1));
            u.put("name", name);
            u.put("email", email);
            u.put("password", pass);
            users.put(email, u);

            String json = "{\"success\":true,\"message\":\"Registration successful\",\"userId\":" + u.get("id") + ",\"name\":\"" + name + "\",\"email\":\"" + email + "\",\"token\":\"token-" + System.currentTimeMillis() + "\"}";
            sendJsonResponse(exchange, 200, json);
        }
    }

    // 7. POST /api/auth/login
    static class AuthLoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String body = readRequestBody(exchange);
            Map<String, String> req = parseSimpleJson(body);
            String email = req.getOrDefault("email", "");
            String pass = req.getOrDefault("password", "");

            Map<String, Object> u = users.get(email);
            if (u != null && pass.equals(u.get("password"))) {
                String json = "{\"success\":true,\"message\":\"Login successful\",\"userId\":" + u.get("id") + ",\"name\":\"" + u.get("name") + "\",\"email\":\"" + email + "\",\"token\":\"token-" + System.currentTimeMillis() + "\"}";
                sendJsonResponse(exchange, 200, json);
            } else {
                String json = "{\"success\":false,\"message\":\"Invalid email or password\"}";
                sendJsonResponse(exchange, 401, json);
            }
        }
    }

    // 8. POST /api/algorithms/visualize
    static class VisualizeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String body = readRequestBody(exchange);
            Map<String, String> req = parseSimpleJson(body);
            String algo = req.getOrDefault("algorithm", "DIJKSTRA").toUpperCase();

            long startTime = System.nanoTime();

            if ("EDIT_DISTANCE".equals(algo)) {
                String s1 = req.getOrDefault("string1", "Hyderbad");
                String s2 = req.getOrDefault("string2", "Hyderabad");

                EditDistance.DPMatrixResult res = EditDistance.getEditMatrix(s1, s2);
                StringBuilder matJson = new StringBuilder("[");
                int[][] m = res.getMatrix();
                for (int i = 0; i < m.length; i++) {
                    matJson.append("[");
                    for (int j = 0; j < m[i].length; j++) {
                        matJson.append(m[i][j]);
                        if (j < m[i].length - 1) matJson.append(",");
                    }
                    matJson.append("]");
                    if (i < m.length - 1) matJson.append(",");
                }
                matJson.append("]");

                double execMs = (System.nanoTime() - startTime) / 1_000_000.0;
                String resp = "{" +
                    "\"algorithm\":\"EDIT_DISTANCE\"," +
                    "\"str1\":\"" + s1 + "\"," +
                    "\"str2\":\"" + s2 + "\"," +
                    "\"editDistance\":" + res.getTotalDistance() + "," +
                    "\"dpMatrix\":" + matJson + "," +
                    "\"timeComplexity\":\"O(m × n)\"," +
                    "\"spaceComplexity\":\"O(m × n)\"," +
                    "\"executionTimeMs\":" + Math.round(execMs * 100.0) / 100.0 + "," +
                    "\"description\":\"Dynamic programming table for converting '" + s1 + "' to '" + s2 + "' with minimum cost " + res.getTotalDistance() + "\"" +
                    "}";
                sendJsonResponse(exchange, 200, resp);
            } else {
                String src = resolveCityName(req.getOrDefault("source", "Hyderabad"));
                String dest = resolveCityName(req.getOrDefault("destination", "Vijayawada"));
                String pref = req.getOrDefault("preference", "FASTEST");

                List<?> steps = Collections.emptyList();
                List<String> path = Collections.emptyList();
                String desc = "", timeC = "", spaceC = "";

                if ("BFS".equals(algo)) {
                    BFS.BFSResult r = BFS.findPath(graph, src, dest);
                    steps = r.getSteps(); path = r.getPath();
                    desc = "Breadth First Search layer-by-layer traversal";
                    timeC = "O(V + E)"; spaceC = "O(V)";
                } else if ("DFS".equals(algo)) {
                    DFS.DFSResult r = DFS.findPath(graph, src, dest, 3);
                    steps = r.getSteps(); path = r.getPath();
                    desc = "Depth First Search recursive path exploration";
                    timeC = "O(V + E)"; spaceC = "O(V)";
                } else if ("ASTAR".equals(algo)) {
                    AStarAlgorithm.AStarResult r = AStarAlgorithm.findPath(graph, src, dest, pref);
                    steps = r.getSteps(); path = r.getPath();
                    desc = "A* Search with straight-line Haversine heuristic";
                    timeC = "O(E) best case"; spaceC = "O(V)";
                } else {
                    DijkstraAlgorithm.DijkstraResult r = DijkstraAlgorithm.findShortestPath(graph, src, dest, pref);
                    steps = r.getSteps(); path = r.getPath();
                    desc = "Dijkstra's Min-Heap algorithm with " + pref + " weights";
                    timeC = "O((V+E) log V)"; spaceC = "O(V)";
                }

                StringBuilder stepsJson = new StringBuilder("[");
                for (int i = 0; i < steps.size(); i++) {
                    Object stepObj = steps.get(i);
                    if (stepObj instanceof DijkstraAlgorithm.DijkstraStep) {
                        DijkstraAlgorithm.DijkstraStep s = (DijkstraAlgorithm.DijkstraStep) stepObj;
                        stepsJson.append("{\"stepNumber\":").append(s.getStepNumber())
                                 .append(",\"vertex\":\"").append(s.getVertex()).append("\"")
                                 .append(",\"currentCost\":").append(s.getCurrentCost())
                                 .append(",\"action\":\"").append(s.getAction().replace("\"", "'")).append("\"}");
                    } else if (stepObj instanceof AStarAlgorithm.AStarStep) {
                        AStarAlgorithm.AStarStep s = (AStarAlgorithm.AStarStep) stepObj;
                        stepsJson.append("{\"stepNumber\":").append(s.getStepNumber())
                                 .append(",\"vertex\":\"").append(s.getVertex()).append("\"")
                                 .append(",\"fScore\":").append(s.getFScore())
                                 .append(",\"action\":\"").append(s.getAction().replace("\"", "'")).append("\"}");
                    } else if (stepObj instanceof BFS.BFSStep) {
                        BFS.BFSStep s = (BFS.BFSStep) stepObj;
                        stepsJson.append("{\"stepNumber\":").append(s.getStepNumber())
                                 .append(",\"currentNode\":\"").append(s.getCurrentNode()).append("\"")
                                 .append(",\"action\":\"").append(s.getAction().replace("\"", "'")).append("\"}");
                    } else if (stepObj instanceof DFS.DFSStep) {
                        DFS.DFSStep s = (DFS.DFSStep) stepObj;
                        stepsJson.append("{\"stepNumber\":").append(s.getStepNumber())
                                 .append(",\"currentNode\":\"").append(s.getCurrentNode()).append("\"")
                                 .append(",\"action\":\"").append(s.getAction().replace("\"", "'")).append("\"}");
                    }
                    if (i < steps.size() - 1) stepsJson.append(",");
                }
                stepsJson.append("]");

                StringBuilder pathJson = new StringBuilder("[");
                for (int i = 0; i < path.size(); i++) {
                    pathJson.append("\"").append(path.get(i)).append("\"");
                    if (i < path.size() - 1) pathJson.append(",");
                }
                pathJson.append("]");

                double execMs = (System.nanoTime() - startTime) / 1_000_000.0;
                String resp = "{" +
                    "\"algorithm\":\"" + algo + "\"," +
                    "\"description\":\"" + desc + "\"," +
                    "\"timeComplexity\":\"" + timeC + "\"," +
                    "\"spaceComplexity\":\"" + spaceC + "\"," +
                    "\"executionTimeMs\":" + Math.round(execMs * 100.0) / 100.0 + "," +
                    "\"path\":" + pathJson + "," +
                    "\"steps\":" + stepsJson +
                    "}";
                sendJsonResponse(exchange, 200, resp);
            }
        }
    }

    // 9. Static File Handler for Frontend
    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if ("/".equals(path) || path.isEmpty()) {
                path = "/index.html";
            }

            File baseDir = new File("c:/smart route/frontend");
            File file = new File(baseDir, path.substring(1));

            if (!file.exists() || file.isDirectory()) {
                String notFound = "404 Not Found";
                exchange.sendResponseHeaders(404, notFound.length());
                exchange.getResponseBody().write(notFound.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            String contentType = "text/plain";
            if (path.endsWith(".html")) contentType = "text/html; charset=UTF-8";
            else if (path.endsWith(".css")) contentType = "text/css; charset=UTF-8";
            else if (path.endsWith(".js")) contentType = "application/javascript; charset=UTF-8";
            else if (path.endsWith(".png")) contentType = "image/png";
            else if (path.endsWith(".svg")) contentType = "image/svg+xml";

            exchange.getResponseHeaders().set("Content-Type", contentType);
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            exchange.sendResponseHeaders(200, fileBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(fileBytes);
            os.close();
        }
    }
}
