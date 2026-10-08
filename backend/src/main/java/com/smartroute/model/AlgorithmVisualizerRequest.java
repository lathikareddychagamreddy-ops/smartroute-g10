package com.smartroute.model;

import java.util.List;

public class AlgorithmVisualizerRequest {
    private String algorithm; // BFS, DFS, DIJKSTRA, ASTAR, EDIT_DISTANCE
    private String source;
    private String destination;
    private String preference;
    private String string1; // for Edit Distance
    private String string2; // for Edit Distance

    public AlgorithmVisualizerRequest() {}

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getPreference() { return preference; }
    public void setPreference(String preference) { this.preference = preference; }

    public String getString1() { return string1; }
    public void setString1(String string1) { this.string1 = string1; }

    public String getString2() { return string2; }
    public void setString2(String string2) { this.string2 = string2; }
}
