package com.smartroute.algorithm;

import java.util.*;

/**
 * BFS - Breadth-First Search Algorithm Implementation
 * Explores graph layer-by-layer to find shortest unweighted path / explore connectivity.
 * Also records step-by-step traversal states for educational visualization.
 */
public class BFS {

    public static class BFSResult {
        private final List<String> path;
        private final List<Road> roadSegments;
        private final List<String> visitedOrder;
        private final List<BFSStep> steps;
        private final boolean pathFound;

        public BFSResult(List<String> path, List<Road> roadSegments, List<String> visitedOrder, List<BFSStep> steps, boolean pathFound) {
            this.path = path;
            this.roadSegments = roadSegments;
            this.visitedOrder = visitedOrder;
            this.steps = steps;
            this.pathFound = pathFound;
        }

        public List<String> getPath() { return path; }
        public List<Road> getRoadSegments() { return roadSegments; }
        public List<String> getVisitedOrder() { return visitedOrder; }
        public List<BFSStep> getSteps() { return steps; }
        public boolean isPathFound() { return pathFound; }
    }

    public static class BFSStep {
        private final int stepNumber;
        private final String currentNode;
        private final List<String> currentQueue;
        private final Set<String> visitedNodes;
        private final String action;

        public BFSStep(int stepNumber, String currentNode, List<String> currentQueue, Set<String> visitedNodes, String action) {
            this.stepNumber = stepNumber;
            this.currentNode = currentNode;
            this.currentQueue = currentQueue;
            this.visitedNodes = visitedNodes;
            this.action = action;
        }

        public int getStepNumber() { return stepNumber; }
        public String getCurrentNode() { return currentNode; }
        public List<String> getCurrentQueue() { return currentQueue; }
        public Set<String> getVisitedNodes() { return visitedNodes; }
        public String getAction() { return action; }
    }

    /**
     * Executes BFS from source to destination, recording the exact path and step sequence.
     * Time Complexity: O(V + E)
     * Space Complexity: O(V)
     */
    public static BFSResult findPath(Graph graph, String source, String destination) {
        List<String> visitedOrder = new ArrayList<>();
        List<BFSStep> steps = new ArrayList<>();

        if (graph == null || !graph.hasVertex(source) || !graph.hasVertex(destination)) {
            return new BFSResult(Collections.emptyList(), Collections.emptyList(), visitedOrder, steps, false);
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Road> parentRoadMap = new HashMap<>();

        queue.add(source);
        visited.add(source);
        visitedOrder.add(source);

        int stepCounter = 1;
        steps.add(new BFSStep(stepCounter++, source, new ArrayList<>(queue), new HashSet<>(visited),
                "Initialized BFS with source vertex: " + source));

        boolean found = false;

        while (!queue.isEmpty()) {
            String current = queue.poll();

            steps.add(new BFSStep(stepCounter++, current, new ArrayList<>(queue), new HashSet<>(visited),
                    "Dequeued and exploring vertex: " + current));

            if (current.equalsIgnoreCase(destination)) {
                found = true;
                steps.add(new BFSStep(stepCounter++, current, new ArrayList<>(queue), new HashSet<>(visited),
                        "Destination vertex '" + destination + "' reached!"));
                break;
            }

            for (Road road : graph.getNeighbors(current)) {
                String neighbor = road.getDestination();
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    visitedOrder.add(neighbor);
                    parentMap.put(neighbor, current);
                    parentRoadMap.put(neighbor, road);
                    queue.add(neighbor);

                    steps.add(new BFSStep(stepCounter++, neighbor, new ArrayList<>(queue), new HashSet<>(visited),
                            "Discovered unvisited neighbor: " + neighbor + " (enqueued)"));
                }
            }
        }

        if (!found) {
            return new BFSResult(Collections.emptyList(), Collections.emptyList(), visitedOrder, steps, false);
        }

        // Reconstruct path from destination to source
        List<String> path = new ArrayList<>();
        List<Road> roadSegments = new ArrayList<>();
        String curr = destination;
        while (curr != null) {
            path.add(0, curr);
            Road road = parentRoadMap.get(curr);
            if (road != null) {
                roadSegments.add(0, road);
            }
            curr = parentMap.get(curr);
        }

        return new BFSResult(path, roadSegments, visitedOrder, steps, true);
    }
}
