/**
 * Project 3 – Longest Common Subsequence Performance Analysis
 *
 * LCSDriver class serving as the main controller and coordinator for
 * Longest Common Subsequence computation and analysis. This class manages
 * file I/O, sequence validation, algorithm execution, and result generation
 * for comprehensive performance comparison between dynamic programming and
 * brute force approaches.
 *
 * Responsibilities
 * • Coordinates the complete LCS analysis pipeline from input to output
 * • Manages file processing for single and multiple input files
 * • Validates DNA sequence integrity and format compliance
 * • Executes both dynamic programming and brute force LCS algorithms
 * • Generates comprehensive performance reports and asymptotic analysis
 * • Handles error conditions and provides informative error messages
 * • Produces formatted output with detailed efficiency metrics
 * • Maintains processing statistics and generates summary reports
 *
 * Author: Anoushka A Prabhu
 * Version: 1.1
 * Course: 605.620 Algorithms for Bioinformatics
 * Semester: First
 */
import java.io.*;
import java.util.*;

public class LCSDriver {

    // Algorithm solvers for LCS computation
    private LongestCommonSubsequence dpSolver;      // Dynamic programming solver for efficient LCS
    private BruteForceLCS bruteForceSolver;         // Brute force solver for validation on small inputs
    private List<String> processedFiles;            // Tracks successfully processed files for comprehensive analysis

    /**
     * Default constructor initializes algorithm solvers and processing tracker
     */
    public LCSDriver() {
        this.dpSolver = new LongestCommonSubsequence();
        this.bruteForceSolver = new BruteForceLCS();
        this.processedFiles = new ArrayList<>();
    }

    /**
     * Main entry point for LCS analysis application
     * Processes command line arguments and coordinates file processing
     * @param args command line arguments specifying input files and optional output files
     */
    public static void main(String[] args) {
        // Validate that command line arguments are provided
        if (args.length == 0) {
            System.err.println("Error: No arguments provided.");
            printUsage();
            System.exit(1);
        }

        LCSDriver driver = new LCSDriver();

        try {
            // Route to appropriate processing method based on argument count
            if (args.length == 2) {
                driver.processSingleFile(args[0], args[1]);
            } else if (args.length == 1) {
                String inputFile = args[0];
                String outputFile = generateOutputFilename(inputFile);
                driver.processSingleFile(inputFile, outputFile);
            } else {
                driver.processMultipleFiles(args);
            }

            // Generate comprehensive asymptotic analysis if multiple files processed
            if (driver.processedFiles.size() > 1) {
                driver.generateComprehensiveAnalysis();
            }
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Processes a single input file through the complete LCS analysis pipeline
     * @param inputFilename path to the input file containing DNA sequences
     * @param outputFilename path to the output file for results
     */
    public void processSingleFile(String inputFilename, String outputFilename) {
        System.out.println("Processing: " + inputFilename + " -> " + outputFilename);

        // Validate input filename is not null or empty
        if (inputFilename == null || inputFilename.trim().isEmpty()) {
            System.err.println("Error: Input filename cannot be null or empty");
            return;
        }

        try {
            // Read and parse sequences from input file
            List<String> sequences = readSequences(inputFilename);

            // Filter out invalid DNA sequences and report errors
            List<String> validSequences = filterValidSequences(sequences, inputFilename);

            if (validSequences.isEmpty()) {
                System.err.println("Error: No valid DNA sequences found in " + inputFilename);
                return;
            }

            // Validate that at least 2 sequences are available for LCS computation
            if (validSequences.size() < 2) {
                System.err.println("Error: Need at least 2 valid DNA sequences in " + inputFilename +
                        " (found " + validSequences.size() + ")");
                return;
            }

            // Write comprehensive results to output file
            try (PrintWriter output = new PrintWriter(new FileWriter(outputFilename))) {
                writeResults(validSequences, output, inputFilename);
            }

            // Track successfully processed file for comprehensive analysis
            processedFiles.add(inputFilename);
            System.out.println("✓ Completed: " + outputFilename);

        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found: " + inputFilename);
        } catch (SecurityException e) {
            System.err.println("Error: Access denied to file: " + inputFilename);
        } catch (IOException e) {
            System.err.println("Error processing " + inputFilename + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error processing " + inputFilename + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Processes multiple input files in batch mode
     * @param inputFilenames array of input file paths to process
     */
    public void processMultipleFiles(String[] inputFilenames) {
        System.out.println("Processing " + inputFilenames.length + " files...");

        int successCount = 0;
        int errorCount = 0;

        // Process each .txt file that doesn't start with "output_"
        for (String inputFilename : inputFilenames) {
            if (inputFilename.endsWith(".txt") && !inputFilename.startsWith("output_")) {
                String outputFilename = generateOutputFilename(inputFilename);
                try {
                    processSingleFile(inputFilename, outputFilename);
                    successCount++;
                } catch (Exception e) {
                    errorCount++;
                    System.err.println("Failed to process: " + inputFilename);
                }
            }
        }

        System.out.println("✓ Processing complete: " + successCount + " successful, " + errorCount + " failed");
    }

    /**
     * Generate comprehensive analysis across all processed files
     * Creates a summary report with asymptotic performance data
     */
    public void generateComprehensiveAnalysis() {
        String comprehensiveOutput = "output_comprehensive_analysis.txt";
        try (PrintWriter output = new PrintWriter(new FileWriter(comprehensiveOutput))) {
            output.println("COMPREHENSIVE ASYMPTOTIC ANALYSIS");
            output.println("==================================");
            output.println("Generated on: " + new Date());
            output.println();
            output.println("Based on analysis of " + processedFiles.size() + " input files");
            output.println();

            writeComprehensiveAsymptoticTable(output);

            System.out.println("✓ Comprehensive analysis: " + comprehensiveOutput);
        } catch (IOException e) {
            System.err.println("Error generating comprehensive analysis: " + e.getMessage());
        }
    }

    /**
     * Generates output filename based on input filename convention
     * @param inputFilename the input file name to transform
     * @return generated output filename with "output_" prefix
     */
    private static String generateOutputFilename(String inputFilename) {
        if (inputFilename.endsWith(".txt")) {
            String baseName = inputFilename.substring(0, inputFilename.length() - 4);
            return "output_" + baseName + ".txt";
        } else {
            return "output_" + inputFilename + ".txt";
        }
    }

    /**
     * Displays usage instructions for command line operation
     */
    private static void printUsage() {
        System.err.println("Usage examples:");
        System.err.println("  Single file: java LCSDriver input.txt output.txt");
        System.err.println("  Auto-output: java LCSDriver input.txt");
        System.err.println("  Multiple:    java LCSDriver file1.txt file2.txt file3.txt");
        System.err.println("  All TXT:     java LCSDriver *.txt");
        System.err.println();
        System.err.println("Note: Input files should contain DNA sequences (A, C, G, T characters only)");
    }

    /**
     * Validates if a string contains only valid DNA characters
     * @param sequence the string to validate as DNA sequence
     * @return true if sequence contains only A, C, G, T characters (case insensitive)
     */
    private boolean isValidDNA(String sequence) {
        return sequence != null && sequence.matches("[ACGTacgt]*");
    }

    /**
     * Converts sequence to uppercase and validates
     * @param sequence the DNA sequence to normalize
     * @return uppercase version of the sequence, or empty string if null
     */
    private String normalizeDNA(String sequence) {
        if (sequence == null) return "";
        return sequence.toUpperCase();
    }

    /**
     * Filters out invalid DNA sequences and reports errors
     * @param sequences list of raw sequences to filter
     * @param filename source filename for error reporting
     * @return list of valid DNA sequences in uppercase format
     */
    private List<String> filterValidSequences(List<String> sequences, String filename) {
        List<String> validSequences = new ArrayList<>();
        int invalidCount = 0;

        for (int i = 0; i < sequences.size(); i++) {
            String seq = sequences.get(i);
            if (seq == null || seq.trim().isEmpty()) {
                System.err.println("Warning: Empty sequence S" + (i+1) + " in " + filename + " - skipping");
                invalidCount++;
            } else if (isValidDNA(seq)) {
                validSequences.add(normalizeDNA(seq));
            } else {
                invalidCount++;
                System.err.println("Error: Invalid DNA sequence S" + (i+1) + " in " + filename + ": " + seq);
                System.err.println("       Only A, C, G, T characters are allowed (case insensitive).");
                System.err.println("       Invalid characters found: " + getInvalidChars(seq));
            }
        }

        if (invalidCount > 0) {
            System.err.println("Warning: " + invalidCount + " invalid sequences skipped in " + filename);
        }

        return validSequences;
    }

    /**
     * Extracts invalid characters from a sequence for error reporting
     * @param sequence the DNA sequence to analyze
     * @return string listing invalid characters found in the sequence
     */
    private String getInvalidChars(String sequence) {
        StringBuilder invalid = new StringBuilder();
        for (char c : sequence.toCharArray()) {
            if (!Character.toString(c).matches("[ACGTacgt]")) {
                if (invalid.length() > 0) invalid.append(", ");
                invalid.append("'").append(c).append("'");
            }
        }
        return invalid.toString();
    }

    /**
     * Reads sequences from input file with comprehensive error checking
     * @param filename the file to read sequences from
     * @return list of raw sequences extracted from the file
     * @throws IOException if file cannot be read or is invalid
     */
    private List<String> readSequences(String filename) throws IOException {
        List<String> sequences = new ArrayList<>();
        File inputFile = new File(filename);

        // Validate file existence and accessibility
        if (!inputFile.exists()) {
            throw new FileNotFoundException("Input file not found: " + inputFile.getAbsolutePath());
        }

        if (inputFile.length() == 0) {
            throw new IOException("Input file is empty: " + filename);
        }

        if (!inputFile.canRead()) {
            throw new SecurityException("Cannot read input file: " + filename);
        }

        // Read and parse file line by line
        try (BufferedReader input = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int lineNumber = 0;

            while ((line = input.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                // Process non-empty, non-comment lines
                if (!line.isEmpty() && !line.startsWith("#")) {
                    String sequence = extractSequence(line, lineNumber, filename);
                    if (sequence != null && !sequence.isEmpty()) {
                        sequences.add(sequence);
                    }
                }
            }
        }

        return sequences;
    }

    /**
     * Extracts sequence from a line, handling "key=value" format or raw sequence
     * @param line the line to extract sequence from
     * @param lineNumber current line number for error reporting
     * @param filename source filename for error reporting
     * @return extracted sequence string, or null if malformed
     */
    private String extractSequence(String line, int lineNumber, String filename) {
        String sequence;

        if (line.contains("=")) {
            String[] parts = line.split("=", 2); // Split into 2 parts maximum
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                System.err.println("Warning: Malformed line " + lineNumber + " in " + filename + ": " + line);
                return null;
            }
            sequence = parts[1].trim();
        } else {
            sequence = line;
        }

        return sequence;
    }

    /**
     * Writes comprehensive LCS analysis results to output stream
     * @param sequences list of valid DNA sequences to analyze
     * @param output PrintWriter for writing results
     * @param inputFilename source filename for reporting
     */
    private void writeResults(List<String> sequences, PrintWriter output, String inputFilename) {
        output.println("LONGEST COMMON SUBSEQUENCE ANALYSIS");
        output.println("====================================");
        output.println("Input file: " + inputFilename);
        output.println("Generated on: " + new Date());
        output.println();

        output.println("INPUT SEQUENCES:");
        output.println("----------------");
        for (int i = 0; i < sequences.size(); i++) {
            output.printf("S%d: %s%n", i + 1, sequences.get(i));
            output.printf("S%d Length: %d%n", i + 1, sequences.get(i).length());
        }
        output.println();

        List<SequencePair> pairs = generatePairs(sequences);
        List<SequencePair> smallPairs = getSmallPairs(pairs);

        output.println("DYNAMIC PROGRAMMING RESULTS:");
        output.println("============================");
        output.printf("%-10s %-8s %-12s %-12s %-20s%n",
                "Pair", "LCS Len", "Comparisons", "Time (ns)", "LCS");
        output.println("----------------------------------------------------------------------");

        int invalidResults = 0;
        for (SequencePair pair : pairs) {
            try {
                String lcs = dpSolver.computeLCS(pair.seq1, pair.seq2);
                long comparisons = dpSolver.getComparisonCount();
                long time = dpSolver.getExecutionTime();

                boolean valid = verifyLCS(pair.seq1, pair.seq2, lcs);
                String status = valid ? "" : " [INVALID]";
                if (!valid) invalidResults++;

                output.printf("%-10s %-8d %-12d %-12d %-20s%s%n",
                        pair.getLabel(), lcs.length(), comparisons, time,
                        truncate(lcs, 15), status);
            } catch (Exception e) {
                output.printf("%-10s %-8s %-12s %-12s %-20s%n",
                        pair.getLabel(), "ERROR", "ERROR", "ERROR", "COMPUTATION_FAILED");
                System.err.println("Error computing LCS for " + pair.getLabel() + ": " + e.getMessage());
            }
        }

        if (!smallPairs.isEmpty()) {
            output.println();
            output.println("BRUTE FORCE RESULTS (SMALL PAIRS):");
            output.println("===================================");
            output.printf("%-10s %-8s %-12s %-12s %-20s%n",
                    "Pair", "LCS Len", "Comparisons", "Time (ns)", "LCS");
            output.println("----------------------------------------------------------------------");

            for (SequencePair pair : smallPairs) {
                try {
                    String lcs = bruteForceSolver.computeLCS(pair.seq1, pair.seq2);
                    long comparisons = bruteForceSolver.getComparisonCount();
                    long time = bruteForceSolver.getExecutionTime();

                    String dpLCS = dpSolver.computeLCS(pair.seq1, pair.seq2);
                    boolean matches = lcs.equals(dpLCS);
                    String status = matches ? " [MATCH]" : " [DIFF]";

                    output.printf("%-10s %-8d %-12d %-12d %-20s%s%n",
                            pair.getLabel(), lcs.length(), comparisons, time,
                            truncate(lcs, 15), status);
                } catch (Exception e) {
                    output.printf("%-10s %-8s %-12s %-12s %-20s%n",
                            pair.getLabel(), "ERROR", "ERROR", "ERROR", "COMPUTATION_FAILED");
                    System.err.println("Error in brute force for " + pair.getLabel() + ": " + e.getMessage());
                }
            }
        }

        // Enhanced efficiency analysis
        output.println();
        output.println("EFFICIENCY ANALYSIS:");
        output.println("====================");
        output.println("Dynamic Programming: O(m*n) time and space complexity");
        output.println("Brute Force: O(2^(m+n)) exponential time complexity");
        output.println("Brute Force Practical Limit: ~15 characters");
        output.println();
        output.println("Total sequence pairs analyzed: " + pairs.size());
        output.println("Small pairs suitable for brute force: " + smallPairs.size());
        if (invalidResults > 0) {
            output.println("Invalid LCS results detected: " + invalidResults);
        }

        // CORRECTED asymptotic data summary
        output.println();
        output.println("ASYMPTOTIC DATA SUMMARY:");
        output.println("========================");
        output.printf("%-12s %-12s %-15s %-15s%n",
                "String Size", "DP Time(ns)", "DP Comparisons", "Theoretical m×n");
        output.println("----------------------------------------------------------------");

        if (!pairs.isEmpty()) {
            SequencePair samplePair = pairs.get(0);
            int size = samplePair.seq1.length();

            try {
                String lcs = dpSolver.computeLCS(samplePair.seq1, samplePair.seq2);
                long comparisons = dpSolver.getComparisonCount();
                long time = dpSolver.getExecutionTime();
                long theoretical = (long) samplePair.seq1.length() * samplePair.seq2.length();

                output.printf("%-12d %-12d %-15d %-15d%n",
                        size, time, comparisons, theoretical);
            } catch (Exception e) {
                output.printf("%-12d %-12s %-15s %-15s%n",
                        size, "ERROR", "ERROR", "ERROR");
            }
        }
    }

    /**
     * Writes comprehensive asymptotic table across all processed files
     * @param output PrintWriter for writing the comprehensive analysis table
     */
    private void writeComprehensiveAsymptoticTable(PrintWriter output) {
        output.println("COMPREHENSIVE ASYMPTOTIC DATA");
        output.println("==============================");
        output.printf("%-8s %-12s %-15s %-15s %-15s %-15s%n",
                "Size", "DP Time(ns)", "DP Comparisons", "Theoretical m×n", "BF Time(ns)", "BF Comparisons");
        output.println("------------------------------------------------------------------------------------------");

        // Sort files by size for better presentation
        List<String> sortedFiles = new ArrayList<>(processedFiles);
        sortedFiles.sort((f1, f2) -> {
            int size1 = extractSizeFromFilename(f1);
            int size2 = extractSizeFromFilename(f2);
            return Integer.compare(size1, size2);
        });

        for (String filename : sortedFiles) {
            try {
                List<String> sequences = readSequences(filename);
                List<String> validSequences = filterValidSequences(sequences, filename);

                if (validSequences.size() >= 2) {
                    String s1 = validSequences.get(0);
                    String s2 = validSequences.get(1);
                    int size = s1.length();

                    // DP analysis
                    String dpLCS = dpSolver.computeLCS(s1, s2);
                    long dpTime = dpSolver.getExecutionTime();
                    long dpComparisons = dpSolver.getComparisonCount();
                    long theoretical = (long) s1.length() * s2.length();

                    // Brute force analysis (only for small sizes)
                    long bfTime = -1;
                    long bfComparisons = -1;
                    if (s1.length() <= 15 && s2.length() <= 15) {
                        String bfLCS = bruteForceSolver.computeLCS(s1, s2);
                        bfTime = bruteForceSolver.getExecutionTime();
                        bfComparisons = bruteForceSolver.getComparisonCount();
                    }

                    output.printf("%-8d %-12d %-15d %-15d %-15d %-15d%n",
                            size, dpTime, dpComparisons, theoretical, bfTime, bfComparisons);
                }
            } catch (Exception e) {
                System.err.println("Error processing " + filename + " for comprehensive analysis: " + e.getMessage());
            }
        }

        output.println();
        output.println("ANALYSIS NOTES:");
        output.println("===============");
        output.println("• DP comparisons should exactly equal Theoretical m×n");
        output.println("• BF shows exponential growth in comparisons and time");
        output.println("• Practical BF limit is around 15 characters due to O(2^(m+n)) complexity");
        output.println("• DP maintains O(m×n) polynomial complexity for all sizes");
        output.println("• Input validation: Only A, C, G, T characters accepted (case insensitive)");
    }

    /**
     * Extracts size number from filename for sorting and analysis
     * @param filename the filename to extract size from
     * @return extracted size as integer, or 0 if no number found
     */
    private int extractSizeFromFilename(String filename) {
        try {
            String numberPart = filename.replaceAll("[^0-9]", "");
            return numberPart.isEmpty() ? 0 : Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Generates all possible sequence pairs from the input sequences
     * @param sequences list of valid DNA sequences
     * @return list of SequencePair objects representing all combinations
     */
    private List<SequencePair> generatePairs(List<String> sequences) {
        List<SequencePair> pairs = new ArrayList<>();
        for (int i = 0; i < sequences.size(); i++) {
            for (int j = i + 1; j < sequences.size(); j++) {
                pairs.add(new SequencePair(i + 1, sequences.get(i), j + 1, sequences.get(j)));
            }
        }
        return pairs;
    }

    /**
     * Filters sequence pairs to only those suitable for brute force analysis
     * @param pairs all sequence pairs to filter
     * @return list of pairs where both sequences are ≤15 characters
     */
    private List<SequencePair> getSmallPairs(List<SequencePair> pairs) {
        List<SequencePair> smallPairs = new ArrayList<>();
        for (SequencePair pair : pairs) {
            if (pair.seq1.length() <= 15 && pair.seq2.length() <= 15) {
                smallPairs.add(pair);
            }
        }
        return smallPairs;
    }

    /**
     * Verifies that a candidate LCS is indeed a valid subsequence of both input strings
     * @param s1 first input string
     * @param s2 second input string
     * @param lcs candidate longest common subsequence to verify
     * @return true if lcs is a valid subsequence of both s1 and s2
     */
    private boolean verifyLCS(String s1, String s2, String lcs) {
        if (lcs == null) return false;

        // Verify lcs is subsequence of s1
        int i = 0;
        for (char c : s1.toCharArray()) {
            if (i < lcs.length() && c == lcs.charAt(i)) {
                i++;
            }
        }
        if (i != lcs.length()) {
            return false;
        }

        // Verify lcs is subsequence of s2
        i = 0;
        for (char c : s2.toCharArray()) {
            if (i < lcs.length() && c == lcs.charAt(i)) {
                i++;
            }
        }
        return i == lcs.length();
    }

    /**
     * Truncates a string to specified length with ellipsis if needed
     * @param s the string to truncate
     * @param length maximum length before truncation
     * @return original string if within length, or truncated version with "..."
     */
    private String truncate(String s, int length) {
        if (s == null) return "null";
        if (s.length() <= length) return s;
        return s.substring(0, length) + "...";
    }

    /**
     * Inner class representing a pair of sequences for LCS computation
     * Encapsulates two sequences with their identifiers for organized processing
     */
    private static class SequencePair {
        int id1;        // Identifier for first sequence
        String seq1;    // First DNA sequence
        int id2;        // Identifier for second sequence
        String seq2;    // Second DNA sequence

        /**
         * Constructs a sequence pair with identifiers and sequences
         * @param id1 identifier for first sequence
         * @param seq1 first DNA sequence
         * @param id2 identifier for second sequence
         * @param seq2 second DNA sequence
         */
        SequencePair(int id1, String seq1, int id2, String seq2) {
            this.id1 = id1;
            this.seq1 = seq1;
            this.id2 = id2;
            this.seq2 = seq2;
        }

        /**
         * Generates a label for the sequence pair (e.g., "S1-S2")
         * @return formatted pair label string
         */
        String getLabel() {
            return "S" + id1 + "-S" + id2;
        }
    }
}