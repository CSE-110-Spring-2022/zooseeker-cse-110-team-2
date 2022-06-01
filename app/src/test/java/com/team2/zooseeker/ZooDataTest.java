package com.team2.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jgrapht.Graph;
import org.junit.Test;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import com.team2.zooseeker.model.IdentifiedWeightedEdge;
import com.team2.zooseeker.model.ZooData;

public class ZooDataTest {

    @Test
    public void testLoadVertexInfoWithLocation() throws FileNotFoundException {
        Map<String, ZooData.VertexInfo> vertexInfo = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/zoo_node_info.json"));
        assertEquals("Entrance and Exit Gate", vertexInfo.get("entrance_exit_gate").name);
        assertEquals(32.73459618734685, vertexInfo.get("entrance_exit_gate").lat, 0.00005);
        assertEquals(-117.14936, vertexInfo.get("entrance_exit_gate").lng, 0.00005);
        assertEquals(27, vertexInfo.size());
        assertEquals(ZooData.VertexInfo.Kind.EXHIBIT_GROUP, vertexInfo.get("parker_aviary").kind);
        assertEquals("parker_aviary", vertexInfo.get("toucan").group_id);

    }

    @Test
    public void testLoadEdgeInfoWithLocation() throws FileNotFoundException {
        Map<String, ZooData.EdgeInfo> edgeInfo = ZooData.loadEdgeInfoJSON(new FileInputStream("src/main/assets/zoo_edge_info.json"));
        System.out.println(edgeInfo.size());
        assertEquals(26, edgeInfo.size());
        assertEquals("Gate Path", edgeInfo.get("gate_to_front").street);
    }

    @Test
    public void testLoadZooGraphWithLocation() throws FileNotFoundException {
        Graph<String, IdentifiedWeightedEdge> zooGraph = ZooData.loadZooGraphJSON(new FileInputStream("src/main/assets/zoo_graph.json"));
        assertTrue(zooGraph.containsVertex("entrance_exit_gate"));
//        System.out.println(zooGraph.edgeSet().size());
//        System.out.println(zooGraph.vertexSet().size());
        assertEquals(26, zooGraph.edgeSet().size());
        assertEquals(22, zooGraph.vertexSet().size());
    }

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
