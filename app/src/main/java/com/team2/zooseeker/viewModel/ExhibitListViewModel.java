package com.team2.zooseeker.viewModel;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

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

    int counter;

    public ExhibitListViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        ExhibitsListDatabase db = ExhibitsListDatabase.getSingleton(context);
        exhibitsListDao = db.exhibitsListDao();
        counter = exhibitsListDao.getNumSelected(true);
    }

    public LiveData<List<ExhibitModel>> getExhibitsList(boolean selected){
        if(exhibits == null){
            loadUsers(selected);
        }
        return exhibits;
    }

    private void loadUsers(boolean selected){
        if(selected){
            exhibits = exhibitsListDao.getShowSelected(true);
        }
        else{
            exhibits = exhibitsListDao.getAllExhibits("exhibit");
        }
    }

    public ExhibitsListDao getExhibitsListDao(){
        return this.exhibitsListDao;
    }


    public void toggleSelected(ExhibitModel exhibitModel){
        exhibitModel.selected = !exhibitModel.selected;
        exhibitsListDao.update(exhibitModel);
        if (exhibitModel.selected) {
            counter++;
        } else {
            counter--;
        }

    }

    public int getCounter() {
        return counter;
    }
}
