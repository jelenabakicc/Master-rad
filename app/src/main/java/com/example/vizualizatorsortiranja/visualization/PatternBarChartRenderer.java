package com.example.vizualizatorsortiranja.visualization;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.utils.Transformer;

/**
 * Custom BarChartRenderer that applies pattern drawables to bars.
 * This makes bars distinguishable in black-and-white printing.
 */
public class PatternBarChartRenderer extends BarChartRenderer {

    private Drawable[] patternDrawables;
    private RectF mBarShadowRect = new RectF();

    public PatternBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    /**
     * Set the pattern drawables for each bar.
     *
     * @param drawables Array of Drawable objects for patterns
     */
    public void setPatternDrawables(Drawable[] drawables) {
        this.patternDrawables = drawables;
    }

    @Override
    protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {
        if (patternDrawables == null || patternDrawables.length == 0) {
            // Fall back to default rendering if no patterns set
            super.drawDataSet(c, dataSet, index);
            return;
        }

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        mBarBorderPaint.setColor(dataSet.getBarBorderColor());
        mBarBorderPaint.setStrokeWidth(dataSet.getBarBorderWidth());

        final boolean drawBorder = dataSet.getBarBorderWidth() > 0.f;

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        // Draw the bars
        if (mChart.isDrawBarShadowEnabled()) {
            mShadowPaint.setColor(dataSet.getBarShadowColor());

            BarBuffer buffer = mBarBuffers[index];

            final float barWidth = mChart.getBarData().getBarWidth();
            final float barWidthHalf = barWidth / 2.0f;
            float x;

            for (int i = 0, count = Math.min((int) (Math.ceil((float) (dataSet.getEntryCount()) * phaseX)), dataSet.getEntryCount());
                 i < count;
                 i++) {

                BarEntry e = dataSet.getEntryForIndex(i);

                x = e.getX();

                mBarShadowRect.left = x - barWidthHalf;
                mBarShadowRect.right = x + barWidthHalf;

                trans.rectValueToPixel(mBarShadowRect);

                if (!mViewPortHandler.isInBoundsLeft(mBarShadowRect.right))
                    continue;

                if (!mViewPortHandler.isInBoundsRight(mBarShadowRect.left))
                    break;

                mBarShadowRect.top = mViewPortHandler.contentTop();
                mBarShadowRect.bottom = mViewPortHandler.contentBottom();

                c.drawRect(mBarShadowRect, mShadowPaint);
            }
        }

        BarBuffer buffer = mBarBuffers[index];

        // Draw each bar with its pattern
        for (int j = 0; j < buffer.size(); j += 4) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;

            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;

            int barIndex = j / 4;

            // Get the appropriate pattern for this bar
            Drawable pattern = patternDrawables[barIndex % patternDrawables.length];

            // Calculate bar bounds
            int left = (int) buffer.buffer[j];
            int top = (int) buffer.buffer[j + 1];
            int right = (int) buffer.buffer[j + 2];
            int bottom = (int) buffer.buffer[j + 3];

            // Apply pattern to bar
            pattern.setBounds(left, top, right, bottom);
            pattern.draw(c);

            // Draw border if enabled
            if (drawBorder) {
                c.drawRect(left, top, right, bottom, mBarBorderPaint);
            }
        }
    }
}
