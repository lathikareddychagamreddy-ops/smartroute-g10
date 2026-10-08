package com.smartroute.algorithm;

import java.util.*;

/**
 * DFS - Depth-First Search Algorithm Implementation
 * Explores deep branch paths with recursive backtracking to discover paths between nodes.
 * Records search tree traversal steps for visualization.
 */
public class DFS {

    public static class DFSResult {
        private final List<String> path;
        private final List<List<String>> allPaths;
        private final List<String> visitedOrder;
        private final List<DFSStep> steps;
        private final boolean pathFound;

        public DFSResult(List<String> path, List<List<String>> allPaths, List<String> visitedOrder, List<DFSStep> steps, boolean pathFound) {
            this.path = path;
            this.allPaths = allPaths;
            this.visitedOrder = visitedOrder;
            this.steps = steps;
            this.pathFound = pathFound;
        }

        public List<String> getPath() { return path; }
        public List<List<String>> getAllPaths() { return allPaths; }
        public List<String> getVisitedOrder() { return visitedOrder; }
        public List<DFSStep> getSteps() { return steps; }
        public boolean isPathFound() { return pathFound; }
    }

    public static class DFSStep {
        private final int stepNumber;
        private final String currentNode;
        private final List<String> currentPath;
        private final String action;

        public DFSStep(int stepNumber, String currentNode, List<String> currentPath, String action) {
            this.stepNumber = stepNumber;
            this.currentNode = currentNode;
            this.currentPath = currentPath;
            this.action = action;
        }

        public int getStepNumber() { return stepNumber; }
        public String getCurrentNode() { return currentNode; }
        public List<String> getCurrentPath() { return currentPath; }
        public String getAction() { return action; }
    }

    /**
     * Executes DFS to find the first path as well as explore alternative acyclic paths (up to maxPaths).
     * Time Complexity: O(V + E)
     */
    public static DFSResult findPath(Graph graph, String source, String destination, int maxPaths) {
        List<String> visitedOrder = new ArrayList<>();
        List<DFSStep> steps = new ArrayList<>();
        List<List<String>> allPaths = new ArrayList<>();

        if (graph == null || !graph.hasVertex(source) || !graph.hasVertex(destination)) {
            return new DFSResult(Collections.emptyList(), allPaths, visitedOrder, steps, false);
        }

        Set<String> visited = new HashSet<>();
        List<String> currentPath = new ArrayList<>();
        int[] stepCounter = new int[]{1};

        currentPath.add(source);
        visited.add(source);
        visitedOrder.add(source);

        steps.add(new DFSStep(stepCounter[0]++, source, new ArrayList<>(currentPath),
                "DFS started at source vertex: " + source));

        dfsRecursive(graph, source, destination, visited, currentPath, visitedOrder, steps, allPaths, stepCounter, maxPaths);

        List<String> primaryPath = allPaths.isEmpty() ? Collections.emptyList() : allPaths.get(0);
        return new DFSResult(primaryPath, allPaths, visitedOrder, steps, !allPaths.isEmpty());
    }

    private static void dfsRecursive(Graph graph, String current, String destination,
                                     Set<String> visited, List<String> currentPath,
                                     List<String> visitedOrder, List<DFSStep> steps,
                                     List<List<String>> allPaths, int[] stepCounter, int maxPaths) {
        if (allPaths.size() >= maxPaths) {
            return;
        }

        if (current.equalsIgnoreCase(destination)) {
            allPaths.add(new ArrayList<>(currentPath));
            steps.add(new DFSStep(stepCounter[0]++, current, new ArrayList<>(currentPath),
                    "Target destination '" + destination + "' found! Path recorded: " + currentPath));
            return;
        }

        for (Road road : graph.getNeighbors(current)) {
            String neighbor = road.getDestination();
            if (!visited.contains(neighbor)) {
                visited.add(neighbor);
                visitedOrder.add(neighbor);
                currentPath.add(neighbor);

                steps.add(new DFSStep(stepCounter[0]++, neighbor, new ArrayList<>(currentPath),
                        "Advancing deeper into vertex: " + neighbor));

                dfsRecursive(graph, neighbor, destination, visited, currentPath, visitedOrder, steps, allPaths, stepCounter, maxPaths);

                // Backtrack
                currentPath.remove(currentPath.size() - 1);
                visited.remove(neighbor);

                steps.add(new DFSStep(stepCounter[0]++, current, new ArrayList<>(currentPath),
                        "Backtracking from vertex: " + neighbor + " to " + current));
            }
        }
    }
}
