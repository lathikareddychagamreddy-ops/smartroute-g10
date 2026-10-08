package com.smartroute.model;

import java.util.List;

public class SearchResponse {
    private String query;
    private List<String> suggestions;
    private String correctedSuggestion; // Closest match if query had typo ("Did you mean X?")
    private int distance;

    public SearchResponse() {}

    public SearchResponse(String query, List<String> suggestions, String correctedSuggestion, int distance) {
        this.query = query;
        this.suggestions = suggestions;
        this.correctedSuggestion = correctedSuggestion;
        this.distance = distance;
    }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }

    public String getCorrectedSuggestion() { return correctedSuggestion; }
    public void setCorrectedSuggestion(String correctedSuggestion) { this.correctedSuggestion = correctedSuggestion; }

    public int getDistance() { return distance; }
    public void setDistance(int distance) { this.distance = distance; }
}
