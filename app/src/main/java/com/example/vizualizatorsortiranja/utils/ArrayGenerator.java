package com.example.vizualizatorsortiranja.utils;

import java.util.Random;

/**
 * Utility class for generating test arrays.
 */
public class ArrayGenerator {

    /**
     * Generates a random array of given size.
     * @param size Number of elements
     * @param maxValue Maximum value for elements
     * @return Random array
     */
    public static int[] generateRandom(int size, int maxValue) {
        int[] array = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(maxValue) + 1;
        }
        return array;
    }

    /**
     * Generates a sorted array.
     * @param size Number of elements
     * @return Sorted array
     */
    public static int[] generateSorted(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i + 1;
        }
        return array;
    }

    /**
     * Generates a reverse sorted array.
     * @param size Number of elements
     * @return Reverse sorted array
     */
    public static int[] generateReverseSorted(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i;
        }
        return array;
    }

    /**
     * Generates a nearly sorted array (sorted with few random swaps).
     * @param size Number of elements
     * @param swaps Number of random swaps to perform
     * @return Nearly sorted array
     */
    public static int[] generateNearlySorted(int size, int swaps) {
        int[] array = generateSorted(size);
        Random random = new Random();

        for (int i = 0; i < swaps; i++) {
            int index1 = random.nextInt(size);
            int index2 = random.nextInt(size);
            int temp = array[index1];
            array[index1] = array[index2];
            array[index2] = temp;
        }

        return array;
    }
}
