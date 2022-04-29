package com.team2.zooseeker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import cse110.ZooData;

public class ZooDataTest {

    @Test
    public void loadVertexInfoTest() throws FileNotFoundException {
        Map<String, ZooData.VertexInfo> vertexInfo = ZooData.loadVertexInfoJSON(new FileInputStream(new File("src/main/assets/sample_node_info.json")));
        assertEquals("Entrance and Exit Gate", vertexInfo.get("entrance_exit_gate").name);
        assertEquals(7, vertexInfo.size());


    }
}
