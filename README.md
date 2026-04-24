# Longest Common Subsequence Analysis

This project implements and analyzes two different approaches for solving the Longest Common Subsequence (LCS) problem: an efficient dynamic programming solution and a brute force recursive approach. The implementation compares DNA sequences using both algorithms with comprehensive performance metrics including comparison counts and execution timing.

## 📚 Overview

The Longest Common Subsequence problem is fundamental in bioinformatics with critical applications in sequence alignment, genome comparison, and evolutionary studies. This project provides a complete implementation of both polynomial-time (dynamic programming) and exponential-time (brute force) solutions, analyzing their performance characteristics across various sequence sizes from 2 to 60 characters.

The program processes DNA sequence pairs, computes their LCS using both algorithms where feasible, and generates detailed output files with performance metrics, validation results, and asymptotic analysis.

## 🛠️ Requirements

- Java 17 or higher
- IntelliJ IDEA or command line tools
- No external libraries required (uses only standard Java libraries)

## 📁 File Structure

```text
Project3/
├── 📄 README.md                    # Project documentation and execution instructions
├── 📁 src/                         # Source code directory
│   ├── LCSDriver.java              # Main driver class handling I/O and coordination
│   ├── LongestCommonSubsequence.java # Dynamic programming implementation
│   └── BruteForceLCS.java          # Brute force recursive implementation
├── 📁 out/                         # Compiled Java classes
│   ├── LCSDriver.class
│   ├── LongestCommonSubsequence.class
│   └── BruteForceLCS.class
├── 📄 input.txt                    # Primary test input (sequences: 29, 28, 40, 16 chars)
├── 📄 asymptotic_2.txt             # Small sequences for asymptotic analysis (2 chars)
├── 📄 asymptotic_5.txt             # Small sequences (5 chars)
├── 📄 asymptotic_8.txt             # Medium sequences (8 chars)
├── 📄 asymptotic_12.txt            # Medium sequences (12 chars)
├── 📄 asymptotic_25.txt            # Large sequences (25 chars)
├── 📄 asymptotic_60.txt            # Very large sequences (60 chars)
├── 📄 error_single_seq.txt         # Error testing - single sequence only
├── 📄 error_malformed.txt          # Error testing - malformed input
├── 📄 error_empty.txt              # Error testing - empty input
├── 📄 output.txt                   # Primary output for input.txt
├── 📄 2_out.txt                    # Output for asymptotic_2.txt
├── 📄 5_out.txt                    # Output for asymptotic_5.txt
├── 📄 8_out.txt                    # Output for asymptotic_8.txt
├── 📄 12_out.txt                   # Output for asymptotic_12.txt
├── 📄 25_out.txt                   # Output for asymptotic_25.txt
├── 📄 60_out.txt                   # Output for asymptotic_60.txt
├── 📄 single_seq_out.txt           # Error case output
├── 📄 malformed_out.txt            # Malformed input output
└── 📄 Project 3.iml                # IntelliJ project configuration
```
## ▶️ How To Run
Using IntelliJ IDEA
1. Open the project in IntelliJ IDEA

2. Ensure JDK 17+ is configured in Project Structure

3. Navigate to Run → Edit Configurations

4. Add Application configuration with main class LCSDriver

5. Set Program Arguments for different test cases:

- Primary analysis: input.txt

- Asymptotic analysis: asymptotic_2.txt asymptotic_5.txt asymptotic_8.txt asymptotic_12.txt asymptotic_25.txt asymptotic_60.txt

- Error testing: error_single_seq.txt error_malformed.txt error_empty.txt

6. Apply and run the configuration


```bash
# Navigate to project directory
cd /path/to/Project3

# Compile all source files
javac -d out src/*.java

# Run with different input files
java -cp out LCSDriver input.txt output.txt
java -cp out LCSDriver asymptotic_2.txt 2_out.txt
java -cp out LCSDriver asymptotic_5.txt 5_out.txt
java -cp out LCSDriver asymptotic_8.txt 8_out.txt
java -cp out LCSDriver asymptotic_12.txt 12_out.txt
java -cp out LCSDriver asymptotic_25.txt 25_out.txt
java -cp out LCSDriver asymptotic_60.txt 60_out.txt
java -cp out LCSDriver error_single_seq.txt single_seq_out.txt
java -cp out LCSDriver error_malformed.txt malformed_out.txt
java -cp out LCSDriver error_empty.txt empty_out.txt


```
## 🔧 Implementation Details
- Algorithms Implemented

| Algorithm           | Time Complexity | Space Complexity | Practical Limit                  | Comparison Count   |
| ------------------- | --------------- | ---------------- | -------------------------------- | ------------------ |
| Dynamic Programming | O(m×n)          | O(m×n)           | approximately ten thousand chars | exactly m×n        |
| Brute Force         | O(2^(m+n))      | O(m+n)           | approximately fifteen chars      | exponential growth |




- Features
Dual Algorithm Implementation: Both dynamic programming and brute force solutions

Comprehensive Metrics: Execution time, exact comparison counts, LCS validation

Input Validation: Strict DNA sequence validation (A, C, G, T characters only)

Error Handling: Robust handling of malformed input, file errors, and edge cases

Asymptotic Analysis: Detailed performance comparison across 6 different sequence sizes

Result Verification: Cross validation between DP and brute force results

## Algorithms and Complexity
Dynamic Programming LCS:

-Time Complexity: O(m×n) for both computation and comparisons

-Space Complexity: O(m×n) for full LCS reconstruction

-Comparison Count: Exactly m×n operations in core algorithm

-Practical Limit: Handles sequences up to thousands of characters efficiently

Brute Force LCS:

-Time Complexity: O(2^(m+n)) exponential time complexity

-Space Complexity: O(m+n) for recursion stack depth

-Comparison Count: Grows exponentially with sequence length

-Practical Limit: ~15 characters due to computational explosion

Algorithm Verification:

-Both algorithms produce identical LCS results for verifiable cases

-Brute force used for validation on small sequences (≤15 chars)

-Dynamic programming maintains correctness across all sizes
## Error Handling
Missing or invalid command line arguments: Graceful exit with usage instructions

File not found or permission errors: Descriptive error messages with file path details

Malformed input files: Handles non-DNA characters, empty sequences, and formatting errors

Insufficient sequence data: Validates minimum of 2 sequences required for LCS computation

Single sequence files: Proper error reporting when only one sequence is provided

Invalid DNA characters: Identifies and reports specific invalid characters in sequences

Memory and stack limits: Prevents stack overflow in brute force with safety checks

Large sequence handling: Automatic fallback for brute force on large inputs

Output file creation errors: Handles write permissions and path issues

Error messages are descriptive and written to both console and output files for debugging

## 🧪 Test Data
The project includes comprehensive test files covering various sequence sizes and scenarios:

-input.txt: Mixed-size sequences (29, 28, 40, 16 chars) - Primary required test case

-asymptotic_2.txt: 2-character sequences - Minimal size for baseline analysis

-asymptotic_5.txt: 5-character sequences - Small scale for brute force verification

-asymptotic_8.txt: 8-character sequences - Practical brute force limit

-asymptotic_12.txt: 12-character sequences - Extreme brute force testing

-asymptotic_25.txt: 25-character sequences - Medium scale DP analysis

-asymptotic_60.txt: 60-character sequences - Large scale DP performance

-error_single_seq.txt: Single sequence only - Error case validation

-error_malformed.txt: Contains invalid DNA characters - Input validation testing

-error_empty.txt: Contains no sequence -Error of empty file test

Note: Sequence sizes range from 2 to 60 characters to demonstrate asymptotic behavior across exponential and polynomial complexity classes.
## 📊 Output Format
### Header: Program info, timestamp, input file details

### Input Sequences: Validated DNA sequences with lengths (S1, S2, etc.)

### DP Results Table:

-Sequence pairs (S1-S2, etc.)

-LCS length and truncated subsequence

-Exact m×n comparison count

-Execution time (ns)

-Validation status

### Brute Force Table (≤15 chars):

-Same metrics as DP

-Match verification with DP results

### Efficiency Analysis: Complexity explanations and practical limits

### Asymptotic Summary: Performance metrics and theoretical comparison

## 💡 Bioinformatics Relevance
### This project demonstrates Longest Common Subsequence techniques applicable to,

-DNA Sequence Alignment: Finding conserved regions between DNA sequences for evolutionary studies

-Genome Comparison: Identifying common subsequences across different organisms to establish homology

-Variant Analysis: Detecting conserved regions in mutated DNA sequences for disease research

-PCR Primer Design: Locating conserved DNA regions suitable for primer binding sites

-Gene Finding: Identifying coding regions in DNA through sequence conservation patterns


## 👨‍💻 Author
```text
Name: Anoushka A Prabhu
Course: Algorithms for Bioinformatics EN.605.620.81.FA25
Institution: Johns Hopkins University
Semester: Fall 2025
Submission: Project 3 - Longest Common Subsequence Analysis 
```
