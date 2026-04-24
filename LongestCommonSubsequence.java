/**
 * Project 3 – Longest Common Subsequence Performance Analysis
 *
 * LongestCommonSubsequence class implementing the dynamic programming solution
 * for computing the longest common subsequence of two input strings. This class
 * provides the efficient O(m×n) algorithm for LCS computation with comprehensive
 * performance tracking and error handling.
 *
 * Responsibilities
 * • Implements the standard dynamic programming algorithm for LCS computation
 * • Tracks exact comparison counts for empirical complexity analysis
 * • Measures execution time in nanoseconds for performance evaluation
 * • Provides robust error handling for memory constraints and invalid inputs
 * • Implements both full LCS reconstruction and space-optimized length computation
 * • Maintains accurate performance metrics without counting reconstruction steps
 * • Handles edge cases including empty strings and null inputs gracefully
 * • Provides memory-efficient alternative for large sequence analysis
 *
 * Author: Anoushka A Prabhu
 * Version: 1.1
 * Course: 605.620 Algorithms for Bioinformatics
 * Semester: First
 */
public class LongestCommonSubsequence {
    // Performance tracking variables
    private long comparisonCount;  // Tracks character comparisons during DP table construction
    private long startTime;        // Start timestamp for execution timing in nanoseconds
    private long endTime;          // End timestamp for execution timing in nanoseconds

    /**
     * Default constructor initializes performance counters to zero state
     */
    public LongestCommonSubsequence() {
        this.comparisonCount = 0;
    }

    /**
     * Resets all performance counters to prepare for new computation
     */
    public void resetCounters() {
        comparisonCount = 0;
        startTime = 0;
        endTime = 0;
    }

    /**
     * Returns the total number of character comparisons performed in last computation
     * @return long representing total comparisons made during DP table construction
     */
    public long getComparisonCount() {
        return comparisonCount;
    }

    /**
     * Calculates and returns the total execution time for the last LCS computation
     * @return long representing execution time in nanoseconds
     */
    public long getExecutionTime() {
        return endTime - startTime;
    }

    /**
     * CORRECTED: Computes LCS using dynamic programming with input validation
     * Implements the standard O(m×n) algorithm with exact comparison counting
     * @param s1 first input string for LCS computation
     * @param s2 second input string for LCS computation
     * @return String containing the longest common subsequence found
     * @throws IllegalArgumentException if input strings are null or too large
     * @throws RuntimeException if memory constraints are exceeded during computation
     */
    public String computeLCS(String s1, String s2) {
        // Input validation - ensure inputs are valid before processing
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("Input strings cannot be null");
        }

        // Handle empty string edge case - empty strings have empty LCS
        if (s1.isEmpty() || s2.isEmpty()) {
            return ""; // Empty strings have empty LCS
        }

        // Reset performance counters for new computation
        resetCounters();
        // Record start time for performance measurement
        startTime = System.nanoTime();

        try {
            int m = s1.length();
            int n = s2.length();

            // Check for reasonable size limits to prevent excessive memory usage
            // 10,000x10,000 DP table would require ~400MB memory
            if (m > 10000 || n > 10000) {
                throw new IllegalArgumentException("Input strings too large for efficient computation: " +
                        m + "x" + n + ". Consider using optimized version.");
            }

            // Create DP table with dimensions (m+1) x (n+1)
            // dp[i][j] stores LCS length for s1[0..i-1] and s2[0..j-1]
            int[][] dp = new int[m + 1][n + 1];

            // Build DP table - COUNT ONLY MAIN LOOP COMPARISONS
            // This loop executes exactly m×n times, giving precise comparison count
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    comparisonCount++; // This gives exactly m × n comparisons
                    if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                        // Characters match - extend LCS from previous diagonal
                        dp[i][j] = dp[i - 1][j - 1] + 1;
                    } else {
                        // Characters don't match - take maximum of left or top cell
                        dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                    }
                }
            }

            // Reconstruct LCS - DO NOT COUNT THESE COMPARISONS
            // Reconstruction is O(m+n) and not part of core algorithm complexity
            String lcs = reconstructLCS(s1, s2, dp);
            endTime = System.nanoTime();
            return lcs;

        } catch (OutOfMemoryError e) {
            // Handle memory exhaustion from large DP table allocation
            endTime = System.nanoTime();
            throw new RuntimeException("Out of memory computing LCS for strings of length " +
                    s1.length() + " and " + s2.length(), e);
        } catch (Exception e) {
            // Handle any other computation errors
            endTime = System.nanoTime();
            throw new RuntimeException("Error computing LCS: " + e.getMessage(), e);
        }
    }

    /**
     * CORRECTED: LCS reconstruction without counting comparisons
     * Backtraces through the DP table to reconstruct the actual LCS string
     * @param s1 first input string used in computation
     * @param s2 second input string used in computation
     * @param dp populated dynamic programming table with LCS lengths
     * @return String containing the reconstructed longest common subsequence
     */
    private String reconstructLCS(String s1, String s2, int[][] dp) {
        StringBuilder lcs = new StringBuilder();
        int i = s1.length();
        int j = s2.length();

        // Backtrack from bottom-right to top-left of DP table
        while (i > 0 && j > 0) {
            // NO comparisonCount++ here - reconstruction is not part of core algorithm cost
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                // Characters match - add to LCS and move diagonally
                lcs.insert(0, s1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                // Move upward in DP table (skip character in s1)
                i--;
            } else {
                // Move leftward in DP table (skip character in s2)
                j--;
            }
        }

        return lcs.toString();
    }

    /**
     * Space-optimized version for just the length with error handling
     * Uses O(min(m,n)) space instead of O(m×n) by storing only two rows
     * @param s1 first input string for LCS length computation
     * @param s2 second input string for LCS length computation
     * @return int representing the length of the longest common subsequence
     * @throws IllegalArgumentException if input strings are null
     * @throws RuntimeException if computation encounters errors
     */
    public int computeLCSLength(String s1, String s2) {
        // Input validation - ensure inputs are valid before processing
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("Input strings cannot be null");
        }

        // Handle empty string edge case
        if (s1.isEmpty() || s2.isEmpty()) {
            return 0;
        }

        // Reset performance counters for new computation
        resetCounters();
        // Record start time for performance measurement
        startTime = System.nanoTime();

        try {
            int m = s1.length();
            int n = s2.length();
            // Use only 2 rows to optimize space usage - O(min(m,n)) space complexity
            int[][] dp = new int[2][n + 1];

            // Build DP table using space-optimized approach
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    comparisonCount++; // Count comparisons same as full version
                    if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                        // Characters match - extend LCS from previous diagonal
                        dp[i % 2][j] = dp[(i - 1) % 2][j - 1] + 1;
                    } else {
                        // Characters don't match - take maximum of left or top cell
                        dp[i % 2][j] = Math.max(dp[(i - 1) % 2][j], dp[i % 2][j - 1]);
                    }
                }
            }

            endTime = System.nanoTime();
            // Return LCS length from bottom-right corner of effective DP table
            return dp[m % 2][n];

        } catch (Exception e) {
            // Handle computation errors
            endTime = System.nanoTime();
            throw new RuntimeException("Error computing LCS length: " + e.getMessage(), e);
        }
    }
}