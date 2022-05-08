package com.team2.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jgrapht.Graph;
import org.junit.Test;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import cse110.IdentifiedWeightedEdge;
import cse110.ZooData;

public class ZooDataTest {

    @Test
    public void testLoadVertexInfo() throws FileNotFoundException {
        Map<String, ZooData.VertexInfo> vertexInfo = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/sample_node_info.json"));
        assertEquals("Entrance and Exit Gate", vertexInfo.get("entrance_exit_gate").name);
        assertEquals(7, vertexInfo.size());
    }

    @Test
    public void testLoadEdgeInfo() throws FileNotFoundException {
        Map<String, ZooData.EdgeInfo> edgeInfo = ZooData.loadEdgeInfoJSON(new FileInputStream("src/main/assets/sample_edge_info.json"));
        assertEquals(7, edgeInfo.size());
        assertEquals("Entrance Way", edgeInfo.get("edge-0").street);
    }

    @Test
    public void testLoadZooGraph() throws FileNotFoundException {
        Graph<String, IdentifiedWeightedEdge> zooGraph = ZooData.loadZooGraphJSON(new FileInputStream("src/main/assets/sample_zoo_graph.json"));
        assertTrue(zooGraph.containsVertex("entrance_exit_gate"));
        assertEquals(7, zooGraph.edgeSet().size());
    }
}
