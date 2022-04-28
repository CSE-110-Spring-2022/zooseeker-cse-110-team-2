package com.team2.zooseeker.viewModel;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import com.team2.zooseeker.model.SearchModel;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel {

    SearchModel searchModel = new SearchModel();

    public SearchViewModel(TextView searchBar){
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                Log.i("search", "----");
                List<String> searchResults = searchModel.search(String.valueOf(charSequence), count);
                for(String result: searchResults){
                    Log.i("search", result);
                }
                Log.i("search", "----");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


}
