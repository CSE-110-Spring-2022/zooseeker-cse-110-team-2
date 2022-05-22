package com.team2.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

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

import com.team2.zooseeker.model.ExhibitModel;
import com.team2.zooseeker.model.ExhibitsListDao;
import com.team2.zooseeker.model.ExhibitsListDatabase;
import com.team2.zooseeker.model.ZooData;

@RunWith(AndroidJUnit4.class)
public class DisplayExhibitsTest {

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
            List<ExhibitModel> exhibitModels = ExhibitModel.convert(map);
            testDao = testDB.exhibitsListDao();
            testDao.insertAll(exhibitModels);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDisplaySize()  {
        ActivityScenario<MainActivity> scenario
                = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            try {
                Map<String, ZooData.VertexInfo> map = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/sample_node_info.json"));
                int count = 0;
                for(Map.Entry<String, ZooData.VertexInfo> m : map.entrySet()){
                    if(m.getValue().kind.equals(ZooData.VertexInfo.Kind.EXHIBIT)){
                        count ++;
                    }
                }

                assertEquals(count, activity.recyclerView.getAdapter().getItemCount());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        });
    }

    @Test
    public void testAddToList(){
        ActivityScenario<MainActivity> scenario
                = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            try {
                RecyclerView recyclerView = activity.recyclerView;
                RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
                assertNotNull(firstVH);
                long id = firstVH.getItemId();

                CheckBox checkExhibit = firstVH.itemView.findViewById(R.id.exhibitModel);

                Map<String, ZooData.VertexInfo> map = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/sample_node_info.json"));
                List<ExhibitModel> exhibitsTest = ExhibitModel.convert(map);
                int selected = 0;

                for(ExhibitModel e : exhibitsTest){
                    if(e.selected){
                        selected++;
                    }
                }

                //Checkbox check should be same as database selected
                ExhibitModel exhibitModel = testDao.get(id);
                assertEquals(checkExhibit.isChecked(), exhibitModel.selected);

                //click checkbox
                checkExhibit.performClick();

                //When clicking performs, database should have changed
                ExhibitModel afterClick = testDao.get(id);
                assertEquals(!exhibitModel.selected, afterClick.selected);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}
