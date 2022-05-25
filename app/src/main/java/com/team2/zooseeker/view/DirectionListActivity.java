package com.team2.zooseeker.view;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.team2.zooseeker.R;
import com.team2.zooseeker.model.PermissionChecker;
import com.team2.zooseeker.model.ReplanModel;
import com.team2.zooseeker.viewModel.DirectionListAdapter;
import com.team2.zooseeker.viewModel.DirectionListViewModel;

import org.w3c.dom.Text;

public class DirectionListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    private DirectionListViewModel directionListViewModel;
    private DirectionListAdapter adapter;
    private Button nextButton;
    private Button previousButton;
    private TextView previousDisplay;
    private TextView nextDisplay;

    /**
     * Initialize DirectionListActivity with lists of strings and next button
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_list);

        adapter = new DirectionListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.direction_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        nextButton = findViewById(R.id.next_button);
        nextButton.setText("NEXT");

        previousDisplay = findViewById(R.id.prev_display);
        nextDisplay = findViewById(R.id.next_display);

        previousButton = findViewById(R.id.backButton);
        previousButton.setText("BACK");
        directionListViewModel = new ViewModelProvider(this).get(DirectionListViewModel.class);
        directionListViewModel.populateList(adapter, previousDisplay, nextDisplay);

        PermissionChecker perms = new PermissionChecker(this);
        if (!perms.ensurePermissions()) {
            directionListViewModel.autoUpdateRoute(this, adapter, previousDisplay, nextDisplay);
        }
    }

    public void onNextButtonClicked(View view) {
        directionListViewModel.nextExhibit(adapter, previousDisplay, nextDisplay);
//        adapter.incrementNumToDisplay();
//        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        if (String.valueOf(nextButton.getText()).equals("Finish")){
            finish();
        }

        if (!directionListViewModel.exhibitsRemaining()){
            nextButton.setText("Finish");
        }
    }

    public void onPreviousButtonClicked(View view){
        boolean result = directionListViewModel.prevExhibit(adapter, previousDisplay, nextDisplay);
        if (!result) {
            finish();
        }
    }

    public void onSkipButtonClicked(View view) {
        directionListViewModel.skipExhibit(adapter, previousDisplay, nextDisplay);
        if (String.valueOf(nextButton.getText()).equals("Finish")){
            finish();
        }
        if (!directionListViewModel.exhibitsRemaining()){
            nextButton.setText("Finish");
        }
    }
}