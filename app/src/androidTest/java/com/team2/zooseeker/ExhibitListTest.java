package com.team2.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
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

import java.io.IOException;
import java.util.List;

import com.team2.zooseeker.model.ExhibitModel;
import com.team2.zooseeker.model.ExhibitsListDao;
import com.team2.zooseeker.model.ExhibitsListDatabase;
import com.team2.zooseeker.model.ZooData;

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
            dao.insertAll(ExhibitModel.convert(ZooData.loadVertexInfoJSON(c, "sample_node_info.json")));
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
        scenario.onActivity(((MainActivity activity) -> {
            List<ExhibitModel> exhibitModels = dao.getAll();
            RecyclerView.ViewHolder firstVH = activity.recyclerView.findViewHolderForAdapterPosition(0);
            assertNotNull(firstVH);
            long id = firstVH.getItemId();
            CheckBox exhibitChecked = firstVH.itemView.findViewById(R.id.exhibitModel);
            boolean checked = exhibitChecked.isChecked();
            exhibitChecked.performClick();
            assertEquals(!checked, dao.get(id).selected);
        }));
    }
}
