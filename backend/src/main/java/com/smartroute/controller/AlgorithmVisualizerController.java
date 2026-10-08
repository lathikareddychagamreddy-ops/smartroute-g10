package com.smartroute.controller;

import com.smartroute.algorithm.*;
import com.smartroute.model.AlgorithmVisualizerRequest;
import com.smartroute.model.AlgorithmVisualizerResponse;
import com.smartroute.service.RouteRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/algorithms")
@CrossOrigin(origins = "*")
public class AlgorithmVisualizerController {

    private final RouteRecommendationService routeRecommendationService;

    public AlgorithmVisualizerController(RouteRecommendationService routeRecommendationService) {
        this.routeRecommendationService = routeRecommendationService;
    }

    @PostMapping("/visualize")
    public ResponseEntity<AlgorithmVisualizerResponse> visualizeAlgorithm(@RequestBody AlgorithmVisualizerRequest request) {
        String algo = request.getAlgorithm() != null ? request.getAlgorithm().toUpperCase().trim() : "DIJKSTRA";
        AlgorithmVisualizerResponse resp = new AlgorithmVisualizerResponse();
        resp.setAlgorithm(algo);

        long startTime = System.nanoTime();

        if ("EDIT_DISTANCE".equals(algo)) {
            String s1 = request.getString1() != null ? request.getString1() : "Hyderbad";
            String s2 = request.getString2() != null ? request.getString2() : "Hyderabad";

            EditDistance.DPMatrixResult matrixRes = EditDistance.getEditMatrix(s1, s2);
            resp.setStr1(s1);
            resp.setStr2(s2);
            resp.setDpMatrix(matrixRes.getMatrix());
            resp.setEditDistance(matrixRes.getTotalDistance());
            resp.setDescription("Levenshtein Distance Dynamic Programming table comparing '" + s1 + "' and '" + s2 + "'. Minimum edit cost = " + matrixRes.getTotalDistance());
            resp.setTimeComplexity("O(m × n)");
            resp.setSpaceComplexity("O(m × n)");
            resp.setSuccess(true);
        } else {
            Graph graph = routeRecommendationService.buildGraph();
            String src = request.getSource() != null ? request.getSource().trim() : "Hyderabad";
            String dest = request.getDestination() != null ? request.getDestination().trim() : "Vijayawada";
            String pref = request.getPreference() != null ? request.getPreference().trim() : "FASTEST";

            if (!graph.hasVertex(src) || !graph.hasVertex(dest)) {
                resp.setSuccess(false);
                resp.setMessage("Source or Destination vertex not found in network");
                return ResponseEntity.badRequest().body(resp);
            }

            switch (algo) {
                case "BFS": {
                    BFS.BFSResult res = BFS.findPath(graph, src, dest);
                    resp.setPath(res.getPath());
                    resp.setVisitedOrder(res.getVisitedOrder());
                    resp.setSteps(res.getSteps());
                    resp.setDescription("Breadth First Search (BFS) explores all neighbors level-by-level using a FIFO queue.");
                    resp.setTimeComplexity("O(V + E)");
                    resp.setSpaceComplexity("O(V)");
                    resp.setSuccess(res.isPathFound());
                    break;
                }
                case "DFS": {
                    DFS.DFSResult res = DFS.findPath(graph, src, dest, 5);
                    resp.setPath(res.getPath());
                    resp.setVisitedOrder(res.getVisitedOrder());
                    resp.setSteps(res.getSteps());
                    resp.setDescription("Depth First Search (DFS) explores paths deeply using recursive backtracking and visited state tracking.");
                    resp.setTimeComplexity("O(V + E)");
                    resp.setSpaceComplexity("O(V)");
                    resp.setSuccess(res.isPathFound());
                    break;
                }
                case "ASTAR": {
                    AStarAlgorithm.AStarResult res = AStarAlgorithm.findPath(graph, src, dest, pref);
                    resp.setPath(res.getPath());
                    resp.setSteps(res.getSteps());
                    resp.setDescription("A* Pathfinding combines actual path cost g(n) with straight-line Haversine heuristic h(n) (f(n) = g(n) + h(n)) using a Min-Heap.");
                    resp.setTimeComplexity("O(E) best, O((V+E) log V) worst");
                    resp.setSpaceComplexity("O(V)");
                    resp.setSuccess(res.isPathFound());
                    break;
                }
                case "DIJKSTRA":
                default: {
                    DijkstraAlgorithm.DijkstraResult res = DijkstraAlgorithm.findShortestPath(graph, src, dest, pref);
                    resp.setPath(res.getPath());
                    resp.setSteps(res.getSteps());
                    resp.setDescription("Dijkstra's Algorithm finds the optimal path using a Min-Heap PriorityQueue with dynamic multi-criteria edge weighting (" + pref + ").");
                    resp.setTimeComplexity("O((V + E) log V)");
                    resp.setSpaceComplexity("O(V)");
                    resp.setSuccess(res.isPathFound());
                    break;
                }
            }
        }

        long endTime = System.nanoTime();
        double execMs = (endTime - startTime) / 1_000_000.0;
        resp.setExecutionTimeMs(Math.round(execMs * 1000.0) / 1000.0);

        return ResponseEntity.ok(resp);
    }
}
