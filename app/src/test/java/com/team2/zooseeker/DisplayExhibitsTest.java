package com.team2.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.team2.zooseeker.model.RouteModel;
import com.team2.zooseeker.view.MainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cse110.Exhibit;
import cse110.ExhibitsListDao;
import cse110.ExhibitsListDatabase;
import cse110.ZooData;

@RunWith(AndroidJUnit4.class)
public class DisplayExhibitsTest {

    public ExhibitsListDao exhibitsListDao;
    public ExhibitsListDatabase exhibitsListDatabase;

    private static void forceLayout(RecyclerView recyclerView){
        recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        recyclerView.layout(0, 0, 1080, 2280);
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

    }

}
