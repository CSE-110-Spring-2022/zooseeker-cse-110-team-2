package com.team2.zooseeker;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.team2.zooseeker.model.SearchModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.View;

import com.team2.zooseeker.model.ExhibitModel;
import com.team2.zooseeker.model.ExhibitsListDao;
import com.team2.zooseeker.model.ExhibitsListDatabase;
import com.team2.zooseeker.model.ZooData;

@RunWith(AndroidJUnit4.class)
public class SearchTest {

    ExhibitsListDao testDao;
    ExhibitsListDatabase testDB;

    private static void forceLayout(RecyclerView recyclerView){
        recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        recyclerView.layout(0, 0, 1080, 2280);
    }

    @Before
    public void resetDatabase(){
        Context context = ApplicationProvider.getApplicationContext();
        testDB = Room.inMemoryDatabaseBuilder(context, ExhibitsListDatabase.class)
                .allowMainThreadQueries()
                .build();
        ExhibitsListDatabase.injectDatabase(testDB);

        try {
            Map<String, ZooData.VertexInfo> map = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/sample_node_info.json"));
            List<ExhibitModel> exhibitModels = ExhibitModel.convert(map);
            testDao = testDB.exhibitsListDao();
            testDao.insertAll(exhibitModels);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void searchByNameLowerCaseTest(){
        SearchModel model = new SearchModel(testDao.getAll());
        List<ExhibitModel> output;
        output = model.search("all", 3);
        assertEquals("Alligators", output.get(0).name);
        assertEquals(1, output.size());

        output = model.search("a", 1);
        assertEquals(6, output.size());
    }

    @Test
    public void searchByNameUpperCaseTest(){
        SearchModel model = new SearchModel(testDao.getAll());
        List<ExhibitModel> output;
        output = model.search("ALL", 3);
        assertEquals("Alligators", output.get(0).name);
        assertEquals(1, output.size());

        output = model.search("A", 1);
        assertEquals(6, output.size());
    }

    @Test
    public void searchByTagTest(){
        SearchModel model = new SearchModel(testDao.getAll());
        List<ExhibitModel> output;
        output = model.search("reptile", 7);
        assertEquals("Alligators", output.get(0).name);
        assertEquals(1, output.size());
    }

    @Test
    public void searchBadTagTest(){
        SearchModel model = new SearchModel(testDao.getAll());
        List<ExhibitModel> output;
        output = model.search("abcdef", 6);
        assertEquals(0, output.size());
    }
}
