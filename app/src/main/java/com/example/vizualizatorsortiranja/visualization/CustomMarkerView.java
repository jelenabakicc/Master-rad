package com.example.vizualizatorsortiranja.visualization;

import android.content.Context;
import android.widget.TextView;

import com.example.vizualizatorsortiranja.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

/**
 * Custom MarkerView for displaying algorithm name and value when bar is tapped.
 */
public class CustomMarkerView extends MarkerView {

    private TextView tvAlgorithmName;
    private TextView tvValue;
    private String[] algorithmNames;
    private String unit;
    private DecimalFormat decimalFormat;

    /**
     * Constructor for CustomMarkerView.
     *
     * @param context Context
     * @param layoutResource Layout resource ID
     * @param algorithmNames Array of algorithm names
     * @param unit Unit to display (e.g., "ms", "swaps", "comparisons")
     */
    public CustomMarkerView(Context context, int layoutResource, String[] algorithmNames, String unit) {
        super(context, layoutResource);

        this.algorithmNames = algorithmNames;
        this.unit = unit;

        if (unit.equals("ms")) {
            this.decimalFormat = new DecimalFormat("0.00000");
        } else {
            this.decimalFormat = new DecimalFormat("0");
        }

        tvAlgorithmName = findViewById(R.id.tv_algorithm_name);
        tvValue = findViewById(R.id.tv_value);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int index = (int) e.getX();

        if (index >= 0 && index < algorithmNames.length) {
            tvAlgorithmName.setText(algorithmNames[index]);
            tvValue.setText(decimalFormat.format(e.getY()) + " " + unit);
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2f), -getHeight());
    }
}
