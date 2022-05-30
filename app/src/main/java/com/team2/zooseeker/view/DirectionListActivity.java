package com.team2.zooseeker.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team2.zooseeker.R;
import com.team2.zooseeker.model.PermissionChecker;
import com.team2.zooseeker.viewModel.DirectionListAdapter;
import com.team2.zooseeker.viewModel.DirectionListViewModel;
import com.team2.zooseeker.viewModel.MockLocationStore;

public class DirectionListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    private DirectionListViewModel directionListViewModel;
    private DirectionListAdapter adapter;
    private Button nextButton;
    private Button previousButton;
    private ImageButton settingButton;
    private ImageButton reloadButton;
    private TextView previousDisplay;
    private TextView nextDisplay;

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

        settingButton = findViewById(R.id.setting_button);
        reloadButton = findViewById(R.id.reload_button);
        previousDisplay = findViewById(R.id.prev_display);
        nextDisplay = findViewById(R.id.next_display);

        previousButton = findViewById(R.id.backButton);
        previousButton.setText("BACK");
        directionListViewModel = new ViewModelProvider(this).get(DirectionListViewModel.class);
        directionListViewModel.populateList(adapter, previousDisplay, nextDisplay);

        MockLocationStore.getSingleton().setEnabled(true);

        PermissionChecker perms = new PermissionChecker(this);
        if (!perms.ensurePermissions()) {
            directionListViewModel.autoUpdateRoute(this, adapter, previousDisplay, nextDisplay);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        directionListViewModel.reloadDirections(adapter, previousDisplay, nextDisplay);
    }

    public void onNextButtonClicked(View view) {
        directionListViewModel.nextExhibit(adapter, previousDisplay, nextDisplay);
        if (String.valueOf(nextButton.getText()).equals("Finish")){
            finish();
        }

        if (!directionListViewModel.exhibitsRemaining()){
            nextButton.setText("Finish");
        }
    }

    public void onPreviousButtonClicked(View view){
        boolean result = directionListViewModel.prevExhibit(adapter, previousDisplay, nextDisplay);
        if (directionListViewModel.exhibitsRemaining()) nextButton.setText("Next");
        if (!result) {
            finish();
        }
    }

    public void onSkipButtonClicked(View view) {
        if (String.valueOf(nextButton.getText()).equals("Finish")){
            finish();
        }
        directionListViewModel.skipExhibit(adapter, previousDisplay, nextDisplay);
        if (!directionListViewModel.exhibitsRemaining()){
            nextButton.setText("Finish");
        }
    }

    public void onSettingButtonClicked(View view) {
        onPause();
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    public void onReloadClicked(View view) {
        if (!PermissionChecker.hasPermissions(this)) {
            MockLocationStore.getSingleton().setEnabled(false);
        }
        directionListViewModel.reloadAutoUpdate(this, adapter, previousDisplay, nextDisplay);
    }
}