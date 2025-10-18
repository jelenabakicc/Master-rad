package com.example.vizualizatorsortiranja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

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
    private EditText editPerformanceArrayLength;
    private LinearLayout layoutArrayLength;
    private LinearLayout layoutGenerationButtons;
    private Button btnPerfGenerateRandom;
    private Button btnPerfGenerateGaussian;
    private Button btnPerfGenerateSorted;
    private Button btnPerfGenerateReversed;
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
    private int[] currentArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String savedLanguage = preferences.getString("Language", "en");
        setLocale(savedLanguage);

        setContentView(R.layout.activity_performance);

        spinnerAlgorithm = findViewById(R.id.spinner_performance_algorithm);
        editPerformanceArray = findViewById(R.id.edit_performance_array);
        editPerformanceArrayLength = findViewById(R.id.edit_performance_array_length);
        layoutArrayLength = findViewById(R.id.layout_array_length);
        layoutGenerationButtons = findViewById(R.id.layout_generation_buttons);
        btnPerfGenerateRandom = findViewById(R.id.btn_perf_generate_random);
        btnPerfGenerateGaussian = findViewById(R.id.btn_perf_generate_gaussian);
        btnPerfGenerateSorted = findViewById(R.id.btn_perf_generate_sorted);
        btnPerfGenerateReversed = findViewById(R.id.btn_perf_generate_reversed);
        btnRunBenchmark = findViewById(R.id.btn_run_benchmark);
        btnToggleView = findViewById(R.id.btn_toggle_view);
        viewFlipper = findViewById(R.id.view_flipper);
        textTheory = findViewById(R.id.text_theory);
        performanceChart = findViewById(R.id.performance_chart);
        swapChart = findViewById(R.id.swap_chart);
        comparisonChart = findViewById(R.id.comparison_chart);

        algorithms = new SortingAlgorithm[]{
                new BubbleSort(),
                new InsertionSort(),
                new SelectionSort(),
                new QuickSort(),
                new MergeSort(),
                new HeapSort()
        };

        theoryTexts = new String[]{
                getString(R.string.theory_bubble_sort),
                getString(R.string.theory_insertion_sort),
                getString(R.string.theory_selection_sort),
                getString(R.string.theory_quick_sort),
                getString(R.string.theory_merge_sort),
                getString(R.string.theory_heap_sort)
        };

        String[] algorithmNames = new String[algorithms.length];
        for (int i = 0; i < algorithms.length; i++) {
            algorithmNames[i] = algorithms[i].getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, algorithmNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlgorithm.setAdapter(adapter);

        textTheory.setText(theoryTexts[0]);

        Intent intent = getIntent();
        String arrayData = intent.getStringExtra("ARRAY_DATA");
        boolean autoRunBenchmark = intent.getBooleanExtra("AUTO_RUN_BENCHMARK", false);

        if (arrayData != null && autoRunBenchmark) {
            // Called from VisualizationActivity with specific array
            layoutArrayLength.setVisibility(View.GONE);
            layoutGenerationButtons.setVisibility(View.GONE);

            // Set initial visibility: performance mode (not theory mode)
            editPerformanceArray.setVisibility(View.VISIBLE);
            btnRunBenchmark.setVisibility(View.VISIBLE);
            spinnerAlgorithm.setVisibility(View.GONE);
            btnToggleView.setText(R.string.show_theory);
            isShowingTheory = false;

            editPerformanceArray.setText(arrayData);

            viewFlipper.showNext();

            try {
                String[] parts = arrayData.split(",");
                int[] customArray = new int[parts.length];

                for (int i = 0; i < parts.length; i++) {
                    customArray[i] = Integer.parseInt(parts[i].trim());
                }

                currentArray = customArray;
                runBenchmark(customArray);
            } catch (NumberFormatException e) {
                Toast.makeText(this, getString(R.string.error_invalid_array_format_short), Toast.LENGTH_SHORT).show();
            }
        } else {
            // Normal mode - start in theory mode
            layoutArrayLength.setVisibility(View.GONE);
            layoutGenerationButtons.setVisibility(View.GONE);
            editPerformanceArray.setVisibility(View.GONE);
            btnRunBenchmark.setVisibility(View.GONE);
            btnToggleView.setText(R.string.show_performance);
        }

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

        btnPerfGenerateRandom.setOnClickListener(v -> {
            String lengthStr = editPerformanceArrayLength.getText().toString();
            int length = lengthStr.isEmpty() ? 10 : Integer.parseInt(lengthStr);

            if (length < 1 || length > 100) {
                Toast.makeText(this, getString(R.string.error_array_length_range), Toast.LENGTH_SHORT).show();
                return;
            }

            currentArray = com.example.vizualizatorsortiranja.utils.ArrayGenerator.generateRandom(length, 100);
            updateArrayTextField();
        });

        btnPerfGenerateGaussian.setOnClickListener(v -> {
            String lengthStr = editPerformanceArrayLength.getText().toString();
            int length = lengthStr.isEmpty() ? 10 : Integer.parseInt(lengthStr);

            if (length < 1 || length > 100) {
                Toast.makeText(this, getString(R.string.error_array_length_range), Toast.LENGTH_SHORT).show();
                return;
            }

            currentArray = com.example.vizualizatorsortiranja.utils.ArrayGenerator.generateGaussian(length, 100);
            updateArrayTextField();
        });

        btnPerfGenerateSorted.setOnClickListener(v -> {
            String lengthStr = editPerformanceArrayLength.getText().toString();
            int length = lengthStr.isEmpty() ? 10 : Integer.parseInt(lengthStr);

            if (length < 1 || length > 100) {
                Toast.makeText(this, getString(R.string.error_array_length_range), Toast.LENGTH_SHORT).show();
                return;
            }

            currentArray = com.example.vizualizatorsortiranja.utils.ArrayGenerator.generateSorted(length);
            updateArrayTextField();
        });

        btnPerfGenerateReversed.setOnClickListener(v -> {
            String lengthStr = editPerformanceArrayLength.getText().toString();
            int length = lengthStr.isEmpty() ? 10 : Integer.parseInt(lengthStr);

            if (length < 1 || length > 100) {
                Toast.makeText(this, getString(R.string.error_array_length_range), Toast.LENGTH_SHORT).show();
                return;
            }

            currentArray = com.example.vizualizatorsortiranja.utils.ArrayGenerator.generateReverseSorted(length);
            updateArrayTextField();
        });

        // Run benchmark button
        btnRunBenchmark.setOnClickListener(v -> {
            String arrayStr = editPerformanceArray.getText().toString().trim();

            if (arrayStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_enter_array_values), Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                String[] parts = arrayStr.split(",");
                int[] customArray = new int[parts.length];

                for (int i = 0; i < parts.length; i++) {
                    customArray[i] = Integer.parseInt(parts[i].trim());
                }

                currentArray = customArray;
                runBenchmark(customArray);

                if (isShowingTheory) {
                    viewFlipper.showNext();
                    isShowingTheory = false;
                    btnToggleView.setText(R.string.show_theory);
                    spinnerAlgorithm.setVisibility(android.view.View.GONE);
                    editPerformanceArray.setVisibility(android.view.View.VISIBLE);
                    btnRunBenchmark.setVisibility(android.view.View.VISIBLE);
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, getString(R.string.error_invalid_array_format), Toast.LENGTH_SHORT).show();
            }
        });

        // Toggle view button
        btnToggleView.setOnClickListener(v -> {
            viewFlipper.showNext();
            isShowingTheory = !isShowingTheory;

            if (isShowingTheory) {
                btnToggleView.setText(R.string.show_performance);
                textTheory.setText(theoryTexts[spinnerAlgorithm.getSelectedItemPosition()]);
                spinnerAlgorithm.setVisibility(View.VISIBLE);
                editPerformanceArray.setVisibility(View.GONE);
                btnRunBenchmark.setVisibility(View.GONE);
                layoutArrayLength.setVisibility(View.GONE);
                layoutGenerationButtons.setVisibility(View.GONE);
            } else {
                btnToggleView.setText(R.string.show_theory);
                spinnerAlgorithm.setVisibility(View.GONE);
                editPerformanceArray.setVisibility(View.VISIBLE);
                btnRunBenchmark.setVisibility(View.VISIBLE);

                if (!autoRunBenchmark) {
                    layoutArrayLength.setVisibility(View.VISIBLE);
                    layoutGenerationButtons.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void runBenchmark(int[] inputArray) {
        ArrayList<BarEntry> timeEntries = new ArrayList<>();
        ArrayList<BarEntry> swapEntries = new ArrayList<>();
        ArrayList<BarEntry> comparisonEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < algorithms.length; i++) {
            // Warm-up: Run algorithm 3 times to let JIT compile
            for (int warmup = 0; warmup < 3; warmup++) {
                int[] warmupArray = inputArray.clone();
                algorithms[i].sortForPerformanceWithSwaps(warmupArray);
            }

            // Force garbage collection before measurement
            System.gc();
            try {
                Thread.sleep(50); // Give GC time to complete
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Run multiple iterations and take median
            final int ITERATIONS = 5;
            long[] times = new long[ITERATIONS];
            int swapCount = 0;
            int comparisonCount = 0;

            for (int iter = 0; iter < ITERATIONS; iter++) {
                int[] testArray = inputArray.clone();
                PerformanceResult result = algorithms[i].sortForPerformanceWithSwaps(testArray);
                times[iter] = result.getExecutionTime();

                if (iter == ITERATIONS - 1) {
                    swapCount = result.getSwapCount();
                    comparisonCount = result.getComparisonCount();
                }
            }

            // Calculate median time (more robust than mean against outliers)
            java.util.Arrays.sort(times);
            long medianTime = times[ITERATIONS / 2];

            // Convert nanoseconds to milliseconds for better readability
            float timeInMs = medianTime / 1_000_000f;

            timeEntries.add(new BarEntry(i, timeInMs));
            swapEntries.add(new BarEntry(i, swapCount));
            comparisonEntries.add(new BarEntry(i, comparisonCount));
            labels.add(algorithms[i].getName());
        }

        // Create time dataset
        BarDataSet timeDataSet = new BarDataSet(timeEntries, getString(R.string.chart_execution_time));
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
        performanceChart.setExtraBottomOffset(50f);
        performanceChart.getLegend().setEnabled(false);

        XAxis timeXAxis = performanceChart.getXAxis();
        timeXAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        timeXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        timeXAxis.setGranularity(1f);
        timeXAxis.setGranularityEnabled(true);
        timeXAxis.setLabelRotationAngle(-30f);
        timeXAxis.setTextSize(9f);
        timeXAxis.setYOffset(5f);

        performanceChart.getAxisLeft().setAxisMinimum(0f);
        performanceChart.getAxisRight().setEnabled(false);

        performanceChart.setTouchEnabled(true);
        performanceChart.setDragEnabled(true);
        performanceChart.setScaleEnabled(false);
        performanceChart.setHighlightPerTapEnabled(true);

        CustomMarkerView timeMarker = new CustomMarkerView(this, R.layout.marker_view,
                labels.toArray(new String[0]), getString(R.string.unit_milliseconds));
        performanceChart.setMarker(timeMarker);

        performanceChart.invalidate();

        BarDataSet swapDataSet = new BarDataSet(swapEntries, getString(R.string.chart_number_of_swaps));
        swapDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        swapDataSet.setValueTextSize(12f);
        swapDataSet.setValueTextColor(Color.BLACK);

        BarData swapBarData = new BarData(swapDataSet);
        swapBarData.setBarWidth(0.8f);

        swapChart.setData(swapBarData);
        swapChart.getDescription().setEnabled(false);
        swapChart.setFitBars(true);
        swapChart.animateY(1000);
        swapChart.setExtraBottomOffset(50f);
        swapChart.getLegend().setEnabled(false);

        XAxis swapXAxis = swapChart.getXAxis();
        swapXAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        swapXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        swapXAxis.setGranularity(1f);
        swapXAxis.setGranularityEnabled(true);
        swapXAxis.setLabelRotationAngle(-30f);
        swapXAxis.setTextSize(9f);
        swapXAxis.setYOffset(5f);

        swapChart.getAxisLeft().setAxisMinimum(0f);
        swapChart.getAxisRight().setEnabled(false);

        // Enable touch and setup custom marker view for swap chart
        swapChart.setTouchEnabled(true);
        swapChart.setDragEnabled(true);
        swapChart.setScaleEnabled(false);
        swapChart.setHighlightPerTapEnabled(true);

        CustomMarkerView swapMarker = new CustomMarkerView(this, R.layout.marker_view,
                labels.toArray(new String[0]), getString(R.string.unit_swaps));
        swapChart.setMarker(swapMarker);

        swapChart.invalidate();

        // Create comparison dataset
        BarDataSet comparisonDataSet = new BarDataSet(comparisonEntries, getString(R.string.chart_number_of_comparisons));
        comparisonDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        comparisonDataSet.setValueTextSize(12f);
        comparisonDataSet.setValueTextColor(Color.BLACK);

        BarData comparisonBarData = new BarData(comparisonDataSet);
        comparisonBarData.setBarWidth(0.8f);

        comparisonChart.setData(comparisonBarData);
        comparisonChart.getDescription().setEnabled(false);
        comparisonChart.setFitBars(true);
        comparisonChart.animateY(1000);
        comparisonChart.setExtraBottomOffset(50f);
        comparisonChart.getLegend().setEnabled(false);

        // Configure comparison chart X axis
        XAxis comparisonXAxis = comparisonChart.getXAxis();
        comparisonXAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        comparisonXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        comparisonXAxis.setGranularity(1f);
        comparisonXAxis.setGranularityEnabled(true);
        comparisonXAxis.setLabelRotationAngle(-30f);
        comparisonXAxis.setTextSize(9f);
        comparisonXAxis.setYOffset(5f);

        // Configure comparison chart Y axis
        comparisonChart.getAxisLeft().setAxisMinimum(0f);
        comparisonChart.getAxisRight().setEnabled(false);

        // Enable touch and setup custom marker view for comparison chart
        comparisonChart.setTouchEnabled(true);
        comparisonChart.setDragEnabled(true);
        comparisonChart.setScaleEnabled(false);
        comparisonChart.setHighlightPerTapEnabled(true);

        CustomMarkerView comparisonMarker = new CustomMarkerView(this, R.layout.marker_view,
                labels.toArray(new String[0]), getString(R.string.unit_comparisons));
        comparisonChart.setMarker(comparisonMarker);

        comparisonChart.invalidate();
    }

    private void updateArrayTextField() {
        if (currentArray == null) return;

        StringBuilder arrayStr = new StringBuilder();
        for (int i = 0; i < currentArray.length; i++) {
            arrayStr.append(currentArray[i]);
            if (i < currentArray.length - 1) {
                arrayStr.append(",");
            }
        }
        editPerformanceArray.setText(arrayStr.toString());
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
