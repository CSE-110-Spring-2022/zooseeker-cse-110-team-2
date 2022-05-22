package com.team2.zooseeker.view;

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

import com.team2.zooseeker.R;
import com.team2.zooseeker.model.PermissionChecker;
import com.team2.zooseeker.model.ReplanModel;
import com.team2.zooseeker.viewModel.DirectionListAdapter;
import com.team2.zooseeker.viewModel.DirectionListViewModel;

public class DirectionListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    private DirectionListViewModel directionListViewModel;
    private DirectionListAdapter adapter;
    private Button nextButton;

    /**
     * Initialize DirectionListActivity with lists of strings and next button
     */
    @SuppressLint("MissingPermission")
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
        directionListViewModel = new ViewModelProvider(this).get(DirectionListViewModel.class);
        directionListViewModel.populateList(adapter);

        ReplanModel replan = new ReplanModel(this, "zoo_node_info.json");

        // Get Location Permissions
        PermissionChecker perms = new PermissionChecker(this);
        if (perms.ensurePermissions()) {
            return;
        }; // TODO: Account for user denying permissions
        //TODO: move to viewmodel?

        // Listen for Location Updates
        {
            String provider = LocationManager.GPS_PROVIDER;
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = location -> {
                Log.d("DEBUG", String.format("Location changed: %s", location));
//                if (replan.offTrack(location)) { //TODO: needs nodes at each end of current edge to pass in
//                    //TODO: Prompt user (once) to re-plan route
//                }
            };
            locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);
        }
    }

    public void onNextButtonClicked(View view) {
        adapter.incrementNumToDisplay();
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

}