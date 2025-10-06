package com.example.vizualizatorsortiranja.algorithms;

import java.util.ArrayList;
import java.util.List;

import com.example.vizualizatorsortiranja.models.PerformanceResult;
import com.example.vizualizatorsortiranja.models.SortStep;

/**
 * Heap Sort implementation.
 * Time Complexity: O(n log n)
 */
public class HeapSort implements SortingAlgorithm {

    @Override
    public List<SortStep> sortWithSteps(int[] array) {
        List<SortStep> steps = new ArrayList<>();
        int n = array.length;
        int[] arr = array.clone();

        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i, steps);
        }

        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            steps.add(new SortStep(arr.clone(), 0, i, SortStep.StepType.SWAP));

            // Heapify the reduced heap
            heapify(arr, i, 0, steps);
        }

        steps.add(new SortStep(arr.clone(), -1, -1, SortStep.StepType.COMPLETE));
        return steps;
    }

    private void heapify(int[] arr, int n, int i, List<SortStep> steps) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n) {
            steps.add(new SortStep(arr.clone(), largest, left, SortStep.StepType.COMPARE));
            if (arr[left] > arr[largest]) {
                largest = left;
            }
        }

        if (right < n) {
            steps.add(new SortStep(arr.clone(), largest, right, SortStep.StepType.COMPARE));
            if (arr[right] > arr[largest]) {
                largest = right;
            }
        }

        if (largest != i) {
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;

            steps.add(new SortStep(arr.clone(), i, largest, SortStep.StepType.SWAP));

            heapify(arr, n, largest, steps);
        }
    }

    @Override
    public long sortForPerformance(int[] array) {
        long startTime = System.nanoTime();
        int n = array.length;

        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyPerformance(array, n, i);
        }

        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            heapifyPerformance(array, i, 0);
        }

        return System.nanoTime() - startTime;
    }

    private void heapifyPerformance(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != i) {
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;

            heapifyPerformance(arr, n, largest);
        }
    }

    @Override
    public String getName() {
        return "Heap Sort";
    }

    @Override
    public PerformanceResult sortForPerformanceWithSwaps(int[] array) {
        long startTime = System.nanoTime();
        int n = array.length;
        int[] swapCounter = {0}; // Use array to pass by reference
        int[] comparisonCounter = {0};

        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyPerformanceWithSwaps(array, n, i, swapCounter, comparisonCounter);
        }

        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;
            swapCounter[0]++;

            heapifyPerformanceWithSwaps(array, i, 0, swapCounter, comparisonCounter);
        }

        long executionTime = System.nanoTime() - startTime;
        return new PerformanceResult(executionTime, swapCounter[0], comparisonCounter[0]);
    }

    private void heapifyPerformanceWithSwaps(int[] arr, int n, int i, int[] swapCounter, int[] comparisonCounter) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n) {
            comparisonCounter[0]++;
            if (arr[left] > arr[largest]) {
                largest = left;
            }
        }

        if (right < n) {
            comparisonCounter[0]++;
            if (arr[right] > arr[largest]) {
                largest = right;
            }
        }

        if (largest != i) {
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            swapCounter[0]++;

            heapifyPerformanceWithSwaps(arr, n, largest, swapCounter, comparisonCounter);
        }
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }
}
