package com.example.vizualizatorsortiranja.models;

/**
 * Holds performance metrics for sorting algorithms.
 */
public class PerformanceResult {
    private final long executionTime; // in nanoseconds
    private final int swapCount;
    private final int comparisonCount;

    public PerformanceResult(long executionTime, int swapCount, int comparisonCount) {
        this.executionTime = executionTime;
        this.swapCount = swapCount;
        this.comparisonCount = comparisonCount;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public int getSwapCount() {
        return swapCount;
    }

    public int getComparisonCount() {
        return comparisonCount;
    }
}
