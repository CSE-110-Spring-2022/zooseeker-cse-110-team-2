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
    public PathDatabase pathDatabase;

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
        pathDatabase = Room.inMemoryDatabaseBuilder(context, PathDatabase.class)
                .allowMainThreadQueries()
                .build();

        try {
            Map<String, ZooData.VertexInfo> map = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/sample_node_info.json"));
            List<ExhibitModel> exhibitModels = ExhibitModel.convert(map);
            testDao = testDB.exhibitsListDao();
            testDao.insertAll(exhibitModels);
            pathDao = pathDatabase.pathDao();

            Set keys = map.keySet();
            Iterator key = keys.iterator();

            while(key.hasNext()){
                String current = key.next().toString();
                PathModel p = new PathModel(map.get(current), 0);
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
    public void testNumberOfVisitedAtStart()  {
        ActivityScenario<MainActivity> scenario
                = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            try {
                Map<String, ZooData.VertexInfo> map = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/sample_node_info.json"));
                Button plan = activity.findViewById(R.id.plan_btn);

                plan.performClick();

                int count = 0;

                for(PathModel p : pathDao.getAll()){
                    if(p.visited){
                        count += 1;
                    }
                }

                assertEquals(count, pathDao.getAllVisited(true).size());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        });
    }

    /**
     * This test will see how exhibits have been marked as visited after hitting next.
     */
    @Test
    public void testNumberOfVisitedAfterNext()  {
        ActivityScenario<DirectionListActivity> scenario
                = ActivityScenario.launch(DirectionListActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            Button next = activity.findViewById(R.id.next_button);
            next.performClick();
            int count = 0;

            for(PathModel p : pathDao.getAll()){
                if(p.visited) {
                    count += 1;
                }
            }

            assertEquals(count, pathDao.getAllVisited(true).size());
        });
    }

    /**
     * This test will see how exhibits have been marked as visited after hitting skip.
     */
    @Test
    public void testNumberOfVisitedAfterSkip()  {
        ActivityScenario<DirectionListActivity> scenario
                = ActivityScenario.launch(DirectionListActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            Button skip = activity.findViewById(R.id.next_button);
            skip.performClick();
            int count = 0;

            for(PathModel p : pathDao.getAll()){
                if(p.visited) {
                    count += 1;
                }
            }

            assertEquals(count, pathDao.getAllVisited(true).size());
        });
    }

    /**
     * This test will see how exhibits have been marked as visited after hitting previous.
     */
    @Test
    public void testNumberOfVisitedAfterPrev()  {
        ActivityScenario<DirectionListActivity> scenario
                = ActivityScenario.launch(DirectionListActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            Button next = activity.findViewById(R.id.next_button);
            next.performClick();
            int count = 0;


            for(PathModel p : pathDao.getAll()){
                if(p.visited) {
                    count += 1;
                }
            }

            assertEquals(count, pathDao.getAllVisited(true).size());

            Button prev = activity.findViewById(R.id.backButton);
            prev.performClick();

            assertEquals(count, pathDao.getAllVisited(true).size());
        });
    }
}
