package com.team2.zooseeker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.team2.zooseeker.R;
import com.team2.zooseeker.model.DirectionModel;
import com.team2.zooseeker.viewModel.DirectionListAdapter;
import com.team2.zooseeker.viewModel.DirectionListViewModel;

import java.util.List;

public class DirectionListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    DirectionListViewModel directionListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_list);

        DirectionListAdapter adapter = new DirectionListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.direction_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        directionListViewModel = new ViewModelProvider(this).get(DirectionListViewModel.class);
        directionListViewModel.populateList(adapter);
    }
}