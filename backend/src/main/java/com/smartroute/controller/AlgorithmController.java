package com.smartroute.controller;

import com.smartroute.model.AlgorithmInfo;
import com.smartroute.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/algorithms")
@CrossOrigin(origins = "*")
public class AlgorithmController {

    private final RouteService routeService;

    public AlgorithmController(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * GET /api/algorithms
     * Returns algorithm metadata, theoretical complexities, data structures,
     * and experimental demonstration graph execution benchmarks.
     */
    @GetMapping
    public ResponseEntity<List<AlgorithmInfo>> getAlgorithms() {
        return ResponseEntity.ok(routeService.getAlgorithmsMetadata());
    }
}
