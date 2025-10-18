package com.example.vizualizatorsortiranja.algorithms;

import java.util.ArrayList;
import java.util.List;

import com.example.vizualizatorsortiranja.models.PerformanceResult;
import com.example.vizualizatorsortiranja.models.SortStep;

/**
 * Selection Sort implementation.
 * Time Complexity: O(n^2)
 */
public class SelectionSort implements SortingAlgorithm {

    @Override
    public List<SortStep> sortWithSteps(int[] array) {
        List<SortStep> steps = new ArrayList<>();
        int n = array.length;
        int[] arr = array.clone();

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < n; j++) {
                steps.add(new SortStep(arr.clone(), minIndex, j, SortStep.StepType.COMPARE, minIndex, true));

                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;

                steps.add(new SortStep(arr.clone(), i, minIndex, SortStep.StepType.SWAP, minIndex, true));
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
            int minIndex = i;

            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                int temp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = temp;
            }
        }

        return System.nanoTime() - startTime;
    }

    @Override
    public String getName() {
        return "Selection Sort";
    }

    @Override
    public PerformanceResult sortForPerformanceWithSwaps(int[] array) {
        long startTime = System.nanoTime();
        int n = array.length;
        int swapCount = 0;
        int comparisonCount = 0;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < n; j++) {
                comparisonCount++;
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                int temp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = temp;
                swapCount++;
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
