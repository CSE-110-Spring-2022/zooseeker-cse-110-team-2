package com.team2.zooseeker;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.team2.zooseeker.model.ExhibitModel;
import com.team2.zooseeker.model.ExhibitsListDao;
import com.team2.zooseeker.model.ExhibitsListDatabase;
import com.team2.zooseeker.model.ZooData;

@RunWith(AndroidJUnit4.class)
public class ExhibitListDatabaseTest {

    private ExhibitsListDatabase db;
    private ExhibitsListDao dao;

    @Before
    public void createDb() {
        Context c = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(c, ExhibitsListDatabase.class).allowMainThreadQueries().build();
        dao = db.exhibitsListDao();
        Map<String, ZooData.VertexInfo> exhibits = null;
        try {
            exhibits = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/sample_node_info.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<ExhibitModel> exhibitsList = ExhibitModel.convert(exhibits);
        dao.insertAll(exhibitsList);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testDbSize() {
        assertEquals(7, dao.getAll().size());
    }

    @Test
    public void testUpdate() {
        ExhibitModel exhibitModel = dao.getAll().get(0);
        boolean exhibitSelected = exhibitModel.selected;
        exhibitModel.selected = !exhibitModel.selected;
        dao.update(exhibitModel);
        assertEquals(!exhibitSelected, dao.getAll().get(0).selected);
    }
}
