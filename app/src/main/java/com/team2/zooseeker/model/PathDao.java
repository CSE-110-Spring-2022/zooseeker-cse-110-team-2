package com.team2.zooseeker.model;

import androidx.room.Dao;
import androidx.room.Insert;

import java.nio.file.Path;

@Dao
public interface PathDao {

    @Insert
    long insert(PathModel p);




}
