package com.team2.zooseeker.model;

import android.content.Context;

import androidx.lifecycle.AndroidViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;

import cse110.Exhibit;
import cse110.ExhibitsListDao;
import cse110.ExhibitsListDatabase;
import cse110.ZooData;

public class SearchModel {
    private final List<Exhibit> allExhbits;
//    Context context;

    public SearchModel(List<Exhibit> data) {
        allExhbits = data;
//        context = appContext;
    }

    public List<Exhibit> search(String searchQuery, int size){
        List<Exhibit> allExhibitsCopy = new ArrayList<Exhibit>(allExhbits);
        if (searchQuery.equals("")){
            return allExhibitsCopy;
        }
        ArrayList<Exhibit> result = new ArrayList<Exhibit>();
        String searchQueryLower = searchQuery.toLowerCase();

        for(Exhibit data: allExhibitsCopy){
            String currentName = data.name.toLowerCase();
            if (currentName.length() >= size && searchQueryLower.substring(0, size).equals(currentName.substring(0, size))){
                result.add(data);
            }
        }
        return result;
    }

//    public List<Exhibit> getAllExhibits(){
//        Map<String, ZooData.VertexInfo> exhibits = null;
//
//        try {
//            exhibits = ZooData
//                    .loadVertexInfoJSON(context, "sample_node_info.json");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        List<Exhibit> exhibitsList = Exhibit.convert(exhibits);
//        return exhibitsList;
//
//    }
}
