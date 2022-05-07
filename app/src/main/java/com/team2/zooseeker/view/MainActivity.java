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
import com.team2.zooseeker.model.RouteModel;
import com.team2.zooseeker.viewModel.ExhibitListViewModel;
import com.team2.zooseeker.viewModel.SearchViewModel;

import java.io.IOException;
import java.util.ArrayList;

import cse110.ExhibitListAdapter;

public class MainActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public ExhibitListViewModel viewModel;
    private Button planButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        viewModel = new ViewModelProvider(this)
                .get(ExhibitListViewModel.class);

        ExhibitListAdapter adapter = new ExhibitListAdapter();
        adapter.setHasStableIds(true);
        adapter.setOnCheckBoxClickedListener(viewModel::toggleSelected);
        viewModel.getExhibitsList().observe(this, adapter::setExhibits);

        recyclerView = findViewById(R.id.exhibit_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        TextView searchBar = findViewById(R.id.search_bar);
        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.setUpSearch(searchBar, adapter);

        planButton = findViewById(R.id.plan_btn);
        planButton.setOnClickListener(this::generatePlan);



    }

    private void generatePlan(View view) {
        startActivity(new Intent(this, DirectionListActivity.class));
    }


    public void planBtnOnClickListener(View view) {
        ArrayList<String> list = new ArrayList<>();
        String dataFile = "sample_zoo_graph.json";
        ArrayList<String> plan = new ArrayList<>();

        //Currently uses mock list
        list.add("gators");
        list.add("gorillas");
        list.add("lions");

        try {
            RouteModel routeModel = new RouteModel(list, view.getContext().getAssets().open(dataFile));
            plan = routeModel.genRoute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //currently plan is not being used, but can be accessed after here
        //System.out.println(plan);
    }
}