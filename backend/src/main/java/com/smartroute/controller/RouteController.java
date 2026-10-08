package com.smartroute.controller;

import com.smartroute.dto.DynamicConditionRequest;
import com.smartroute.dto.DynamicConditionResponse;
import com.smartroute.dto.RouteRequest;
import com.smartroute.dto.RouteResponse;
import com.smartroute.model.Route;
import com.smartroute.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * POST /api/routes/calculate
     * Main endpoint: calculates Dijkstra, A*, Modified Dijkstra, BFS, and DFS routes,
     * along with comparison matrix and execution benchmarks.
     */
    @PostMapping("/calculate")
    public ResponseEntity<RouteResponse> calculateRoutes(@RequestBody RouteRequest request) {
        RouteResponse response = routeService.calculateAllRoutes(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/routes/shortest
     * Computes the shortest distance route using Dijkstra's Algorithm with PriorityQueue.
     */
    @PostMapping("/shortest")
    public ResponseEntity<?> calculateShortest(@RequestBody RouteRequest request) {
        Route route = routeService.calculateShortestOnly(request);
        if (route == null) {
            return ResponseEntity.badRequest().body("No route found between selected points.");
        }
        return ResponseEntity.ok(route);
    }

    /**
     * POST /api/routes/fastest
     * Computes the fastest travel time route using A* Search Algorithm.
     */
    @PostMapping("/fastest")
    public ResponseEntity<?> calculateFastest(@RequestBody RouteRequest request) {
        Route route = routeService.calculateFastestOnly(request);
        if (route == null) {
            return ResponseEntity.badRequest().body("No route found between selected points.");
        }
        return ResponseEntity.ok(route);
    }

    /**
     * POST /api/routes/recommended
     * Computes multi-criteria recommended route using Modified Dijkstra.
     */
    @PostMapping("/recommended")
    public ResponseEntity<?> calculateRecommended(@RequestBody RouteRequest request) {
        Route route = routeService.calculateRecommendedOnly(request);
        if (route == null) {
            return ResponseEntity.badRequest().body("No route found between selected points.");
        }
        return ResponseEntity.ok(route);
    }

    /**
     * POST /api/routes/ev-charging
     * Discovers EV charging stations along reachable corridors using BFS.
     */
    @PostMapping("/ev-charging")
    public ResponseEntity<?> calculateEVCharging(@RequestBody RouteRequest request) {
        Route route = routeService.calculateEVChargingOnly(request);
        if (route == null) {
            return ResponseEntity.badRequest().body("No EV route found between selected points.");
        }
        return ResponseEntity.ok(route);
    }

    /**
     * POST /api/routes/scenic
     * Explores scenic alternative paths using DFS.
     */
    @PostMapping("/scenic")
    public ResponseEntity<?> calculateScenic(@RequestBody RouteRequest request) {
        Route route = routeService.calculateScenicOnly(request);
        if (route == null) {
            return ResponseEntity.badRequest().body("No scenic route found between selected points.");
        }
        return ResponseEntity.ok(route);
    }

    /**
     * POST /api/routes/recalculate
     * Dynamically recalculates routes upon changing Traffic and Weather conditions,
     * providing Before vs After comparisons and trade-off explanations.
     */
    @PostMapping("/recalculate")
    public ResponseEntity<DynamicConditionResponse> recalculateDynamicConditions(@RequestBody DynamicConditionRequest request) {
        DynamicConditionResponse response = routeService.recalculateDynamicCondition(request);
        return ResponseEntity.ok(response);
    }
}
