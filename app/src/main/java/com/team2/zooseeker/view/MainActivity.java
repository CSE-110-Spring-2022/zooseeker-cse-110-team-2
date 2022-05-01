package com.team2.zooseeker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.team2.zooseeker.R;
import com.team2.zooseeker.model.RouteModel;
import com.team2.zooseeker.viewModel.SearchViewModel;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView searchBar = findViewById(R.id.search_bar);
        SearchViewModel searchInstance = new SearchViewModel(searchBar);

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