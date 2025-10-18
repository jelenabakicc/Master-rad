package com.example.vizualizatorsortiranja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroupLanguage;
    private RadioButton radioEnglish;
    private RadioButton radioSerbian;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load saved language preference before setting content view
        preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String savedLanguage = preferences.getString("Language", "en");
        setLocale(savedLanguage, false); // false = don't recreate activity

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btnVisualize = findViewById(R.id.btn_visualize);
        Button btnPerformance = findViewById(R.id.btn_performance);
        radioGroupLanguage = findViewById(R.id.radio_group_language);
        radioEnglish = findViewById(R.id.radio_english);
        radioSerbian = findViewById(R.id.radio_serbian);

        if (savedLanguage.equals("sr")) {
            radioSerbian.setChecked(true);
        } else {
            radioEnglish.setChecked(true);
        }

        radioGroupLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_english) {
                    setLocale("en", true);
                } else if (checkedId == R.id.radio_serbian) {
                    setLocale("sr", true);
                }
            }
        });

        btnVisualize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VisualizationActivity.class);
                startActivity(intent);
            }
        });

        btnPerformance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PerformanceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setLocale(String languageCode, boolean recreate) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Language", languageCode);
        editor.apply();

        // Recreate activity to apply changes
        if (recreate) {
            recreate();
        }
    }
}