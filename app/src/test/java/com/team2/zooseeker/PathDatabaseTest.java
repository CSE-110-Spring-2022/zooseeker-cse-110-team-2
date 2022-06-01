package com.team2.zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.team2.zooseeker.model.ExhibitsListDatabase;
import com.team2.zooseeker.model.PathDao;
import com.team2.zooseeker.model.PathDatabase;
import com.team2.zooseeker.model.PathModel;
import com.team2.zooseeker.model.ZooData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

@RunWith(AndroidJUnit4.class)

public class PathDatabaseTest {

    private PathDatabase pathDb;

    private PathDao pathDao;
    private Map<String, ZooData.VertexInfo> exhibits;

    @Before
    public void createDB() {
        Context c = ApplicationProvider.getApplicationContext();
//        pathDb = PathDatabase.getSingleton(c);
        pathDb = Room.inMemoryDatabaseBuilder(c, PathDatabase.class).allowMainThreadQueries().build();
        pathDao = pathDb.pathDao();

        try {
            exhibits = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/sample_node_info.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @After
    public void closeDb() throws IOException {
        pathDb.close();
    }

    @Test
    public void testInsert() {
        System.out.println(exhibits);
        var e = exhibits.get("entrance_exit_gate");
        pathDao.insert(new PathModel(e, 0));
        assertEquals(1, pathDao.getAll().size());
    }
}
