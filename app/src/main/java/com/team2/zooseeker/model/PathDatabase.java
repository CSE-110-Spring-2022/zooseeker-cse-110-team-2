package com.team2.zooseeker.model;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.nio.file.Path;

@Database(entities = {PathModel.class}, version = 1)
public abstract class PathDatabase extends RoomDatabase {

    private static PathDatabase singleton = null;

    public abstract PathDao pathDao();

    public synchronized static PathDatabase getSingleton(Context context){
        if(singleton == null){
            singleton = PathDatabase.makeDatabase(context);
        }
        return singleton;
    }

    private static PathDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, PathDatabase.class, "path_database.db").allowMainThreadQueries().build();
    }

    @VisibleForTesting
    public static void injectDatabase(PathDatabase database){
        if(singleton != null){
            singleton.close();
        }
        singleton = database;
    }

}
