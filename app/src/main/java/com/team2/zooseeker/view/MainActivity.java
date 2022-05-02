package com.team2.zooseeker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.team2.zooseeker.R;
import com.team2.zooseeker.viewModel.SearchViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView searchBar = findViewById(R.id.search_bar);
        SearchViewModel searchInstance = new SearchViewModel(searchBar);
    }


}