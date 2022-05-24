package com.team2.zooseeker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.team2.zooseeker.R;
import com.team2.zooseeker.viewModel.DirectionListAdapter;
import com.team2.zooseeker.viewModel.DirectionListViewModel;

public class DirectionListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    private DirectionListViewModel directionListViewModel;
    private DirectionListAdapter adapter;
    private Button nextButton;
    private Button previousButton;

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

        previousButton = findViewById(R.id.backButton);
        previousButton.setText("BACK");
        previousButton.setVisibility(View.INVISIBLE);
        directionListViewModel = new ViewModelProvider(this).get(DirectionListViewModel.class);
        directionListViewModel.populateList(adapter);
    }

    public void onNextButtonClicked(View view) {
        int result = adapter.incrementNumToDisplay();
        Log.d("direction", String.valueOf(adapter.directions.size()));
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        if (result >= adapter.directions.size()){
            nextButton.setText("Finish");
        }

        if (result > 0){
            previousButton.setVisibility(View.VISIBLE);
        }

    }

    public void onPreviousButtonClicked(View view){
        int result = adapter.decrementNumToDisplay();
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);

    }

}