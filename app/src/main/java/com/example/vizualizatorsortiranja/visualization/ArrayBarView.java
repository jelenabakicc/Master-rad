package com.example.vizualizatorsortiranja.visualization;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom view for visualizing array as vertical bars.
 * Each element is represented as a bar with height proportional to its value.
 */
public class ArrayBarView extends View {

    private int[] array;
    private int highlightIndex1 = -1;
    private int highlightIndex2 = -1;
    private int[] leftArray = null;
    private int[] rightArray = null;
    private int leftHighlight = -1;
    private int rightHighlight = -1;
    private Paint barPaint;
    private Paint highlightPaint;
    private Paint textPaint;
    private Paint helperPaint;

    public ArrayBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        barPaint = new Paint();
        barPaint.setColor(Color.BLUE);
        barPaint.setStyle(Paint.Style.FILL);

        highlightPaint = new Paint();
        highlightPaint.setColor(Color.RED);
        highlightPaint.setStyle(Paint.Style.FILL);

        helperPaint = new Paint();
        helperPaint.setColor(Color.parseColor("#4CAF50")); // Green
        helperPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
    }

    public void setArray(int[] array) {
        this.array = array;
        invalidate();
    }

    public void setHighlightIndices(int index1, int index2) {
        this.highlightIndex1 = index1;
        this.highlightIndex2 = index2;
        invalidate();
    }

    public void setHelperArrays(int[] leftArray, int[] rightArray, int leftHighlight, int rightHighlight) {
        this.leftArray = leftArray;
        this.rightArray = rightArray;
        this.leftHighlight = leftHighlight;
        this.rightHighlight = rightHighlight;
        invalidate();
    }

    public void clearHelperArrays() {
        this.leftArray = null;
        this.rightArray = null;
        this.leftHighlight = -1;
        this.rightHighlight = -1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (array == null || array.length == 0) {
            return;
        }

        float width = getWidth();
        float height = getHeight();

        // If helper arrays are present (Merge Sort), draw them separately
        if (leftArray != null && rightArray != null) {
            drawMergeSortView(canvas, width, height);
        } else {
            drawNormalView(canvas, width, height);
        }
    }

    private void drawNormalView(Canvas canvas, float width, float height) {
        float barWidth = width / array.length;

        // Reserve space at bottom for text
        float textHeight = 40;
        float drawableHeight = height - textHeight;

        int maxValue = getMaxValue(array);

        // Calculate appropriate text size based on bar width
        float textSize = Math.min(barWidth * 0.8f, 30);
        textPaint.setTextSize(textSize);

        for (int i = 0; i < array.length; i++) {
            float barHeight = (array[i] / (float) maxValue) * drawableHeight;
            float left = i * barWidth;
            float top = drawableHeight - barHeight;
            float right = left + barWidth - 2; // 2px gap between bars

            Paint paint = (i == highlightIndex1 || i == highlightIndex2) ? highlightPaint : barPaint;
            canvas.drawRect(left, top, right, drawableHeight, paint);

            // Draw the number at the bottom center of each bar
            float textX = left + (barWidth - 2) / 2;
            float textY = height - 10; // 10px from bottom
            canvas.drawText(String.valueOf(array[i]), textX, textY, textPaint);
        }
    }

    private void drawMergeSortView(Canvas canvas, float width, float height) {
        float textHeight = 40;

        // Divide height into 3 sections: main array, left subarray, right subarray
        float sectionHeight = (height - textHeight * 3) / 3;

        int maxValue = getMaxValueMultiple(array, leftArray, rightArray);

        // Use main array length for consistent bar width across all sections
        int mainArrayLength = array.length;

        // Draw main array (top)
        drawArraySectionFixed(canvas, array, 0, sectionHeight, width, maxValue, highlightIndex1, highlightIndex2, barPaint, mainArrayLength, 0);

        // Draw left subarray (middle)
        if (leftArray.length > 0) {
            drawArraySectionFixed(canvas, leftArray, sectionHeight + textHeight, sectionHeight, width, maxValue, leftHighlight, -1, helperPaint, mainArrayLength, 0);
        }

        // Draw right subarray (bottom)
        if (rightArray.length > 0) {
            drawArraySectionFixed(canvas, rightArray, sectionHeight * 2 + textHeight * 2, sectionHeight, width, maxValue, rightHighlight, -1, helperPaint, mainArrayLength, leftArray.length);
        }
    }

    private void drawArraySectionFixed(Canvas canvas, int[] arr, float offsetY, float sectionHeight, float width, int maxValue, int highlight1, int highlight2, Paint normalPaint, int totalArrayLength, int offsetPosition) {
        if (arr == null || arr.length == 0) return;

        // Use total array length for consistent bar width
        float barWidth = width / totalArrayLength;
        float textHeight = 40;

        float textSize = Math.min(barWidth * 0.8f, 30);
        textPaint.setTextSize(textSize);

        // For helper arrays (green bars), show only elements from highlight index onwards
        boolean isHelperArray = normalPaint == helperPaint;
        int startIndex = 0;
        if (isHelperArray && highlight1 >= 0) {
            startIndex = highlight1;
        }

        for (int i = startIndex; i < arr.length; i++) {
            float barHeight = (arr[i] / (float) maxValue) * sectionHeight;
            // Calculate position based on original array position
            int actualPosition = offsetPosition + i;
            float left = actualPosition * barWidth;
            float top = offsetY + (sectionHeight - barHeight);
            float right = left + barWidth - 2;
            float bottom = offsetY + sectionHeight;

            Paint paint = (i == highlight1 || i == highlight2) ? highlightPaint : normalPaint;
            canvas.drawRect(left, top, right, bottom, paint);

            // Draw the number
            float textX = left + (barWidth - 2) / 2;
            float textY = offsetY + sectionHeight + 30;
            canvas.drawText(String.valueOf(arr[i]), textX, textY, textPaint);
        }
    }

    private int getMaxValue(int[] arr) {
        int max = arr[0];
        for (int value : arr) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int getMaxValueMultiple(int[] arr1, int[] arr2, int[] arr3) {
        int max = 0;
        if (arr1 != null && arr1.length > 0) max = Math.max(max, getMaxValue(arr1));
        if (arr2 != null && arr2.length > 0) max = Math.max(max, getMaxValue(arr2));
        if (arr3 != null && arr3.length > 0) max = Math.max(max, getMaxValue(arr3));
        return max;
    }
}
