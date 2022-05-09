package com.team2.zooseeker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.team2.zooseeker.R;
import com.team2.zooseeker.model.SearchModel;
import com.team2.zooseeker.viewModel.ExhibitListViewModel;
import com.team2.zooseeker.viewModel.SearchViewModel;

import cse110.ExhibitListAdapter;

public class MainActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public ExhibitListViewModel viewModel;
    private Button planButton;
    private TextView counter;
    ExhibitListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this)
                .get(ExhibitListViewModel.class);
        adapter = new ExhibitListAdapter();
        adapter.setHasStableIds(true);
        adapter.setOnCheckBoxClickedListener(viewModel::toggleSelected);
        viewModel.getExhibitsList().observe(this, adapter::setExhibits);

        // RecyclerView setup connecting with adapter to display list of exhibits
        recyclerView = findViewById(R.id.exhibit_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        TextView searchBar = findViewById(R.id.search_bar);
        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.setUpSearch(searchBar, adapter);

        planButton = findViewById(R.id.plan_btn);
        TextView counter = findViewById(R.id.counter);
        counter.setText(String.valueOf(SearchModel.getCount()));
        adapter.setCounter(counter);
    }

    public void planBtnOnClickListener(View view) {
        Intent intent = new Intent(this, DirectionListActivity.class);
        startActivity(intent);
    }
}