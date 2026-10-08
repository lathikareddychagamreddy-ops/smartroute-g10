package com.smartroute.model;

import java.util.List;

public class AlgorithmVisualizerResponse {
    private String algorithm;
    private String description;
    private String timeComplexity;
    private String spaceComplexity;
    private List<String> path;
    private List<String> visitedOrder;
    private List<?> steps;
    private int[][] dpMatrix; // for Edit Distance
    private String str1;
    private String str2;
    private int editDistance;
    private double executionTimeMs;
    private boolean success;
    private String message;

    public AlgorithmVisualizerResponse() {}

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTimeComplexity() { return timeComplexity; }
    public void setTimeComplexity(String timeComplexity) { this.timeComplexity = timeComplexity; }

    public String getSpaceComplexity() { return spaceComplexity; }
    public void setSpaceComplexity(String spaceComplexity) { this.spaceComplexity = spaceComplexity; }

    public List<String> getPath() { return path; }
    public void setPath(List<String> path) { this.path = path; }

    public List<String> getVisitedOrder() { return visitedOrder; }
    public void setVisitedOrder(List<String> visitedOrder) { this.visitedOrder = visitedOrder; }

    public List<?> getSteps() { return steps; }
    public void setSteps(List<?> steps) { this.steps = steps; }

    public int[][] getDpMatrix() { return dpMatrix; }
    public void setDpMatrix(int[][] dpMatrix) { this.dpMatrix = dpMatrix; }

    public String getStr1() { return str1; }
    public void setStr1(String str1) { this.str1 = str1; }

    public String getStr2() { return str2; }
    public void setStr2(String str2) { this.str2 = str2; }

    public int getEditDistance() { return editDistance; }
    public void setEditDistance(int editDistance) { this.editDistance = editDistance; }

    public double getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(double executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
