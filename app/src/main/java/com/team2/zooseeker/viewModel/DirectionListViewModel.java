package com.team2.zooseeker.viewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.team2.zooseeker.model.RouteModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cse110.Exhibit;
import cse110.ExhibitsListDao;
import cse110.ExhibitsListDatabase;

public class DirectionListViewModel extends AndroidViewModel {

    private final ExhibitsListDao exhibitsListDao;
    private RouteModel routeModel;

    public DirectionListViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        ExhibitsListDatabase db = ExhibitsListDatabase.getSingleton(context);
        exhibitsListDao = db.exhibitsListDao();
        try {
            routeModel = new RouteModel(application.getAssets().open("sample_zoo_graph.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateList(DirectionListAdapter adapter) {
        ArrayList<String> exhibits = Exhibit.getExhibitNames(exhibitsListDao.getAllSelected(true));
        routeModel.setExhibits(exhibits);
        Log.d("DEBUG EXHIBITS", exhibits.toString());
        ArrayList<String> route = routeModel.genRoute();
        Log.d("DEBUG ROUTE", route.toString());
    }
}
