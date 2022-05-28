package com.team2.zooseeker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.team2.zooseeker.R;
import com.team2.zooseeker.viewModel.DirectionModeManager;

public class SettingActivity extends AppCompatActivity {

    private Button detailedDirectionButton;
    private Button briefDirectionButton;
    private Button summaryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        detailedDirectionButton = findViewById(R.id.detailed_direction_button);
        briefDirectionButton = findViewById(R.id.brief_direction_button);
        summaryButton = findViewById(R.id.summary_button);
    }

    public void onDetailedDirectionButton(View view) {
        DirectionModeManager.getSingleton().setDetailedMode();
        finish();
    }

    public void onBriefDirectionButton(View view) {
        DirectionModeManager.getSingleton().setBriefMode();
        finish();
    }

    public void onSummaryButton(View view) {
        Intent intent = new Intent(this, RouteSummaryActivity.class);
        startActivity(intent);
    }
}