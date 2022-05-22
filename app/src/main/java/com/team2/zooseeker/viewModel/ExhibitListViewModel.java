package com.team2.zooseeker.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import com.team2.zooseeker.model.ExhibitModel;
import com.team2.zooseeker.model.ExhibitsListDao;
import com.team2.zooseeker.model.ExhibitsListDatabase;

public class ExhibitListViewModel extends AndroidViewModel {

    private LiveData<List<ExhibitModel>> exhibits;
    private final ExhibitsListDao exhibitsListDao;

    public ExhibitListViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        ExhibitsListDatabase db = ExhibitsListDatabase.getSingleton(context);
        exhibitsListDao = db.exhibitsListDao();
    }

    public LiveData<List<ExhibitModel>> getExhibitsList(){
        if(exhibits == null){
            loadUsers();
        }
        return exhibits;
    }

    private void loadUsers(){
        exhibits = exhibitsListDao.getAllExhibits("exhibit");
    }

    public void toggleSelected(ExhibitModel exhibitModel){
        exhibitModel.selected = !exhibitModel.selected;
        exhibitsListDao.update(exhibitModel);
    }
}
