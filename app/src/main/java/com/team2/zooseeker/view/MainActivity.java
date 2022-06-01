package com.team2.zooseeker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.team2.zooseeker.R;
import com.team2.zooseeker.model.ExhibitModel;
import com.team2.zooseeker.model.SearchModel;
import com.team2.zooseeker.viewModel.ExhibitListViewModel;
import com.team2.zooseeker.viewModel.SearchViewModel;

import com.team2.zooseeker.viewModel.ExhibitListAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public ExhibitListViewModel viewModel;
    private Button planButton;
    private CheckBox showSelected;
    public TextView counter;
    public ExhibitListAdapter adapter;
    public AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this)
                .get(ExhibitListViewModel.class);
        adapter = new ExhibitListAdapter();
        adapter.setHasStableIds(true);
        adapter.setOnCheckBoxClickedListener(viewModel::toggleSelected);
        viewModel.getExhibitsList(findViewById(R.id.Show_selected).isSelected()).observe(this, adapter::setExhibits);

        // RecyclerView setup connecting with adapter to display list of exhibits
        recyclerView = findViewById(R.id.exhibit_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        TextView searchBar = findViewById(R.id.search_bar);
        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.setUpSearch(searchBar, adapter);

        planButton = findViewById(R.id.plan_btn);
        TextView counter = findViewById(R.id.counter);
        counter.setText(String.valueOf(viewModel.getCounter()));
        adapter.setCounter(counter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.setMessage("Please select an exhibit to plan a route.").setCancelable(true).setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
            }
        }).create();

        showSelected = findViewById(R.id.Show_selected);
        showSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    List<ExhibitModel> exhibitsList;
                    if(isChecked){
                        exhibitsList = viewModel.getExhibitsListDao().getShowSelectedList(true);
                    }
                    else{
                        exhibitsList = viewModel.getExhibitsListDao().getExhibits("exhibit");
                    }
                    adapter.setExhibits(exhibitsList);
                }
            }
        );
    }

    public void planBtnOnClickListener(View view) {
        int count = viewModel.getCounter();

        if(count > 0){
            Intent intent = new Intent(this, DirectionListActivity.class);
            startActivity(intent);
        } else {

            dialog.show();

        }

    }
}