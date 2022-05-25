package com.team2.zooseeker.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface PathDao {

    @Insert
    long insert(PathModel p);

    @Query("SELECT * FROM `paths_database` ORDER BY `position`")
    List<PathModel> getAll();

    @Query("DELETE FROM `paths_database`")
    void deleteAll();

}
