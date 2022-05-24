package com.team2.zooseeker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.team2.zooseeker.R;
import com.team2.zooseeker.viewModel.RouteSummaryAdapter;
import com.team2.zooseeker.viewModel.RouteSummaryViewModel;

public class RouteSummaryActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RouteSummaryAdapter routeSummaryAdapter;
    public RouteSummaryViewModel routeSummaryViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_summary);

        routeSummaryAdapter = new RouteSummaryAdapter();
        routeSummaryAdapter.setHasStableIds(true);

        recyclerView = getI
    }
}