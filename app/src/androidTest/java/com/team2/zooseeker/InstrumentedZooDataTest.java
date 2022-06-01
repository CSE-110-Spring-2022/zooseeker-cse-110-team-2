package com.team2.zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import com.team2.zooseeker.model.ZooData;

@RunWith(AndroidJUnit4.class)
public class InstrumentedZooDataTest {

    @Test
    public void testLoadVertexInfo() throws FileNotFoundException {
        Context c = ApplicationProvider.getApplicationContext();
        try {
            Map<String, ZooData.VertexInfo> vertexInfo = ZooData.loadVertexInfoJSON(c, "sample_node_info.json");
            assertEquals("Entrance and Exit Gate", vertexInfo.get("entrance_exit_gate").name);
            assertEquals(7, vertexInfo.size());
        } catch (IOException e) {
            return;
        }
    }
}
