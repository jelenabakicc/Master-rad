package com.example.vizualizatorsortiranja.algorithms;

import java.util.ArrayList;
import java.util.List;

import com.example.vizualizatorsortiranja.models.PerformanceResult;
import com.example.vizualizatorsortiranja.models.SortStep;

/**
 * Quick Sort implementation.
 * Time Complexity: O(n log n) average, O(n^2) worst case
 */
public class QuickSort implements SortingAlgorithm {

    @Override
    public List<SortStep> sortWithSteps(int[] array) {
        List<SortStep> steps = new ArrayList<>();
        int[] arr = array.clone();
        quickSortHelper(arr, 0, arr.length - 1, steps);
        steps.add(new SortStep(arr.clone(), -1, -1, SortStep.StepType.COMPLETE));
        return steps;
    }

    private void quickSortHelper(int[] arr, int low, int high, List<SortStep> steps) {
        if (low < high) {
            int pi = partition(arr, low, high, steps);
            quickSortHelper(arr, low, pi - 1, steps);
            quickSortHelper(arr, pi + 1, high, steps);
        }
    }

    private int partition(int[] arr, int low, int high, List<SortStep> steps) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            steps.add(new SortStep(arr.clone(), j, high, SortStep.StepType.COMPARE));

            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;

                if (i != j) {
                    steps.add(new SortStep(arr.clone(), i, j, SortStep.StepType.SWAP));
                }
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        steps.add(new SortStep(arr.clone(), i + 1, high, SortStep.StepType.SWAP));
        return i + 1;
    }

    @Override
    public long sortForPerformance(int[] array) {
        long startTime = System.nanoTime();
        quickSortPerformance(array, 0, array.length - 1);
        return System.nanoTime() - startTime;
    }

    private void quickSortPerformance(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partitionPerformance(arr, low, high);
            quickSortPerformance(arr, low, pi - 1);
            quickSortPerformance(arr, pi + 1, high);
        }
    }

    private int partitionPerformance(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    @Override
    public String getName() {
        return "Quick Sort";
    }

    @Override
    public PerformanceResult sortForPerformanceWithSwaps(int[] array) {
        long startTime = System.nanoTime();
        int[] swapCounter = {0}; // Use array to pass by reference
        int[] comparisonCounter = {0};
        quickSortPerformanceWithSwaps(array, 0, array.length - 1, swapCounter, comparisonCounter);
        long executionTime = System.nanoTime() - startTime;
        return new PerformanceResult(executionTime, swapCounter[0], comparisonCounter[0]);
    }

    private void quickSortPerformanceWithSwaps(int[] arr, int low, int high, int[] swapCounter, int[] comparisonCounter) {
        if (low < high) {
            int pi = partitionPerformanceWithSwaps(arr, low, high, swapCounter, comparisonCounter);
            quickSortPerformanceWithSwaps(arr, low, pi - 1, swapCounter, comparisonCounter);
            quickSortPerformanceWithSwaps(arr, pi + 1, high, swapCounter, comparisonCounter);
        }
    }

    private int partitionPerformanceWithSwaps(int[] arr, int low, int high, int[] swapCounter, int[] comparisonCounter) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            comparisonCounter[0]++;
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                swapCounter[0]++;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        swapCounter[0]++;

        return i + 1;
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }
}
