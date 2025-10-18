package com.example.vizualizatorsortiranja.models;

/**
 * Represents a single step in the sorting visualization.
 * Contains array state and indices being compared/swapped.
 */
public class SortStep {

    public enum StepType {
        COMPARE,
        SWAP,
        COMPLETE
    }

    public enum HeapPhase {
        BUILD_HEAP,
        EXTRACT_MAX
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
    private final int leftStartPosition; // Starting position of left subarray in main array
    private final int rightStartPosition; // Starting position of right subarray in main array
    private final int pivotIndex; // Pivot index for Quick Sort visualization
    private final int keyIndex; // Key index for Insertion Sort visualization
    private final int minIndex; // Minimum index for Selection Sort visualization
    private final int heapSize; // Current heap size for Heap Sort visualization
    private final HeapPhase heapPhase; // Current phase for Heap Sort
    private final int heapParentIndex; // Parent node in heapify operation
    private final int heapLeftChild; // Left child in heapify operation
    private final int heapRightChild; // Right child in heapify operation

    public SortStep(int[] arrayState, int index1, int index2, StepType type) {
        this(arrayState, index1, index2, type, -1);
    }

    public SortStep(int[] arrayState, int index1, int index2, StepType type, int pivotIndex) {
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
        this.leftStartPosition = -1;
        this.rightStartPosition = -1;
        this.pivotIndex = pivotIndex;
        this.keyIndex = -1;
        this.minIndex = -1;
        this.heapSize = -1;
        this.heapPhase = null;
        this.heapParentIndex = -1;
        this.heapLeftChild = -1;
        this.heapRightChild = -1;
    }

    public SortStep(int[] arrayState, int index1, int index2, StepType type, Integer keyValue, int keyIndex, boolean hasKeyValue) {
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
        this.leftStartPosition = -1;
        this.rightStartPosition = -1;
        this.pivotIndex = -1;
        this.keyIndex = keyIndex;
        this.minIndex = -1;
        this.heapSize = -1;
        this.heapPhase = null;
        this.heapParentIndex = -1;
        this.heapLeftChild = -1;
        this.heapRightChild = -1;
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
        this.leftStartPosition = -1;
        this.rightStartPosition = -1;
        this.pivotIndex = -1;
        this.keyIndex = -1;
        this.minIndex = -1;
        this.heapSize = -1;
        this.heapPhase = null;
        this.heapParentIndex = -1;
        this.heapLeftChild = -1;
        this.heapRightChild = -1;
    }

    public SortStep(int[] arrayState, int index1, int index2, StepType type,
                    int[] leftArray, int[] rightArray, int leftHighlight, int rightHighlight,
                    int leftStartPosition, int rightStartPosition) {
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
        this.leftStartPosition = leftStartPosition;
        this.rightStartPosition = rightStartPosition;
        this.pivotIndex = -1;
        this.keyIndex = -1;
        this.minIndex = -1;
        this.heapSize = -1;
        this.heapPhase = null;
        this.heapParentIndex = -1;
        this.heapLeftChild = -1;
        this.heapRightChild = -1;
    }

    public SortStep(int[] arrayState, int index1, int index2, StepType type, int minIndex, boolean isSelectionSort) {
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
        this.leftStartPosition = -1;
        this.rightStartPosition = -1;
        this.pivotIndex = -1;
        this.keyIndex = -1;
        this.minIndex = minIndex;
        this.heapSize = -1;
        this.heapPhase = null;
        this.heapParentIndex = -1;
        this.heapLeftChild = -1;
        this.heapRightChild = -1;
    }

    public SortStep(int[] arrayState, int index1, int index2, StepType type,
                    int heapSize, HeapPhase phase, int parentIdx, int leftChild, int rightChild) {
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
        this.leftStartPosition = -1;
        this.rightStartPosition = -1;
        this.pivotIndex = -1;
        this.keyIndex = -1;
        this.minIndex = -1;
        this.heapSize = heapSize;
        this.heapPhase = phase;
        this.heapParentIndex = parentIdx;
        this.heapLeftChild = leftChild;
        this.heapRightChild = rightChild;
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

    public int getLeftStartPosition() {
        return leftStartPosition;
    }

    public int getRightStartPosition() {
        return rightStartPosition;
    }

    public int getPivotIndex() {
        return pivotIndex;
    }

    public int getKeyIndex() {
        return keyIndex;
    }

    public int getMinIndex() {
        return minIndex;
    }

    public int getHeapSize() {
        return heapSize;
    }

    public HeapPhase getHeapPhase() {
        return heapPhase;
    }

    public int getHeapParentIndex() {
        return heapParentIndex;
    }

    public int getHeapLeftChild() {
        return heapLeftChild;
    }

    public int getHeapRightChild() {
        return heapRightChild;
    }
}
