package com.team2.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.team2.zooseeker.model.ExhibitModel;
import com.team2.zooseeker.model.ExhibitsListDao;
import com.team2.zooseeker.model.ExhibitsListDatabase;
import com.team2.zooseeker.model.PathDao;
import com.team2.zooseeker.model.PathDatabase;
import com.team2.zooseeker.model.PathModel;
import com.team2.zooseeker.model.ZooData;
import com.team2.zooseeker.view.DirectionListActivity;
import com.team2.zooseeker.view.MainActivity;
import com.team2.zooseeker.viewModel.DirectionListViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(AndroidJUnit4.class)
public class RetainRouteTest {

    public ExhibitsListDao testDao;
    public ExhibitsListDatabase testDB;
    public PathDao pathDao;
    public PathDatabase pathDb;

    private static void forceLayout(RecyclerView recyclerView) {
        recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        recyclerView.layout(0, 0, 1080, 2280);
    }


    @Before
    public void resetDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        pathDb = PathDatabase.getSingleton(context);
        pathDao = pathDb.pathDao();

        try {
            Map<String, ZooData.VertexInfo> map = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/sample_node_info.json"));
            Set keys = map.keySet();
            Iterator key = keys.iterator();

            while (key.hasNext()) {
                String current = key.next().toString();
                PathModel p = new PathModel(map.get(current), 0);
                p.setVisited(false);
                pathDao.insert(p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This test will check how many exhibits are visited at the start.
     */
    @Test
    public void testNumberOfVisitedAtStart() {
        int count = 0;

        for (PathModel p : pathDao.getAll()) {
            if (p.visited) {
                count += 1;
            }
        }

        assertEquals(count, pathDao.getAllVisited(true).size());
    }

    /**
     * This test will see how exhibits have been marked as visited after hitting next.
     */
    @Test
    public void testNumberOfVisitedAfterNext() {
        PathModel p = pathDao.getAll().get(0);
        p.setVisited(true);
        pathDao.update(p);

        assertEquals(1, pathDao.getAllVisited(true).size());
    }

    /**
     * This test will see how exhibits have been marked as visited after hitting next.
     */
    @Test
    public void testNumberOfVisitedAfterBack() {

        pathDao.getAll().get(0).setVisited(false);
        pathDao.update(pathDao.getAll().get(0));

        assertEquals(0, pathDao.getAllVisited(true).size());
    }

}