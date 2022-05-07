package com.team2.zooseeker.viewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cse110.Exhibit;
import cse110.ExhibitsListDao;
import cse110.ExhibitsListDatabase;
import cse110.ZooData;

public class ExhibitListViewModel extends AndroidViewModel {

    private LiveData<List<Exhibit>> exhibits;
    private final ExhibitsListDao exhibitsListDao;

    public ExhibitListViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        ExhibitsListDatabase db = ExhibitsListDatabase.getSingleton(context);
        exhibitsListDao = db.exhibitsListDao();

    }

    public LiveData<List<Exhibit>> getExhibitsList(){
        if(exhibits == null){
            loadUsers();
        }

        return exhibits;
    }

    private void loadUsers(){
        exhibits = exhibitsListDao.getAllLive();
    }

    public void toggleSelected(Exhibit exhibit){
        exhibit.selected = !exhibit.selected;
        exhibitsListDao.update(exhibit);
    }


}
