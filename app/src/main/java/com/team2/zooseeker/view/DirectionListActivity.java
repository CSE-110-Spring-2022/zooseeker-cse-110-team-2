package com.team2.zooseeker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
        directionListViewModel = new ViewModelProvider(this).get(DirectionListViewModel.class);
        directionListViewModel.populateList(adapter);
    }

    public void onNextButtonClicked(View view) {
        if (String.valueOf(nextButton.getText()).equals("Finish")){
            finish();
        }

        int result = adapter.incrementNumToDisplay();
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);

        if (result >= adapter.directions.size()){
            nextButton.setText("Finish");
        }

    }

    public void onPreviousButtonClicked(View view){
        boolean result = adapter.decrementNumToDisplay();
        if (result){
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        } else {
            finish();
        }
    }

    public void onSummaryButtonClicked(View view) {
        Intent intent = new Intent(this, RouteSummaryActivity.class);
        startActivity(intent);
    }

}