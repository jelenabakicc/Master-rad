package com.example.vizualizatorsortiranja;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vizualizatorsortiranja.algorithms.BubbleSort;
import com.example.vizualizatorsortiranja.algorithms.InsertionSort;
import com.example.vizualizatorsortiranja.algorithms.SelectionSort;
import com.example.vizualizatorsortiranja.algorithms.QuickSort;
import com.example.vizualizatorsortiranja.algorithms.MergeSort;
import com.example.vizualizatorsortiranja.algorithms.HeapSort;
import com.example.vizualizatorsortiranja.algorithms.SortingAlgorithm;
import com.example.vizualizatorsortiranja.models.SortStep;
import com.example.vizualizatorsortiranja.utils.ArrayGenerator;
import com.example.vizualizatorsortiranja.visualization.ArrayBarView;
import java.util.List;

/**
 * Activity for visualizing sorting algorithms in real-time.
 * Shows step-by-step execution with animations.
 */
public class VisualizationActivity extends AppCompatActivity {

    private ArrayBarView arrayBarView;
    private Spinner spinnerAlgorithm;
    private EditText editArrayLength;
    private EditText editCustomArray;
    private Button btnGenerateRandom;
    private Button btnSetArray;
    private Button btnStart;
    private Button btnPause;
    private Button btnReset;
    private SeekBar seekBarSpeed;
    private TextView textSpeedValue;
    private TextView textKeyValue;
    private TextView textCompareValues;

    private SortingAlgorithm[] algorithms;
    private int[] originalArray;
    private List<SortStep> sortSteps;
    private int currentStep = 0;
    private boolean isPaused = false;
    private boolean isSorting = false;
    private Handler handler = new Handler();
    private static final int BASE_DELAY = 500; // base milliseconds between steps
    private int animationDelay = BASE_DELAY; // current delay (adjustable)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization);

        // Initialize views
        arrayBarView = findViewById(R.id.array_bar_view);
        spinnerAlgorithm = findViewById(R.id.spinner_algorithm);
        editArrayLength = findViewById(R.id.edit_array_length);
        editCustomArray = findViewById(R.id.edit_custom_array);
        btnGenerateRandom = findViewById(R.id.btn_generate_random);
        btnSetArray = findViewById(R.id.btn_set_array);
        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnReset = findViewById(R.id.btn_reset);
        seekBarSpeed = findViewById(R.id.seekbar_speed);
        textSpeedValue = findViewById(R.id.text_speed_value);
        textKeyValue = findViewById(R.id.text_key_value);
        textCompareValues = findViewById(R.id.text_compare_values);

        // Initialize algorithms
        algorithms = new SortingAlgorithm[] {
            new BubbleSort(),
            new InsertionSort(),
            new SelectionSort(),
            new QuickSort(),
            new MergeSort(),
            new HeapSort()
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

        // Set default array
        originalArray = ArrayGenerator.generateRandom(10, 100);
        arrayBarView.setArray(originalArray);

        // Setup speed SeekBar
        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Map progress (0-100) to speed multiplier (0.1x to 5x)
                // Progress 50 = 1x speed (BASE_DELAY = 500ms)
                // Progress 0 = 5x slower (2500ms)
                // Progress 100 = 10x faster (50ms)
                float speedMultiplier;
                if (progress < 50) {
                    // Slower: 0-49 maps to 5x-1x slower (2500ms to 500ms)
                    speedMultiplier = 5.0f - (progress / 50.0f) * 4.0f;
                } else {
                    // Faster: 50-100 maps to 1x-10x faster (500ms to 50ms)
                    speedMultiplier = 1.0f / (1.0f + ((progress - 50) / 50.0f) * 9.0f);
                }
                animationDelay = (int) (BASE_DELAY * speedMultiplier);

                // Update speed label
                if (progress < 50) {
                    float displaySpeed = 1.0f / speedMultiplier;
                    textSpeedValue.setText(String.format("%.1fx", displaySpeed));
                } else {
                    float displaySpeed = 1.0f / speedMultiplier;
                    textSpeedValue.setText(String.format("%.0fx", displaySpeed));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Generate random array button
        btnGenerateRandom.setOnClickListener(v -> {
            if (isSorting) {
                Toast.makeText(this, "Please stop current sorting first", Toast.LENGTH_SHORT).show();
                return;
            }

            String lengthStr = editArrayLength.getText().toString();
            int length = lengthStr.isEmpty() ? 10 : Integer.parseInt(lengthStr);

            if (length < 1 || length > 100) {
                Toast.makeText(this, "Please enter array length between 1 and 100", Toast.LENGTH_SHORT).show();
                return;
            }

            originalArray = ArrayGenerator.generateRandom(length, 100);
            arrayBarView.setArray(originalArray);
            editCustomArray.setText(""); // Clear custom array field
        });

        // Set custom array button
        btnSetArray.setOnClickListener(v -> {
            if (isSorting) {
                Toast.makeText(this, "Please stop current sorting first", Toast.LENGTH_SHORT).show();
                return;
            }

            String arrayStr = editCustomArray.getText().toString().trim();

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

                originalArray = customArray;
                arrayBarView.setArray(originalArray);
                editArrayLength.setText(String.valueOf(customArray.length));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid array format. Use comma-separated numbers", Toast.LENGTH_SHORT).show();
            }
        });

        // Start button - begin sorting animation
        btnStart.setOnClickListener(v -> {
            if (isSorting) {
                Toast.makeText(this, "Sorting already in progress", Toast.LENGTH_SHORT).show();
                return;
            }

            SortingAlgorithm selectedAlgorithm = algorithms[spinnerAlgorithm.getSelectedItemPosition()];
            sortSteps = selectedAlgorithm.sortWithSteps(originalArray.clone());
            currentStep = 0;
            isSorting = true;
            isPaused = false;
            btnPause.setText("Pause");
            playNextStep();
        });

        // Pause button - pause/resume animation
        btnPause.setOnClickListener(v -> {
            if (!isSorting) {
                Toast.makeText(this, "No sorting in progress", Toast.LENGTH_SHORT).show();
                return;
            }

            isPaused = !isPaused;
            btnPause.setText(isPaused ? "Resume" : "Pause");

            if (!isPaused) {
                playNextStep();
            }
        });

        // Reset button - restore original array
        btnReset.setOnClickListener(v -> {
            handler.removeCallbacksAndMessages(null);
            isSorting = false;
            isPaused = false;
            currentStep = 0;
            btnPause.setText("Pause");
            arrayBarView.setArray(originalArray);
            arrayBarView.setHighlightIndices(-1, -1);
            textKeyValue.setVisibility(TextView.GONE);
            textCompareValues.setVisibility(TextView.GONE);
        });
    }

    private void playNextStep() {
        if (!isSorting || isPaused || sortSteps == null || currentStep >= sortSteps.size()) {
            if (currentStep >= sortSteps.size()) {
                isSorting = false;
                arrayBarView.setHighlightIndices(-1, -1);
                textKeyValue.setVisibility(TextView.GONE);
                textCompareValues.setVisibility(TextView.GONE);
            }
            return;
        }

        SortStep step = sortSteps.get(currentStep);
        arrayBarView.setArray(step.getArrayState());
        arrayBarView.setHighlightIndices(step.getIndex1(), step.getIndex2());

        // Handle helper arrays for Merge Sort
        if (step.getLeftArray() != null && step.getRightArray() != null) {
            arrayBarView.setHelperArrays(step.getLeftArray(), step.getRightArray(),
                                        step.getLeftHighlight(), step.getRightHighlight());
        } else {
            arrayBarView.clearHelperArrays();
        }

        // Display key value if present
        if (step.getKeyValue() != null) {
            textKeyValue.setText("Key: " + step.getKeyValue());
            textKeyValue.setVisibility(TextView.VISIBLE);
        } else {
            textKeyValue.setVisibility(TextView.GONE);
        }

        // Display comparison values if present (for Merge Sort)
        if (step.getCompareValue1() != null && step.getCompareValue2() != null) {
            textCompareValues.setText("Comparing: " + step.getCompareValue1() + " vs " + step.getCompareValue2());
            textCompareValues.setVisibility(TextView.VISIBLE);
        } else if (step.getCompareValue1() != null && step.getCompareValue2() == null) {
            // For SWAP steps, show which value is being placed
            textCompareValues.setText("Swapping: " + step.getCompareValue1());
            textCompareValues.setVisibility(TextView.VISIBLE);
        } else {
            textCompareValues.setVisibility(TextView.GONE);
        }

        currentStep++;

        handler.postDelayed(this::playNextStep, animationDelay);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
