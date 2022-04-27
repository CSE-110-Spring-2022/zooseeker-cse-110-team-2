package com.team2.zooseeker;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Search {
    ArrayList<String> mockData = new ArrayList<String>();
    public Search(TextView searchBar){
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                Log.i("search", "----");
                List<String> searchResults = search(String.valueOf(charSequence), count);
                for(String result: searchResults){
                    Log.i("search", result);
                }
                Log.i("search", "----");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mockData.add("bear");
        mockData.add("tiger");
        mockData.add("elephant");
        mockData.add("bird");
        mockData.add("bee");

    }

    public List<String> search(String searchQuery, int size){
        ArrayList<String> result = new ArrayList<String>();
        for(String data: mockData){
            if (searchQuery.substring(0, size).equals(data.substring(0, size))){
                result.add(data);
            }
        }
        return result;
    }


}
