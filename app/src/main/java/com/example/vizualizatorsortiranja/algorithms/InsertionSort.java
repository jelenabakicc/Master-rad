package com.example.vizualizatorsortiranja.algorithms;

import java.util.ArrayList;
import java.util.List;

import com.example.vizualizatorsortiranja.models.PerformanceResult;
import com.example.vizualizatorsortiranja.models.SortStep;

/**
 * Insertion Sort implementation.
 * Time Complexity: O(n^2)
 */
public class InsertionSort implements SortingAlgorithm {

    @Override
    public List<SortStep> sortWithSteps(int[] array) {
        List<SortStep> steps = new ArrayList<>();
        int n = array.length;
        int[] arr = array.clone();

        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            int keyPosition = i;

            while (j >= 0 && arr[j] > key) {
                int[] visualArray = arr.clone();
                visualArray[keyPosition] = key;

                steps.add(new SortStep(visualArray, j, keyPosition, SortStep.StepType.COMPARE, key, keyPosition, true));

                arr[j + 1] = arr[j];
                keyPosition = j;

                int[] swapArray = arr.clone();
                swapArray[keyPosition] = key;
                steps.add(new SortStep(swapArray, keyPosition, j + 1, SortStep.StepType.SWAP, key, keyPosition, true));

                j--;
            }

            arr[keyPosition] = key;

            if (keyPosition == i - 1 && arr[i - 1] > key) {
                steps.add(new SortStep(arr.clone(), keyPosition, keyPosition, SortStep.StepType.SWAP, key, keyPosition, true));
            }
        }

        steps.add(new SortStep(arr.clone(), -1, -1, SortStep.StepType.COMPLETE));
        return steps;
    }

    @Override
    public long sortForPerformance(int[] array) {
        long startTime = System.nanoTime();
        int n = array.length;

        for (int i = 1; i < n; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }

        return System.nanoTime() - startTime;
    }

    @Override
    public String getName() {
        return "Insertion Sort";
    }

    @Override
    public PerformanceResult sortForPerformanceWithSwaps(int[] array) {
        long startTime = System.nanoTime();
        int n = array.length;
        int swapCount = 0;
        int comparisonCount = 0;

        for (int i = 1; i < n; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= 0 && array[j] > key) {
                comparisonCount++;
                array[j + 1] = array[j];
                swapCount++; // Count shifts as swaps
                j--;
            }
            if (j >= 0) {
                comparisonCount++;
            }
            array[j + 1] = key;
        }

        long executionTime = System.nanoTime() - startTime;
        return new PerformanceResult(executionTime, swapCount, comparisonCount);
    }

    @Override
    public String getTimeComplexity() {
        return "O(n²)";
    }
}
