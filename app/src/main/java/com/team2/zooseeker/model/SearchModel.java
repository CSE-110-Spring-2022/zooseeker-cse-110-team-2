package com.team2.zooseeker.model;

import java.util.ArrayList;
import java.util.List;

public class SearchModel {
    ArrayList<String> mockData;

    public SearchModel(){
        mockData = new ArrayList<String>();
        mockData.add("bear");
        mockData.add("tiger");
        mockData.add("elephant");
        mockData.add("bird");
        mockData.add("bee");
    }

    public SearchModel(ArrayList<String> data) {
        mockData = data;
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
