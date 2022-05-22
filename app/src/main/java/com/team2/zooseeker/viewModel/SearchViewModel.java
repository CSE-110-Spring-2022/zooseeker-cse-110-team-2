package com.team2.zooseeker.viewModel;

import android.app.Application;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.team2.zooseeker.model.SearchModel;

import java.util.List;

import com.team2.zooseeker.model.ExhibitModel;

import com.team2.zooseeker.model.ExhibitsListDao;
import com.team2.zooseeker.model.ExhibitsListDatabase;

public class SearchViewModel extends AndroidViewModel {

    SearchModel searchModel;
    private final ExhibitsListDao exhibitsListDao;
    TextView searchBar;
    ExhibitListAdapter adapter;

    public SearchViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        ExhibitsListDatabase db = ExhibitsListDatabase.getSingleton(context);
        exhibitsListDao = db.exhibitsListDao();
        List<ExhibitModel> exhibitModels = exhibitsListDao.getExhibits("exhibit");
        searchModel = new SearchModel(exhibitModels);
    }

    public void setUpSearch(TextView search, ExhibitListAdapter exhibitAdapter){
        searchBar = search;
        adapter = exhibitAdapter;
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                List<ExhibitModel> searchResults = searchModel
                        .search(String.valueOf(charSequence), count);
                adapter.setExhibits(searchResults);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }


}
