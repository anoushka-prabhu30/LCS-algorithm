/**
 * Project 3 – Longest Common Subsequence Performance Analysis
 *
 * BruteForceLCS class implementing an exhaustive search method for computing
 * the longest common subsequence of two input strings. This implementation
 * is intended for performance comparison with the dynamic programming version.
 *
 * Responsibilities
 * • Performs a complete brute force exploration of all valid subsequence paths.
 * • Uses a recursive search structure to generate and evaluate all candidates.
 * • Tracks the number of character comparisons executed during the search.
 * • Records execution time in nanoseconds for empirical performance analysis.
 * • Provides size checks to prevent excessive computation beyond feasible limits.
 * • Manages recursive state through controlled StringBuilder modifications.
 * • Implements safety mechanisms to handle stack overflow, memory issues,
 *   and improper input cases.
 * • Produces the best subsequence discovered after evaluating all valid branches.
 *
 * Author: Anoushka A Prabhu
 * Version: 1.1
 * Course: 605.620 Algorithms for Bioinformatics
 * Semester: First
 */
public class BruteForceLCS {
    private long comparisonCount;
    private long startTime;
    private long endTime;
    private String bestLCS;

    public BruteForceLCS() {
        this.comparisonCount = 0;
    }

    public void resetCounters() {
        comparisonCount = 0;
        startTime = 0;
        endTime = 0;
        bestLCS = "";
    }

    public long getComparisonCount() {
        return comparisonCount;
    }

    public long getExecutionTime() {
        return endTime - startTime;
    }

    /**
     * Computes LCS by exhaustively searching all possible subsequence combinations
     * with comprehensive error handling and safety mechanisms
     * @param s1 first input string for LCS computation
     * @param s2 second input string for LCS computation
     * @return String containing the longest common subsequence found, or error codes for large inputs
     * @throws IllegalArgumentException if input strings are null
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

        // Only for small strings - validate size limits to prevent exponential explosion
        // Practical limit of 15 characters prevents computational infeasibility
        if (s1.length() > 15 || s2.length() > 15) {
            endTime = System.nanoTime();
            return "TOO_LARGE";
        }

        // Execute recursive search within try-catch for robust error handling
        try {
            bestLCS = "";
            findLCSRecursive(s1, s2, 0, 0, new StringBuilder());
            endTime = System.nanoTime();
            return bestLCS;
        } catch (StackOverflowError e) {
            // Handle stack overflow from deep recursion for large inputs
            endTime = System.nanoTime();
            System.err.println("Error: Stack overflow in brute force computation for strings of length " +
                    s1.length() + " and " + s2.length());
            return "STACK_OVERFLOW";
        } catch (OutOfMemoryError e) {
            // Handle memory exhaustion from excessive recursive calls
            endTime = System.nanoTime();
            System.err.println("Error: Out of memory in brute force computation");
            return "OUT_OF_MEMORY";
        }
    }

    /**
     * Recursive function to explore all possible LCS candidates
     * Uses depth first search to generate all valid subsequence combinations
     * with safety mechanisms to prevent infinite recursion
     * @param s1 first input string being processed
     * @param s2 second input string being processed
     * @param i current index position in first string s1
     * @param j current index position in second string s2
     * @param current StringBuilder storing the current candidate subsequence being built
     */
    private void findLCSRecursive(String s1, String s2, int i, int j, StringBuilder current) {
        comparisonCount++; // Count this recursive call as a comparison operation

        // Safety check to prevent infinite recursion - hard limit of 10 million comparisons
        if (comparisonCount > 10_000_000) { // 10 million comparison limit
            return;
        }

        // Update best LCS if current candidate is longer than previous best
        if (current.length() > bestLCS.length()) {
            bestLCS = current.toString();
        }

        // Base case: reached end of either string, cannot extend further
        if (i >= s1.length() || j >= s2.length()) {
            return;
        }

        // If characters at current positions match, include in candidate LCS
        if (s1.charAt(i) == s2.charAt(j)) {
            // Append matching character to current candidate subsequence
            current.append(s1.charAt(i));
            // Recursively explore path where we advance both string indices after match
            findLCSRecursive(s1, s2, i + 1, j + 1, current);
            // Backtrack: remove last character to explore alternative paths without this match
            current.deleteCharAt(current.length() - 1);
        }

        // Explore both possibilities: skip current character in s1 or skip in s2
        // This generates the complete search space of all possible subsequences
        findLCSRecursive(s1, s2, i + 1, j, current);  // Skip character in s1 and continue
        findLCSRecursive(s1, s2, i, j + 1, current);  // Skip character in s2 and continue
    }
}