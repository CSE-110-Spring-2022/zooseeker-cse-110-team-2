package com.team2.zooseeker.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.team2.zooseeker.model.ExhibitModel;

import java.util.List;

@Dao
public interface ExhibitsListDao {

    @Insert
    long insert(ExhibitModel exhibitModel);

    @Insert
    List<Long> insertAll(List<ExhibitModel> exhibitModels);

    @Query("SELECT * FROM `exhibits_database` WHERE `id`=:id")
    ExhibitModel get(long id);

    @Query("SELECT * FROM `exhibits_database` WHERE `id`=:id")
    LiveData<ExhibitModel> getLive(long id);

    @Query("SELECT * FROM `exhibits_database` WHERE `selected`=:selected")
    int getNumSelected(boolean selected);

    @Query("SELECT * FROM `exhibits_database` WHERE `kind`=:kind ORDER BY `name`")
    LiveData<List<ExhibitModel>> getAllExhibits(String kind);

    @Query("SELECT * FROM `exhibits_database` WHERE `kind`=:kind ORDER BY `name`")
    List<ExhibitModel> getExhibits(String kind);


    @Query("SELECT * FROM `exhibits_database` WHERE `selected`=:selected")
    List<ExhibitModel> getAllSelected(boolean selected);

    @Query("SELECT * FROM `exhibits_database` WHERE `selected`=:selected")
    LiveData<List<ExhibitModel>> getShowSelected(boolean selected);

    @Query("SELECT * FROM `exhibits_database` WHERE `selected`=:selected")
    List<ExhibitModel> getShowSelectedList(boolean selected);

    @Query("SELECT * FROM `exhibits_database` ORDER BY `name`")
    List<ExhibitModel> getAll();

    @Query("SELECT * FROM `exhibits_database` ORDER BY `name`")
    LiveData<List<ExhibitModel>> getAllLive();

    @Query("SELECT * FROM `exhibits_database`WHERE `dataId`=:dataId")
    ExhibitModel getExhibitByStringID(String dataId);

    @Update
    int update(ExhibitModel exhibitModel);

}
