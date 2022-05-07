package com.team2.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

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

import java.io.IOException;
import java.util.List;

import cse110.Exhibit;
import cse110.ExhibitsListDao;
import cse110.ExhibitsListDatabase;
import cse110.ZooData;

@RunWith(AndroidJUnit4.class)
public class ExhibitListTest {


    private ExhibitsListDatabase db;
    private ExhibitsListDao dao;

    private final String tag = "DEBUG STRING";



    @Before
    public void resetDatabase() {
        Context c = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(c, ExhibitsListDatabase.class).allowMainThreadQueries().build();
        ExhibitsListDatabase.injectDatabase(db);

        dao = db.exhibitsListDao();
        try {
            dao.insertAll(Exhibit.convert(ZooData.loadVertexInfoJSON(c, "sample_node_info.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDirectionListCheck() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);
        scenario.onActivity( ((MainActivity activity) -> {
            List<Exhibit> exhibits = dao.getAll();
            RecyclerView.ViewHolder firstVH = activity.recyclerView.findViewHolderForAdapterPosition(0);
            assertNotNull(firstVH);
            long id = firstVH.getItemId();
            CheckBox exhibitChecked = firstVH.itemView.findViewById(R.id.exhibit);
            boolean checked = exhibitChecked.isChecked();
            exhibitChecked.performClick();
            assertEquals(!checked, dao.get(id).selected);

        }));
    }
}
