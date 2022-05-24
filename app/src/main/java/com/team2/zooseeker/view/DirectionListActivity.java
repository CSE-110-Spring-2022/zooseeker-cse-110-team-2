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
        directionListViewModel = new ViewModelProvider(this).get(DirectionListViewModel.class);
        directionListViewModel.populateList(adapter);
    }

    public void onNextButtonClicked(View view) {
        directionListViewModel.nextExhibit(adapter);
//        adapter.incrementNumToDisplay();
//        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        if (String.valueOf(nextButton.getText()).equals("Finish")){
            finish();
        }

        if (!directionListViewModel.exhibitsRemaining()){
            nextButton.setText("Finish");
        }

    }

    public void onPreviousButtonClicked(View view){
        boolean result = directionListViewModel.prevExhibit(adapter);
        if (!result) {
            finish();
        }

    }

}