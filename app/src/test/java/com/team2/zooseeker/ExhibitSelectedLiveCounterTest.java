package com.team2.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.team2.zooseeker.view.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import cse110.Exhibit;
import cse110.ExhibitsListDao;
import cse110.ExhibitsListDatabase;
import cse110.ZooData;

@RunWith(AndroidJUnit4.class)
public class ExhibitSelectedLiveCounterTest {
    public ExhibitsListDao testDao;
    public ExhibitsListDatabase testDB;

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
            List<Exhibit> exhibits = Exhibit.convert(map);
            testDao = testDB.exhibitsListDao();
            testDao.insertAll(exhibits);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInitialCounter()  {
        ActivityScenario<MainActivity> scenario
                = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {

            int numSelected = testDao.getNumSelected(true);
            TextView textView = activity.findViewById(R.id.counter);

            assertEquals(String.valueOf(numSelected), textView.getText());


        });
    }

    @Test
    public void testIncrementCounter(){
        ActivityScenario<MainActivity> scenario
                = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.recyclerView;
            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
            assertNotNull(firstVH);
            long id = firstVH.getItemId();

            CheckBox checkExhibit = firstVH.itemView.findViewById(R.id.exhibit);

            int numSelected = testDao.getNumSelected(true);
            TextView textView = activity.findViewById(R.id.counter);

            assertEquals(String.valueOf(numSelected), textView.getText());

            checkExhibit.performClick();

            List<Exhibit> exhibits = testDao.getAll();
            int selectedAfterClick = 0;

            for(Exhibit e: exhibits){
                if(e.selected){
                    selectedAfterClick++;
                }
            }

            assertNotEquals(numSelected, selectedAfterClick);
            assertEquals(String.valueOf(selectedAfterClick), textView.getText());

        });
    }

    @Test
    public void testDecrementCounter(){
        ActivityScenario<MainActivity> scenario
                = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.recyclerView;
            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
            assertNotNull(firstVH);
            long id = firstVH.getItemId();

            CheckBox checkExhibit = firstVH.itemView.findViewById(R.id.exhibit);

            int numSelected = testDao.getNumSelected(true);
            TextView textView = activity.findViewById(R.id.counter);

            assertEquals(String.valueOf(numSelected), textView.getText());

            checkExhibit.performClick();

            List<Exhibit> exhibits = testDao.getAll();
            int selectedAfterClick = 0;

            for(Exhibit e: exhibits){
                if(e.selected){
                    selectedAfterClick++;
                }
            }

            assertNotEquals(numSelected, selectedAfterClick);
            assertEquals(String.valueOf(selectedAfterClick), textView.getText());

            checkExhibit.performClick();

            exhibits = testDao.getAll();
            selectedAfterClick = 0;

            for(Exhibit e: exhibits){
                if(e.selected){
                    selectedAfterClick++;
                }
            }

            assertEquals(numSelected, selectedAfterClick);
            assertEquals(String.valueOf(selectedAfterClick), textView.getText());

        });
    }

}