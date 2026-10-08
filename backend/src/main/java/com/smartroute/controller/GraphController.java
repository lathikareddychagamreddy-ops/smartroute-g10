package com.smartroute.controller;

import com.smartroute.model.Edge;
import com.smartroute.model.Graph;
import com.smartroute.model.Node;
import com.smartroute.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/graph")
@CrossOrigin(origins = "*")
public class GraphController {

    private final RouteService routeService;

    public GraphController(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * GET /api/graph
     * Returns full graph representation (nodes with geographic coordinates & amenities,
     * edges with distances, times, tolls, safety, traffic, scenic scores).
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getGraph() {
        Graph graph = routeService.getGraph();
        Map<String, Object> response = new LinkedHashMap<>();

        List<Node> nodes = new ArrayList<>(graph.getNodeMap().values());
        List<Edge> edges = new ArrayList<>();
        for (List<Edge> edgeList : graph.getAdjacencyList().values()) {
            for (Edge e : edgeList) {
                // Return positive ID edges to avoid duplicate reverse lines in UI map
                if (e.getId() != null && e.getId() > 0) {
                    edges.add(e);
                }
            }
        }

        response.put("totalNodes", graph.getNodeCount());
        response.put("totalEdges", edges.size());
        response.put("nodes", nodes);
        response.put("edges", edges);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/graph/nodes
     * Returns all available location names and coordinates.
     */
    @GetMapping("/nodes")
    public ResponseEntity<List<Node>> getNodes() {
        return ResponseEntity.ok(routeService.getAllNodes());
    }
}
