package com.team2.zooseeker.model;

import java.util.ArrayList;
import java.util.List;

public class SearchModel {
    public static List<ExhibitModel> allExhbits;

    public SearchModel(List<ExhibitModel> data) {
        allExhbits = data;
    }

    public List<ExhibitModel> search(String searchQuery, int size){
        List<ExhibitModel> allExhibitsCopy = new ArrayList<ExhibitModel>(allExhbits);
        if (searchQuery.equals("")){
            return allExhibitsCopy;
        }
        ArrayList<ExhibitModel> result = new ArrayList<ExhibitModel>();
        String searchQueryLower = searchQuery.toLowerCase();

        for(ExhibitModel data: allExhibitsCopy){
            String currentName = data.name.toLowerCase();
            String tags = data.tags.toLowerCase();
            if (currentName.length() >= size && (searchQueryLower.substring(0, size)
                    .equals(currentName.substring(0, size))
                    || tags.indexOf(searchQueryLower.substring(0, size)) != -1)){
                result.add(data);
            }
        }
        return result;
    }

    public static void setExhibitSelected(ExhibitModel exhibitModel){
        for (ExhibitModel current: allExhbits){
            if(exhibitModel.name.equals(current.name)){
                current.selected = exhibitModel.selected;
                return;
            }
        }
    }

    public static int getCount(){
        List<ExhibitModel> allExhibitModels = SearchModel.allExhbits;
        int count = 0;
        for(ExhibitModel exhibitModel : allExhibitModels){
            if (exhibitModel.selected){
                count++;
            }
        }
        return count;
    }
}
