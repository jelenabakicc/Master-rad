package com.example.vizualizatorsortiranja.algorithms;

import java.util.ArrayList;
import java.util.List;

import com.example.vizualizatorsortiranja.models.PerformanceResult;
import com.example.vizualizatorsortiranja.models.SortStep;

/**
 * Bubble Sort implementation.
 * Time Complexity: O(n^2)
 */
public class BubbleSort implements SortingAlgorithm {

    @Override
    public List<SortStep> sortWithSteps(int[] array) {
        List<SortStep> steps = new ArrayList<>();
        int n = array.length;
        int[] arr = array.clone();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Record comparison step
                steps.add(new SortStep(arr.clone(), j, j + 1, SortStep.StepType.COMPARE));

                if (arr[j] > arr[j + 1]) {
                    // Swap elements
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;

                    // Record swap step
                    steps.add(new SortStep(arr.clone(), j, j + 1, SortStep.StepType.SWAP));
                }
            }
        }

        steps.add(new SortStep(arr.clone(), -1, -1, SortStep.StepType.COMPLETE));
        return steps;
    }

    @Override
    public long sortForPerformance(int[] array) {
        long startTime = System.nanoTime();
        int n = array.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }

        return System.nanoTime() - startTime;
    }

    @Override
    public String getName() {
        return "Bubble Sort";
    }

    @Override
    public PerformanceResult sortForPerformanceWithSwaps(int[] array) {
        long startTime = System.nanoTime();
        int n = array.length;
        int swapCount = 0;
        int comparisonCount = 0;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                comparisonCount++;
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapCount++;
                }
            }
        }

        long executionTime = System.nanoTime() - startTime;
        return new PerformanceResult(executionTime, swapCount, comparisonCount);
    }

    @Override
    public String getTimeComplexity() {
        return "O(n²)";
    }
}
