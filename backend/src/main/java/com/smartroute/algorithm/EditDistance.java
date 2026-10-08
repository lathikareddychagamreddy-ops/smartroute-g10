package com.smartroute.algorithm;

import java.util.*;

/**
 * EditDistance - Levenshtein Dynamic Programming Algorithm Implementation
 * Computes minimum insertions, deletions, and substitutions to convert one string to another.
 * Used for location autocomplete and fuzzy search correction ("Hyderbad" -> "Hyderabad").
 */
public class EditDistance {

    /**
     * Calculates the Levenshtein Distance between two strings using Dynamic Programming.
     * Time Complexity: O(m * n)
     * Space Complexity: O(m * n)
     *
     * @param s1 First string
     * @param s2 Second string
     * @return Minimum edit operations (insertions, deletions, substitutions)
     */
    public static int calculateDistance(String s1, String s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null || s1.isEmpty()) return s2 == null ? 0 : s2.length();
        if (s2 == null || s2.isEmpty()) return s1.length();

        String str1 = s1.trim().toLowerCase();
        String str2 = s2.trim().toLowerCase();

        int m = str1.length();
        int n = str2.length();

        // dp[i][j] stores the edit distance between str1[0..i-1] and str2[0..j-1]
        int[][] dp = new int[m + 1][n + 1];

        // Base cases: converting empty string to prefix of another
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // Deleting all characters from str1
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // Inserting all characters of str2
        }

        // Fill the DP table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    // Characters match -> no new operation needed
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int insertOp = dp[i][j - 1];      // Insert
                    int deleteOp = dp[i - 1][j];      // Delete
                    int replaceOp = dp[i - 1][j - 1];  // Replace
                    dp[i][j] = 1 + Math.min(insertOp, Math.min(deleteOp, replaceOp));
                }
            }
        }

        return dp[m][n];
    }

    /**
     * Returns the full 2D DP matrix along with labels for visualizer demo.
     */
    public static DPMatrixResult getEditMatrix(String s1, String s2) {
        String str1 = (s1 == null ? "" : s1.trim().toLowerCase());
        String str2 = (s2 == null ? "" : s2.trim().toLowerCase());

        int m = str1.length();
        int n = str2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i][j - 1], Math.min(dp[i - 1][j], dp[i - 1][j - 1]));
                }
            }
        }

        return new DPMatrixResult(str1, str2, dp, dp[m][n]);
    }

    /**
     * Finds the closest matching location names for a user query.
     * Prefixes and substring matches get prioritized or reduced distance penalty.
     */
    public static List<SuggestionResult> findRankedSuggestions(String query, List<String> allLocations, int maxResults) {
        if (query == null || query.trim().isEmpty() || allLocations == null) {
            return Collections.emptyList();
        }

        String q = query.trim().toLowerCase();
        List<SuggestionResult> results = new ArrayList<>();

        for (String location : allLocations) {
            String locLower = location.toLowerCase();
            int dist = calculateDistance(q, locLower);

            // Substring bonus: if the query is a prefix or contains the word
            boolean isPrefix = locLower.startsWith(q);
            boolean isContains = locLower.contains(q);
            int adjustedScore = dist;
            if (isPrefix) {
                adjustedScore = Math.max(0, dist - 3);
            } else if (isContains) {
                adjustedScore = Math.max(0, dist - 1);
            }

            // Reasonable threshold depending on query length
            int threshold = Math.max(3, q.length() / 2 + 2);
            if (dist <= threshold || isContains) {
                results.add(new SuggestionResult(location, dist, adjustedScore));
            }
        }

        results.sort((a, b) -> {
            if (a.adjustedScore != b.adjustedScore) {
                return Integer.compare(a.adjustedScore, b.adjustedScore);
            }
            return Integer.compare(a.distance, b.distance);
        });

        if (results.size() > maxResults) {
            return results.subList(0, maxResults);
        }
        return results;
    }

    public static class SuggestionResult {
        private String location;
        private int distance;
        private int adjustedScore;

        public SuggestionResult(String location, int distance, int adjustedScore) {
            this.location = location;
            this.distance = distance;
            this.adjustedScore = adjustedScore;
        }

        public String getLocation() { return location; }
        public int getDistance() { return distance; }
        public int getAdjustedScore() { return adjustedScore; }
    }

    public static class DPMatrixResult {
        private String str1;
        private String str2;
        private int[][] matrix;
        private int totalDistance;

        public DPMatrixResult(String str1, String str2, int[][] matrix, int totalDistance) {
            this.str1 = str1;
            this.str2 = str2;
            this.matrix = matrix;
            this.totalDistance = totalDistance;
        }

        public String getStr1() { return str1; }
        public String getStr2() { return str2; }
        public int[][] getMatrix() { return matrix; }
        public int getTotalDistance() { return totalDistance; }
    }
}
