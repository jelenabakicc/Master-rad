package com.example.vizualizatorsortiranja.models;

/**
 * Represents a single step in the sorting visualization.
 * Contains array state and indices being compared/swapped.
 */
public class SortStep {

    public enum StepType {
        COMPARE,    // Comparing two elements
        SWAP,       // Swapping two elements
        COMPLETE    // Sorting complete
    }

    private final int[] arrayState;
    private final int index1;
    private final int index2;
    private final StepType type;
    private final Integer keyValue; // Optional key value for algorithms like Insertion Sort
    private final Integer compareValue1; // First value being compared (for Merge Sort)
    private final Integer compareValue2; // Second value being compared (for Merge Sort)
    private final int[] leftArray; // Left subarray for Merge Sort visualization
    private final int[] rightArray; // Right subarray for Merge Sort visualization
    private final int leftHighlight; // Highlight index in left array
    private final int rightHighlight; // Highlight index in right array

    public SortStep(int[] arrayState, int index1, int index2, StepType type) {
        this.arrayState = arrayState;
        this.index1 = index1;
        this.index2 = index2;
        this.type = type;
        this.keyValue = null;
        this.compareValue1 = null;
        this.compareValue2 = null;
        this.leftArray = null;
        this.rightArray = null;
        this.leftHighlight = -1;
        this.rightHighlight = -1;
    }

    public SortStep(int[] arrayState, int index1, int index2, StepType type, int keyValue) {
        this.arrayState = arrayState;
        this.index1 = index1;
        this.index2 = index2;
        this.type = type;
        this.keyValue = keyValue;
        this.compareValue1 = null;
        this.compareValue2 = null;
        this.leftArray = null;
        this.rightArray = null;
        this.leftHighlight = -1;
        this.rightHighlight = -1;
    }

    public SortStep(int[] arrayState, int index1, int index2, StepType type, Integer keyValue, Integer compareValue1, Integer compareValue2) {
        this.arrayState = arrayState;
        this.index1 = index1;
        this.index2 = index2;
        this.type = type;
        this.keyValue = keyValue;
        this.compareValue1 = compareValue1;
        this.compareValue2 = compareValue2;
        this.leftArray = null;
        this.rightArray = null;
        this.leftHighlight = -1;
        this.rightHighlight = -1;
    }

    // Constructor for Merge Sort with helper arrays
    public SortStep(int[] arrayState, int index1, int index2, StepType type,
                    int[] leftArray, int[] rightArray, int leftHighlight, int rightHighlight) {
        this.arrayState = arrayState;
        this.index1 = index1;
        this.index2 = index2;
        this.type = type;
        this.keyValue = null;
        this.compareValue1 = null;
        this.compareValue2 = null;
        this.leftArray = leftArray;
        this.rightArray = rightArray;
        this.leftHighlight = leftHighlight;
        this.rightHighlight = rightHighlight;
    }

    public int[] getArrayState() {
        return arrayState;
    }

    public int getIndex1() {
        return index1;
    }

    public int getIndex2() {
        return index2;
    }

    public StepType getType() {
        return type;
    }

    public Integer getKeyValue() {
        return keyValue;
    }

    public Integer getCompareValue1() {
        return compareValue1;
    }

    public Integer getCompareValue2() {
        return compareValue2;
    }

    public int[] getLeftArray() {
        return leftArray;
    }

    public int[] getRightArray() {
        return rightArray;
    }

    public int getLeftHighlight() {
        return leftHighlight;
    }

    public int getRightHighlight() {
        return rightHighlight;
    }
}
