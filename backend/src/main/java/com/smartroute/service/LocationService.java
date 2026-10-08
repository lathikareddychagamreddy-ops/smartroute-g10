package com.smartroute.service;

import com.smartroute.algorithm.EditDistance;
import com.smartroute.model.LocationEntity;
import com.smartroute.model.SearchResponse;
import com.smartroute.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationEntity> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<LocationEntity> getLocationByName(String name) {
        return locationRepository.findByNameIgnoreCase(name);
    }

    /**
     * Searches for matching location names using Java Edit Distance dynamic programming.
     */
    public SearchResponse searchLocations(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new SearchResponse(query, Collections.emptyList(), null, 0);
        }

        List<String> allNames = locationRepository.findAll().stream()
                .map(LocationEntity::getName)
                .collect(Collectors.toList());

        List<EditDistance.SuggestionResult> suggestions = EditDistance.findRankedSuggestions(query, allNames, 5);

        List<String> suggestionNames = suggestions.stream()
                .map(EditDistance.SuggestionResult::getLocation)
                .collect(Collectors.toList());

        String corrected = null;
        int minDistance = 0;

        if (!suggestions.isEmpty()) {
            EditDistance.SuggestionResult best = suggestions.get(0);
            // If the query does not exactly match the top result, suggest "Did you mean X?"
            if (!best.getLocation().equalsIgnoreCase(query.trim())) {
                corrected = best.getLocation();
                minDistance = best.getDistance();
            }
        }

        return new SearchResponse(query, suggestionNames, corrected, minDistance);
    }
}
