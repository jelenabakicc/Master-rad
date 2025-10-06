package com.example.vizualizatorsortiranja.algorithms;

import java.util.List;

import com.example.vizualizatorsortiranja.models.PerformanceResult;
import com.example.vizualizatorsortiranja.models.SortStep;

/**
 * Base interface for all sorting algorithms.
 * Implementations should provide both visualization steps and performance metrics.
 */
public interface SortingAlgorithm {

    /**
     * Sorts the array and returns a list of steps for visualization.
     * @param array The array to sort
     * @return List of sort steps showing each operation
     */
    List<SortStep> sortWithSteps(int[] array);

    /**
     * Sorts the array for performance measurement (no step tracking).
     * @param array The array to sort
     * @return Time taken in nanoseconds
     */
    long sortForPerformance(int[] array);

    /**
     * Sorts the array for performance measurement with swap counting.
     * @param array The array to sort
     * @return PerformanceResult containing execution time and swap count
     */
    PerformanceResult sortForPerformanceWithSwaps(int[] array);

    /**
     * Gets the name of the algorithm.
     * @return Algorithm name
     */
    String getName();

    /**
     * Gets the theoretical time complexity.
     * @return Time complexity string (e.g., "O(n^2)", "O(n log n)")
     */
    String getTimeComplexity();
}
