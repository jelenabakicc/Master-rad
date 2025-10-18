package com.example.vizualizatorsortiranja.algorithms;

import java.util.ArrayList;
import java.util.List;

import com.example.vizualizatorsortiranja.models.PerformanceResult;
import com.example.vizualizatorsortiranja.models.SortStep;

/**
 * Merge Sort implementation.
 * Time Complexity: O(n log n)
 */
public class MergeSort implements SortingAlgorithm {

    @Override
    public List<SortStep> sortWithSteps(int[] array) {
        List<SortStep> steps = new ArrayList<>();
        int[] arr = array.clone();
        mergeSortHelper(arr, 0, arr.length - 1, steps);
        steps.add(new SortStep(arr.clone(), -1, -1, SortStep.StepType.COMPLETE));
        return steps;
    }

    private void mergeSortHelper(int[] arr, int left, int right, List<SortStep> steps) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSortHelper(arr, left, mid, steps);
            mergeSortHelper(arr, mid + 1, right, steps);
            merge(arr, left, mid, right, steps);
        }
    }

    private void merge(int[] arr, int left, int mid, int right, List<SortStep> steps) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(arr, left, leftArray, 0, n1);
        System.arraycopy(arr, mid + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            steps.add(new SortStep(arr.clone(), k, k, SortStep.StepType.COMPARE,
                                   leftArray.clone(), rightArray.clone(), i, j, left, mid + 1));

            if (leftArray[i] <= rightArray[j]) {
                arr[k] = leftArray[i];
                i++;
            } else {
                arr[k] = rightArray[j];
                j++;
            }

            steps.add(new SortStep(arr.clone(), k, k, SortStep.StepType.SWAP,
                                   leftArray.clone(), rightArray.clone(), i, j, left, mid + 1));
            k++;
        }

    while (i < n1) {
            arr[k] = leftArray[i];
            steps.add(new SortStep(arr.clone(), k, k, SortStep.StepType.SWAP,
                                   leftArray.clone(), rightArray.clone(), i, -1, left, mid + 1));
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = rightArray[j];
            steps.add(new SortStep(arr.clone(), k, k, SortStep.StepType.SWAP,
                                   leftArray.clone(), rightArray.clone(), -1, j, left, mid + 1));
            j++;
            k++;
        }
    }

    @Override
    public long sortForPerformance(int[] array) {
        long startTime = System.nanoTime();
        mergeSortPerformance(array, 0, array.length - 1);
        return System.nanoTime() - startTime;
    }

    private void mergeSortPerformance(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortPerformance(arr, left, mid);
            mergeSortPerformance(arr, mid + 1, right);
            mergePerformance(arr, left, mid, right);
        }
    }

    private void mergePerformance(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(arr, left, leftArray, 0, n1);
        System.arraycopy(arr, mid + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                arr[k++] = leftArray[i++];
            } else {
                arr[k++] = rightArray[j++];
            }
        }

        while (i < n1) {
            arr[k++] = leftArray[i++];
        }

        while (j < n2) {
            arr[k++] = rightArray[j++];
        }
    }

    @Override
    public String getName() {
        return "Merge Sort";
    }

    @Override
    public PerformanceResult sortForPerformanceWithSwaps(int[] array) {
        long startTime = System.nanoTime();
        int[] swapCounter = {0};
        int[] comparisonCounter = {0};
        mergeSortPerformanceWithSwaps(array, 0, array.length - 1, swapCounter, comparisonCounter);
        long executionTime = System.nanoTime() - startTime;
        return new PerformanceResult(executionTime, swapCounter[0], comparisonCounter[0]);
    }

    private void mergeSortPerformanceWithSwaps(int[] arr, int left, int right, int[] swapCounter, int[] comparisonCounter) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortPerformanceWithSwaps(arr, left, mid, swapCounter, comparisonCounter);
            mergeSortPerformanceWithSwaps(arr, mid + 1, right, swapCounter, comparisonCounter);
            mergePerformanceWithSwaps(arr, left, mid, right, swapCounter, comparisonCounter);
        }
    }

    private void mergePerformanceWithSwaps(int[] arr, int left, int mid, int right, int[] swapCounter, int[] comparisonCounter) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(arr, left, leftArray, 0, n1);
        System.arraycopy(arr, mid + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            comparisonCounter[0]++;
            if (leftArray[i] <= rightArray[j]) {
                arr[k++] = leftArray[i++];
            } else {
                arr[k++] = rightArray[j++];
            }
            swapCounter[0]++;
        }

        while (i < n1) {
            arr[k++] = leftArray[i++];
            swapCounter[0]++;
        }

        while (j < n2) {
            arr[k++] = rightArray[j++];
            swapCounter[0]++;
        }
    }

    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }
}
