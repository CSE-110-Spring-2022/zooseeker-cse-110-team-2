package com.team2.zooseeker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import java.util.List;
import java.util.Map;

import cse110.Exhibit;
import cse110.ExhibitListAdapter;
import cse110.ZooData;

public class MainActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public ExhibitListViewModel viewModel;
    private Button showSelected;
    public ExhibitListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView searchBar = findViewById(R.id.search_bar);
        SearchViewModel searchInstance = new SearchViewModel(searchBar);

        viewModel = new ViewModelProvider(this)
                .get(ExhibitListViewModel.class);

        adapter = new ExhibitListAdapter();
        adapter.setHasStableIds(true);
        Context context = getApplication().getApplicationContext();
        try {
            Map<String, ZooData.VertexInfo> map = ZooData.loadVertexInfoJSON(context, "src/main/assets/sample_node_info.json");
            List<Exhibit> exhibits = Exhibit.convert(map);
            adapter.setExhibits(exhibits);
            adapter.setOnCheckBoxClickedListener(viewModel::toggleSelected);
            viewModel.getExhibitsList().observe(this, adapter::setExhibits);

            recyclerView = findViewById(R.id.exhibit_items);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }


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

    public ExhibitListAdapter getAdapter(){
        return adapter;
    }
}