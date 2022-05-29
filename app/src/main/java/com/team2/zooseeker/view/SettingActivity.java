package com.team2.zooseeker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.team2.zooseeker.R;
import com.team2.zooseeker.viewModel.DirectionModeManager;
import com.team2.zooseeker.model.ExhibitModel;
import com.team2.zooseeker.viewModel.ExhibitListViewModel;
import com.team2.zooseeker.viewModel.MockLocationStore;

import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private Button detailedDirectionButton;
    private Button briefDirectionButton;
    private Button summaryButton;
    private Button deletePlanButton;

    private EditText latitudeInput;
    private EditText longitudeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        detailedDirectionButton = findViewById(R.id.detailed_direction_button);
        briefDirectionButton = findViewById(R.id.brief_direction_button);
        summaryButton = findViewById(R.id.summary_button);
        deletePlanButton = findViewById(R.id.deletePlan);
        latitudeInput = findViewById(R.id.latitiude_input);
        longitudeInput = findViewById(R.id.longitude_input);

        latitudeInput.setText(Double.toString(MockLocationStore.getSingleton().getLatitude()));
        longitudeInput.setText(Double.toString(MockLocationStore.getSingleton().getLongitude()));
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

    public void onDeletePlan(View view){
        ExhibitListViewModel viewModel = new ViewModelProvider(this)
                .get(ExhibitListViewModel.class);
        List<ExhibitModel> exhibitsList = viewModel.getExhibitsListDao().getShowSelectedList(true);
        for(ExhibitModel selectedExhibit : exhibitsList){
            selectedExhibit.selected = false;
            viewModel.getExhibitsListDao().update(selectedExhibit);
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DEBUG SETTINGS", "687");
        MockLocationStore.getSingleton().setLocation(Double.parseDouble(latitudeInput.getEditableText().toString()), Double.parseDouble(longitudeInput.getEditableText().toString()));
    }
}