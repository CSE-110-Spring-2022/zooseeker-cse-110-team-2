package com.team2.zooseeker;

import static junit.framework.TestCase.assertEquals;

import android.location.Location;
import android.renderscript.ScriptGroup;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.team2.zooseeker.model.ReplanModel;
import com.team2.zooseeker.model.ZooData;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class ReplanTest {
    @Test
    public void testDistToEdge() {
        assertEquals(1.0, ReplanModel.distToEdge(0, 0, 5, 0, 2, 1));
        assertEquals(1.0, ReplanModel.distToEdge(0, 0, 5, 0, 2, -1));
        assertEquals(0.0, ReplanModel.distToEdge(0, 0, 5, 5, 3, 3));
        assertEquals((float) Math.sqrt(2) / 2, (float) ReplanModel.distToEdge(0, 0, 5, 5, 1, 0));
        assertEquals((float) (2.5 * Math.sqrt(2)), (float) ReplanModel.distToEdge(0, 0, 5, 5, 0, 5));
    }

    @Test
    public void testNearestLandmark() {
        try {
            Map<String, ZooData.VertexInfo> nodes = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/zoo_node_info.json"));
            ReplanModel replan = new ReplanModel(nodes);
            Location mockLoc = new Location("");

            double[] lats = new double[]{32.748983757472594, 32.73561, 32.73551, 32.73551};
            double[] lngs = new double[]{-117.16951754140803, -117.14936, -117.14936, -117.14926};
            double[] dists = new double[]{0, 0, 0.0001, Math.pow(2, 0.5)*0.0001};
            String[] ids = new String[]{"intxn_hippo_monkey_trails", "entrance_exit_gate", "entrance_exit_gate", "entrance_exit_gate"};

            for (int i = 0; i < lats.length; i++) {
                mockLoc.setLatitude(lats[i]);
                mockLoc.setLongitude(lngs[i]);
                assertEquals((float) dists[i], (float) replan.distToNearestLandmark(mockLoc));
                assertEquals(ids[i], replan.getNearestLandmark(mockLoc).id);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Assert.fail("Could not open node file");
        }
    }
}
