package com.example.vizualizatorsortiranja.visualization;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Custom view for visualizing array as vertical bars.
 * Each element is represented as a bar with height proportional to its value.
 */
public class ArrayBarView extends View {

    private int[] array;
    private int highlightIndex1 = -1;
    private int highlightIndex2 = -1;
    private int pivotIndex = -1;
    private int keyIndex = -1;
    private int minIndex = -1;
    private int[] leftArray = null;
    private int[] rightArray = null;
    private int leftHighlight = -1;
    private int rightHighlight = -1;
    private int leftStartPosition = -1;
    private int rightStartPosition = -1;
    private int heapSize = -1;
    private int heapParentIndex = -1;
    private int heapLeftChild = -1;
    private int heapRightChild = -1;
    private Paint barPaint;
    private Paint highlightPaint;
    private Paint pivotPaint;
    private Paint textPaint;
    private Paint helperPaint;
    private Paint sortedPaint;
    private Paint treePaint;
    private Paint childPaint;

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

        pivotPaint = new Paint();
        pivotPaint.setColor(Color.YELLOW);
        pivotPaint.setStyle(Paint.Style.FILL);

        helperPaint = new Paint();
        helperPaint.setColor(Color.parseColor("#4CAF50")); // Green
        helperPaint.setStyle(Paint.Style.FILL);

        sortedPaint = new Paint();
        sortedPaint.setColor(Color.GREEN);
        sortedPaint.setStyle(Paint.Style.FILL);

        treePaint = new Paint();
        treePaint.setColor(Color.GRAY);
        treePaint.setStyle(Paint.Style.STROKE);
        treePaint.setStrokeWidth(3);
        treePaint.setAntiAlias(true);

        childPaint = new Paint();
        childPaint.setColor(Color.parseColor("#FFA500")); // Orange
        childPaint.setStyle(Paint.Style.FILL);

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

    public void setPivotIndex(int pivotIndex) {
        this.pivotIndex = pivotIndex;
        invalidate();
    }

    public void setKeyIndex(int keyIndex) {
        this.keyIndex = keyIndex;
        invalidate();
    }

    public void setMinIndex(int minIndex) {
        this.minIndex = minIndex;
        invalidate();
    }

    public void setHeapInfo(int heapSize, int parent, int left, int right) {
        this.heapSize = heapSize;
        this.heapParentIndex = parent;
        this.heapLeftChild = left;
        this.heapRightChild = right;
        invalidate();
    }

    public void clearHeapInfo() {
        this.heapSize = -1;
        this.heapParentIndex = -1;
        this.heapLeftChild = -1;
        this.heapRightChild = -1;
        invalidate();
    }

    public void setHelperArrays(int[] leftArray, int[] rightArray, int leftHighlight, int rightHighlight, int leftStartPosition, int rightStartPosition) {
        this.leftArray = leftArray;
        this.rightArray = rightArray;
        this.leftHighlight = leftHighlight;
        this.rightHighlight = rightHighlight;
        this.leftStartPosition = leftStartPosition;
        this.rightStartPosition = rightStartPosition;
        invalidate();
    }

    public void clearHelperArrays() {
        this.leftArray = null;
        this.rightArray = null;
        this.leftHighlight = -1;
        this.rightHighlight = -1;
        this.leftStartPosition = -1;
        this.rightStartPosition = -1;
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

        if (heapSize >= 0) {
            drawHeapSortView(canvas, width, height);
        } else if (leftArray != null && rightArray != null) {
            drawMergeSortView(canvas, width, height);
        } else {
            drawNormalView(canvas, width, height);
        }
    }

    private void drawNormalView(Canvas canvas, float width, float height) {
        float barWidth = width / array.length;

        // Reserve space at bottom for text only if array is small enough
        boolean showLabels = array.length <= 30;
        float textHeight = showLabels ? 40 : 0;
        float drawableHeight = height - textHeight;

        int maxValue = getMaxValue(array);

        // Calculate appropriate text size based on bar width
        float textSize = Math.min(barWidth * 0.8f, 30);
        textPaint.setTextSize(textSize);

        for (int i = 0; i < array.length; i++) {
            float barHeight = (array[i] / (float) maxValue) * drawableHeight;
            float left = i * barWidth;
            float top = drawableHeight - barHeight;
            float right = left + barWidth - 2;

            Paint paint;
            if (i == pivotIndex || i == keyIndex || i == minIndex) {
                paint = pivotPaint;
            } else if (i == highlightIndex1 || i == highlightIndex2) {
                paint = highlightPaint;
            } else {
                paint = barPaint;
            }
            canvas.drawRect(left, top, right, drawableHeight, paint);

            if (showLabels) {
                float textX = left + (barWidth - 2) / 2;
                float textY = height - 10; // 10px from bottom
                canvas.drawText(String.valueOf(array[i]), textX, textY, textPaint);
            }
        }
    }

    private void drawMergeSortView(Canvas canvas, float width, float height) {
        float textHeight = 40;

        // Divide height into 3 sections: main array, left subarray, right subarray
        float sectionHeight = (height - textHeight * 3) / 3;

        int maxValue = getMaxValueMultiple(array, leftArray, rightArray);
        int mainArrayLength = array.length;

        drawArraySectionFixed(canvas, array, 0, sectionHeight, width, maxValue, highlightIndex1, highlightIndex2, barPaint, mainArrayLength, 0);
        // Draw left subarray (middle) - aligned to actual position in main array
        if (leftArray.length > 0) {
            drawArraySectionFixed(canvas, leftArray, sectionHeight + textHeight, sectionHeight, width, maxValue, leftHighlight, -1, helperPaint, mainArrayLength, leftStartPosition);
        }
        // Draw right subarray (bottom) - aligned to actual position in main array
        if (rightArray.length > 0) {
            drawArraySectionFixed(canvas, rightArray, sectionHeight * 2 + textHeight * 2, sectionHeight, width, maxValue, rightHighlight, -1, helperPaint, mainArrayLength, rightStartPosition);
        }
    }

    private void drawArraySectionFixed(Canvas canvas, int[] arr, float offsetY, float sectionHeight, float width, int maxValue, int highlight1, int highlight2, Paint normalPaint, int totalArrayLength, int offsetPosition) {
        if (arr == null || arr.length == 0) return;

        // Use total array length for consistent bar width
        float barWidth = width / totalArrayLength;
        float textHeight = 40;

        float textSize = Math.min(barWidth * 0.8f, 30);
        textPaint.setTextSize(textSize);

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

    private void drawHeapSortView(Canvas canvas, float width, float height) {
        // Divide screen: 40% array bars, 60% tree
        float arraySection = height * 0.4f;
        float treeSection = height * 0.6f;

        drawHeapArrayBars(canvas, width, arraySection);
        drawHeapTree(canvas, width, height, arraySection, treeSection);
    }

    private void drawHeapArrayBars(Canvas canvas, float width, float height) {
        float barWidth = width / array.length;
        boolean showLabels = array.length <= 30;
        float textHeight = showLabels ? 40 : 0;
        float drawableHeight = height - textHeight;
        int maxValue = getMaxValue(array);
        float textSize = Math.min(barWidth * 0.8f, 30);
        textPaint.setTextSize(textSize);

        for (int i = 0; i < array.length; i++) {
            float barHeight = (array[i] / (float) maxValue) * drawableHeight;
            float left = i * barWidth;
            float top = drawableHeight - barHeight;
            float right = left + barWidth - 2;

            // Color logic for heap visualization
            Paint paint;
            if (i >= heapSize) {
                paint = sortedPaint;
            } else if (i == heapParentIndex) {
                paint = pivotPaint;
            } else if (i == heapLeftChild || i == heapRightChild) {
                paint = childPaint;
            } else if (i == highlightIndex1 || i == highlightIndex2) {
                paint = highlightPaint;
            } else {
                paint = barPaint;
            }

            canvas.drawRect(left, top, right, drawableHeight, paint);

            if (showLabels) {
                float textX = left + (barWidth - 2) / 2;
                float textY = height - 10;
                canvas.drawText(String.valueOf(array[i]), textX, textY, textPaint);
            }
        }
    }

    private void drawHeapTree(Canvas canvas, float width, float totalHeight, float treeOffsetY, float treeHeight) {
        if (heapSize <= 0) return;

        int levels = (int)(Math.log(heapSize) / Math.log(2)) + 1;
        float levelHeight = treeHeight / (levels + 1);
        float nodeRadius = Math.min(30, width / (2 * heapSize));

        for (int i = 0; i < heapSize; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            float parentX = getNodeX(i, width);
            float parentY = treeOffsetY + getNodeY(i, levelHeight);

            if (left < heapSize) {
                float childX = getNodeX(left, width);
                float childY = treeOffsetY + getNodeY(left, levelHeight);
                canvas.drawLine(parentX, parentY, childX, childY, treePaint);
            }

            if (right < heapSize) {
                float childX = getNodeX(right, width);
                float childY = treeOffsetY + getNodeY(right, levelHeight);
                canvas.drawLine(parentX, parentY, childX, childY, treePaint);
            }
        }

        // Draw all nodes on top of edges
        textPaint.setTextSize(Math.min(nodeRadius, 20));
        for (int i = 0; i < heapSize; i++) {
            float nodeX = getNodeX(i, width);
            float nodeY = treeOffsetY + getNodeY(i, levelHeight);

            Paint paint;
            if (i == heapParentIndex) {
                paint = pivotPaint;
            } else if (i == heapLeftChild || i == heapRightChild) {
                paint = childPaint;
            } else {
                paint = barPaint;
            }

            canvas.drawCircle(nodeX, nodeY, nodeRadius, paint);

            Paint borderPaint = new Paint();
            borderPaint.setColor(Color.BLACK);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(2);
            borderPaint.setAntiAlias(true);
            canvas.drawCircle(nodeX, nodeY, nodeRadius, borderPaint);

            canvas.drawText(String.valueOf(array[i]), nodeX, nodeY + textPaint.getTextSize() / 3, textPaint);
        }
    }

    // Tree layout helper methods
    private int getNodeLevel(int index) {
        return (int)(Math.log(index + 1) / Math.log(2));
    }

    private int getPositionInLevel(int index) {
        int level = getNodeLevel(index);
        return index - ((1 << level) - 1);
    }

    private float getNodeX(int index, float width) {
        int level = getNodeLevel(index);
        int nodesInLevel = 1 << level;
        int position = getPositionInLevel(index);
        float spacing = width / (nodesInLevel + 1);
        return spacing * (position + 1);
    }

    private float getNodeY(int index, float levelHeight) {
        int level = getNodeLevel(index);
        return levelHeight * (level + 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (array == null || array.length == 0) {
                return false;
            }

            float touchX = event.getX();
            float touchY = event.getY();
            float width = getWidth();
            float height = getHeight();

            if (heapSize >= 0) {
                float arraySection = height * 0.4f;

                if (touchY >= arraySection) {
                    int toucedNode = findTouchedTreeNode(touchX, touchY, width, height, arraySection);
                    if (toucedNode >= 0) {
                        String message = "Index: " + toucedNode + ", Value: " + array[toucedNode];
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }

                float barWidth = width / array.length;
                int barIndex = (int) (touchX / barWidth);
                if (barIndex >= 0 && barIndex < array.length) {
                    String message = "Index: " + barIndex + ", Value: " + array[barIndex];
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    return true;
                }
            } else {
                float barWidth = width / array.length;
                int barIndex = (int) (touchX / barWidth);
                if (barIndex >= 0 && barIndex < array.length) {
                    String message = "Index: " + barIndex + ", Value: " + array[barIndex];
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private int findTouchedTreeNode(float touchX, float touchY, float width, float height, float treeOffsetY) {
        if (heapSize <= 0) return -1;

        int levels = (int)(Math.log(heapSize) / Math.log(2)) + 1;
        float treeHeight = height * 0.6f;
        float levelHeight = treeHeight / (levels + 1);
        float nodeRadius = Math.min(30, width / (2 * heapSize));

        for (int i = 0; i < heapSize; i++) {
            float nodeX = getNodeX(i, width);
            float nodeY = treeOffsetY + getNodeY(i, levelHeight);

            float dx = touchX - nodeX;
            float dy = touchY - nodeY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            // If touch is within node radius, return this node index
            if (distance <= nodeRadius) {
                return i;
            }
        }

        return -1;
    }
}
