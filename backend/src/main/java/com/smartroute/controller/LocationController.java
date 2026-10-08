package com.smartroute.controller;

import com.smartroute.model.LocationEntity;
import com.smartroute.model.RoadEntity;
import com.smartroute.model.SearchResponse;
import com.smartroute.repository.RoadRepository;
import com.smartroute.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LocationController {

    private final LocationService locationService;
    private final RoadRepository roadRepository;

    public LocationController(LocationService locationService, RoadRepository roadRepository) {
        this.locationService = locationService;
        this.roadRepository = roadRepository;
    }

    /**
     * Location search and autocomplete using Levenshtein Edit Distance Dynamic Programming.
     * Example: GET /api/search?query=Hyderbad -> returns suggestions and "Hyderabad"
     */
    @GetMapping("/search")
    public ResponseEntity<SearchResponse> searchLocations(@RequestParam("query") String query) {
        SearchResponse response = locationService.searchLocations(query);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all network location vertices.
     */
    @GetMapping("/locations")
    public ResponseEntity<List<LocationEntity>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    /**
     * Get all road network edges.
     */
    @GetMapping("/roads")
    public ResponseEntity<List<RoadEntity>> getAllRoads() {
        return ResponseEntity.ok(roadRepository.findAll());
    }
}
