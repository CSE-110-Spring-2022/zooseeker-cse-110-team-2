package cse110;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExhibitsListDao {

    @Insert
    long insert(Exhibit exhibit);

    @Insert
    List<Long> insertAll(List<Exhibit> exhibits);

    @Query("SELECT * FROM `exhibits_database` WHERE `id`=:id")
    Exhibit get(long id);

    @Query("SELECT * FROM `exhibits_database` WHERE `id`=:id")
    LiveData<Exhibit> getLive(long id);

    @Query("SELECT * FROM `exhibits_database` WHERE `selected`=:selected")
    int getNumSelected(boolean selected);

    @Query("SELECT * FROM `exhibits_database` WHERE `kind`=:kind ORDER BY `name`")
    LiveData<List<Exhibit>> getAllExhibits(String kind);

    @Query("SELECT * FROM `exhibits_database` WHERE `kind`=:kind ORDER BY `name`")
    List<Exhibit> getExhibits(String kind);


    @Query("SELECT * FROM `exhibits_database` ORDER BY `name`")
    List<Exhibit> getAll();

    @Query("SELECT * FROM `exhibits_database` ORDER BY `name`")
    LiveData<List<Exhibit>> getAllLive();

    @Update
    int update(Exhibit exhibit);

}
