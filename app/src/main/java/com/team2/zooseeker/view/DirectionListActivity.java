package com.team2.zooseeker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.team2.zooseeker.R;
import com.team2.zooseeker.model.DirectionModel;
import com.team2.zooseeker.viewModel.DirectionListAdapter;

import java.util.List;

public class DirectionListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_list);

        DirectionListAdapter adapter = new DirectionListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.direction_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setDirectionModels(DirectionModel
                .loadJSON(this, "sample_edge_info.json"));

        List<DirectionModel> directions = DirectionModel
                .loadJSON(this, "sample_edge_info.json");
        Log.d("DirectionListActivity", directions.toString());
    }
}