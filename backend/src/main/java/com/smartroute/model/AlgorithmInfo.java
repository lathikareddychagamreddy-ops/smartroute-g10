package com.smartroute.model;

import java.util.List;

public class AlgorithmInfo {
    private String id;
    private String name;
    private String purpose;
    private String complexity;
    private double measuredTimeMs;
    private List<String> dataStructures;
    private String description;
    private String sampleRoute;
    private String keyOptimization;

    public AlgorithmInfo() {}

    public AlgorithmInfo(String id, String name, String purpose, String complexity,
                         double measuredTimeMs, List<String> dataStructures,
                         String description, String sampleRoute, String keyOptimization) {
        this.id = id;
        this.name = name;
        this.purpose = purpose;
        this.complexity = complexity;
        this.measuredTimeMs = measuredTimeMs;
        this.dataStructures = dataStructures;
        this.description = description;
        this.sampleRoute = sampleRoute;
        this.keyOptimization = keyOptimization;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getComplexity() { return complexity; }
    public void setComplexity(String complexity) { this.complexity = complexity; }

    public double getMeasuredTimeMs() { return measuredTimeMs; }
    public void setMeasuredTimeMs(double measuredTimeMs) { this.measuredTimeMs = measuredTimeMs; }

    public List<String> getDataStructures() { return dataStructures; }
    public void setDataStructures(List<String> dataStructures) { this.dataStructures = dataStructures; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSampleRoute() { return sampleRoute; }
    public void setSampleRoute(String sampleRoute) { this.sampleRoute = sampleRoute; }

    public String getKeyOptimization() { return keyOptimization; }
    public void setKeyOptimization(String keyOptimization) { this.keyOptimization = keyOptimization; }
}
