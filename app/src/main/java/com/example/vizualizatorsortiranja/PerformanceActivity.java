package com.example.vizualizatorsortiranja;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vizualizatorsortiranja.algorithms.BubbleSort;
import com.example.vizualizatorsortiranja.algorithms.HeapSort;
import com.example.vizualizatorsortiranja.algorithms.InsertionSort;
import com.example.vizualizatorsortiranja.algorithms.MergeSort;
import com.example.vizualizatorsortiranja.algorithms.QuickSort;
import com.example.vizualizatorsortiranja.algorithms.SelectionSort;
import com.example.vizualizatorsortiranja.algorithms.SortingAlgorithm;
import com.example.vizualizatorsortiranja.models.PerformanceResult;
import com.example.vizualizatorsortiranja.visualization.CustomMarkerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Activity for displaying performance analysis and comparison charts.
 * Uses MPAndroidChart to visualize time complexity and benchmark results.
 */
public class PerformanceActivity extends AppCompatActivity {

    private Spinner spinnerAlgorithm;
    private EditText editPerformanceArray;
    private Button btnRunBenchmark;
    private Button btnToggleView;
    private ViewFlipper viewFlipper;
    private TextView textTheory;
    private BarChart performanceChart;
    private BarChart swapChart;
    private BarChart comparisonChart;

    private SortingAlgorithm[] algorithms;
    private String[] theoryTexts;
    private boolean isShowingTheory = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        // Initialize views
        spinnerAlgorithm = findViewById(R.id.spinner_performance_algorithm);
        editPerformanceArray = findViewById(R.id.edit_performance_array);
        btnRunBenchmark = findViewById(R.id.btn_run_benchmark);
        btnToggleView = findViewById(R.id.btn_toggle_view);
        viewFlipper = findViewById(R.id.view_flipper);
        textTheory = findViewById(R.id.text_theory);
        performanceChart = findViewById(R.id.performance_chart);
        swapChart = findViewById(R.id.swap_chart);
        comparisonChart = findViewById(R.id.comparison_chart);

        // Initialize algorithms
        algorithms = new SortingAlgorithm[]{
                new BubbleSort(),
                new InsertionSort(),
                new SelectionSort(),
                new QuickSort(),
                new MergeSort(),
                new HeapSort()
        };

        // Initialize theory texts
        theoryTexts = new String[]{
                getString(R.string.theory_bubble_sort),
                getString(R.string.theory_insertion_sort),
                getString(R.string.theory_selection_sort),
                getString(R.string.theory_quick_sort),
                getString(R.string.theory_merge_sort),
                getString(R.string.theory_heap_sort)
        };

        // Setup spinner
        String[] algorithmNames = new String[algorithms.length];
        for (int i = 0; i < algorithms.length; i++) {
            algorithmNames[i] = algorithms[i].getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, algorithmNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlgorithm.setAdapter(adapter);

        // Display theory for first algorithm by default
        textTheory.setText(theoryTexts[0]);

        // Set initial visibility: theory mode is default
        editPerformanceArray.setVisibility(android.view.View.GONE);
        btnRunBenchmark.setVisibility(android.view.View.GONE);
        btnToggleView.setText(R.string.show_performance);

        // Spinner selection listener - update theory display
        spinnerAlgorithm.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                if (isShowingTheory) {
                    textTheory.setText(theoryTexts[position]);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        // Run benchmark button
        btnRunBenchmark.setOnClickListener(v -> {
            String arrayStr = editPerformanceArray.getText().toString().trim();

            if (arrayStr.isEmpty()) {
                Toast.makeText(this, "Please enter array values", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                String[] parts = arrayStr.split(",");
                int[] customArray = new int[parts.length];

                for (int i = 0; i < parts.length; i++) {
                    customArray[i] = Integer.parseInt(parts[i].trim());
                }

                runBenchmark(customArray);

                // Switch to performance view
                if (isShowingTheory) {
                    viewFlipper.showNext();
                    isShowingTheory = false;
                    btnToggleView.setText(R.string.show_theory);
                    // Update visibility when switching to performance mode
                    spinnerAlgorithm.setVisibility(android.view.View.GONE);
                    editPerformanceArray.setVisibility(android.view.View.VISIBLE);
                    btnRunBenchmark.setVisibility(android.view.View.VISIBLE);
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid array format. Use comma-separated numbers", Toast.LENGTH_SHORT).show();
            }
        });

        // Toggle view button
        btnToggleView.setOnClickListener(v -> {
            viewFlipper.showNext();
            isShowingTheory = !isShowingTheory;

            if (isShowingTheory) {
                btnToggleView.setText(R.string.show_performance);
                // Update theory for currently selected algorithm
                textTheory.setText(theoryTexts[spinnerAlgorithm.getSelectedItemPosition()]);
                // Show algorithm spinner in theory mode, hide array input and benchmark button
                spinnerAlgorithm.setVisibility(android.view.View.VISIBLE);
                editPerformanceArray.setVisibility(android.view.View.GONE);
                btnRunBenchmark.setVisibility(android.view.View.GONE);
            } else {
                btnToggleView.setText(R.string.show_theory);
                // Hide algorithm spinner in performance mode, show array input and benchmark button
                spinnerAlgorithm.setVisibility(android.view.View.GONE);
                editPerformanceArray.setVisibility(android.view.View.VISIBLE);
                btnRunBenchmark.setVisibility(android.view.View.VISIBLE);
            }
        });
    }

    private void runBenchmark(int[] inputArray) {
        ArrayList<BarEntry> timeEntries = new ArrayList<>();
        ArrayList<BarEntry> swapEntries = new ArrayList<>();
        ArrayList<BarEntry> comparisonEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        // Run each algorithm and collect performance data
        for (int i = 0; i < algorithms.length; i++) {
            int[] testArray = inputArray.clone();
            PerformanceResult result = algorithms[i].sortForPerformanceWithSwaps(testArray);

            // Convert nanoseconds to milliseconds for better readability
            float timeInMs = result.getExecutionTime() / 1_000_000f;

            timeEntries.add(new BarEntry(i, timeInMs));
            swapEntries.add(new BarEntry(i, result.getSwapCount()));
            comparisonEntries.add(new BarEntry(i, result.getComparisonCount()));
            labels.add(algorithms[i].getName());
        }

        // Create time dataset
        BarDataSet timeDataSet = new BarDataSet(timeEntries, "Execution Time (ms)");
        timeDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        timeDataSet.setValueTextSize(12f);
        timeDataSet.setValueTextColor(Color.BLACK);

        BarData timeBarData = new BarData(timeDataSet);
        timeBarData.setBarWidth(0.8f);

        // Configure time chart
        performanceChart.setData(timeBarData);
        performanceChart.getDescription().setEnabled(false);
        performanceChart.setFitBars(true);
        performanceChart.animateY(1000);
        performanceChart.setExtraBottomOffset(50f); // Add space for rotated labels
        performanceChart.getLegend().setEnabled(false); // Disable legend

        // Configure time chart X axis
        XAxis timeXAxis = performanceChart.getXAxis();
        timeXAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        timeXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        timeXAxis.setGranularity(1f);
        timeXAxis.setGranularityEnabled(true);
        timeXAxis.setLabelRotationAngle(-30f); // Reduced rotation angle
        timeXAxis.setTextSize(9f); // Slightly smaller text
        timeXAxis.setYOffset(5f); // Add offset from axis

        // Configure time chart Y axis
        performanceChart.getAxisLeft().setAxisMinimum(0f);
        performanceChart.getAxisRight().setEnabled(false);

        // Enable touch and setup custom marker view
        performanceChart.setTouchEnabled(true);
        performanceChart.setDragEnabled(true);
        performanceChart.setScaleEnabled(false);
        performanceChart.setHighlightPerTapEnabled(true);

        CustomMarkerView timeMarker = new CustomMarkerView(this, R.layout.marker_view,
                labels.toArray(new String[0]), "ms");
        performanceChart.setMarker(timeMarker);

        performanceChart.invalidate();

        // Create swap dataset
        BarDataSet swapDataSet = new BarDataSet(swapEntries, "Number of Swaps");
        swapDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        swapDataSet.setValueTextSize(12f);
        swapDataSet.setValueTextColor(Color.BLACK);

        BarData swapBarData = new BarData(swapDataSet);
        swapBarData.setBarWidth(0.8f);

        // Configure swap chart
        swapChart.setData(swapBarData);
        swapChart.getDescription().setEnabled(false);
        swapChart.setFitBars(true);
        swapChart.animateY(1000);
        swapChart.setExtraBottomOffset(50f); // Add space for rotated labels
        swapChart.getLegend().setEnabled(false); // Disable legend

        // Configure swap chart X axis
        XAxis swapXAxis = swapChart.getXAxis();
        swapXAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        swapXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        swapXAxis.setGranularity(1f);
        swapXAxis.setGranularityEnabled(true);
        swapXAxis.setLabelRotationAngle(-30f); // Reduced rotation angle
        swapXAxis.setTextSize(9f); // Slightly smaller text
        swapXAxis.setYOffset(5f); // Add offset from axis

        // Configure swap chart Y axis
        swapChart.getAxisLeft().setAxisMinimum(0f);
        swapChart.getAxisRight().setEnabled(false);

        // Enable touch and setup custom marker view for swap chart
        swapChart.setTouchEnabled(true);
        swapChart.setDragEnabled(true);
        swapChart.setScaleEnabled(false);
        swapChart.setHighlightPerTapEnabled(true);

        CustomMarkerView swapMarker = new CustomMarkerView(this, R.layout.marker_view,
                labels.toArray(new String[0]), "swaps");
        swapChart.setMarker(swapMarker);

        swapChart.invalidate();

        // Create comparison dataset
        BarDataSet comparisonDataSet = new BarDataSet(comparisonEntries, "Number of Comparisons");
        comparisonDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        comparisonDataSet.setValueTextSize(12f);
        comparisonDataSet.setValueTextColor(Color.BLACK);

        BarData comparisonBarData = new BarData(comparisonDataSet);
        comparisonBarData.setBarWidth(0.8f);

        // Configure comparison chart
        comparisonChart.setData(comparisonBarData);
        comparisonChart.getDescription().setEnabled(false);
        comparisonChart.setFitBars(true);
        comparisonChart.animateY(1000);
        comparisonChart.setExtraBottomOffset(50f); // Add space for rotated labels
        comparisonChart.getLegend().setEnabled(false); // Disable legend

        // Configure comparison chart X axis
        XAxis comparisonXAxis = comparisonChart.getXAxis();
        comparisonXAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        comparisonXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        comparisonXAxis.setGranularity(1f);
        comparisonXAxis.setGranularityEnabled(true);
        comparisonXAxis.setLabelRotationAngle(-30f); // Reduced rotation angle
        comparisonXAxis.setTextSize(9f); // Slightly smaller text
        comparisonXAxis.setYOffset(5f); // Add offset from axis

        // Configure comparison chart Y axis
        comparisonChart.getAxisLeft().setAxisMinimum(0f);
        comparisonChart.getAxisRight().setEnabled(false);

        // Enable touch and setup custom marker view for comparison chart
        comparisonChart.setTouchEnabled(true);
        comparisonChart.setDragEnabled(true);
        comparisonChart.setScaleEnabled(false);
        comparisonChart.setHighlightPerTapEnabled(true);

        CustomMarkerView comparisonMarker = new CustomMarkerView(this, R.layout.marker_view,
                labels.toArray(new String[0]), "comparisons");
        comparisonChart.setMarker(comparisonMarker);

        comparisonChart.invalidate();
    }
}
