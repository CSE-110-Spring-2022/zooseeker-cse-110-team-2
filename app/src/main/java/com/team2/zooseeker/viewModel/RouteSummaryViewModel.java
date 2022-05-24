package com.team2.zooseeker.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.team2.zooseeker.model.PathDao;
import com.team2.zooseeker.model.PathDatabase;

public class RouteSummaryViewModel extends AndroidViewModel {

    private PathDao pathDao;

    public RouteSummaryViewModel(@NonNull Application application) {
        super(application);
        Context c = application.getApplicationContext();
        pathDao = PathDatabase.getSingleton(c).pathDao();
    }

    public void populateExhibits(RouteSummaryAdapter adp) {
        adp.setExhibits(pathDao.getAll());

    }

}
