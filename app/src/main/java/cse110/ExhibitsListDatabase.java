package cse110;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

@Database(entities = {Exhibit.class}, version = 1)
public abstract class ExhibitsListDatabase extends RoomDatabase {
    private static ExhibitsListDatabase singleton = null;

    public abstract ExhibitsListDao exhibitsListDao();

    public synchronized static ExhibitsListDatabase getSingleton(Context context){
        if(singleton == null){
            singleton = ExhibitsListDatabase.makeDatabase(context);
        }

        return singleton;
    }

    private static ExhibitsListDatabase makeDatabase(Context context){
        return Room.databaseBuilder(context, ExhibitsListDatabase.class, "exhibits_database.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            Map<String, ZooData.VertexInfo> exhibits = null;
                            try {
                                exhibits = ZooData
                                        .loadVertexInfoJSON(context, "src/main/assets/sample_node_info.json");
                                List<Exhibit> exhibitsList = Exhibit.convert(exhibits);
                                getSingleton(context).exhibitsListDao().insertAll(exhibitsList);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                })
                .build();
    }

    @VisibleForTesting
    public static void injectDatabase(ExhibitsListDatabase database){
        if(singleton != null){
            singleton.close();
        }

        singleton = database;
    }

}
