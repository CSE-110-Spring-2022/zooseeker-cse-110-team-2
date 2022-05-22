package com.team2.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.team2.zooseeker.model.RouteModel;

import org.jgrapht.Graph;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import com.team2.zooseeker.model.IdentifiedWeightedEdge;
import com.team2.zooseeker.model.ZooData;

@RunWith(AndroidJUnit4.class)
public class RouteTest {

    @Test
    public void testGenRoute() {
        ArrayList<String> mockList = new ArrayList<>();
        mockList.add("lions");
        mockList.add("gorillas");
        mockList.add("arctic_foxes");

        RouteModel routeModel = null;
        try {
            routeModel = new RouteModel(mockList, new FileInputStream("src/main/assets/sample_zoo_graph.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(routeModel);
        ArrayList<String> route = routeModel.genRoute();
        assertEquals(route.size(), 5);
        assertEquals(route.get(0), "entrance_exit_gate");
        assertEquals(route.get(route.size() - 1), "entrance_exit_gate");
    }

    @Test
    public void testOptimizedRoute() {
        ArrayList<String> mockList = new ArrayList<>();
        mockList.add("lions");
        mockList.add("gorillas");
        mockList.add("arctic_foxes");
        mockList.add("gators");
        mockList.add("elephant_odyssey");

        RouteModel routeModel = null;
        try {
            routeModel = new RouteModel(mockList, new FileInputStream("src/main/assets/sample_zoo_graph.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(routeModel);
        ArrayList<String> route = routeModel.genRoute();
        assertNotEquals(route.get(0), "lions");
        assertNotEquals(route.get(0), "elephant_odyssey");
    }

    @Test
    public void testRouteDirections() {
        ArrayList<String> mockList = new ArrayList<>();
        mockList.add("gators");
        mockList.add("lions");
        mockList.add("gorillas");
        Graph<String, IdentifiedWeightedEdge> g = null;
        Map<String, ZooData.VertexInfo> vertexInfo = null;
        Map<String, ZooData.EdgeInfo> edgeInfo = null;

        try {
            g = ZooData.loadZooGraphJSON(new FileInputStream("src/main/assets/sample_zoo_graph.json"));
            vertexInfo = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/sample_node_info.json"));
            edgeInfo = ZooData.loadEdgeInfoJSON(new FileInputStream("src/main/assets/sample_edge_info.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        RouteModel routeModel = new RouteModel(g, vertexInfo, edgeInfo);
        assertNotNull(routeModel);
        routeModel.setExhibits(mockList);
        ArrayList<String> route = routeModel.genRoute();
        ArrayList<String> directions = routeModel.getDirections(route);
        System.out.println(directions);
        assertEquals(6, directions.size());
    }

    @Test
    public void testGetDirections() {
        Graph<String, IdentifiedWeightedEdge> g = null;
        Map<String, ZooData.VertexInfo> vertexInfo = null;
        Map<String, ZooData.EdgeInfo> edgeInfo = null;

        try {
            g = ZooData.loadZooGraphJSON(new FileInputStream("src/main/assets/sample_zoo_graph.json"));
            vertexInfo = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/sample_node_info.json"));
            edgeInfo = ZooData.loadEdgeInfoJSON(new FileInputStream("src/main/assets/sample_edge_info.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        RouteModel routeModel = new RouteModel(g, vertexInfo, edgeInfo);
        assertNotNull(routeModel);
        ArrayList<String> directions = routeModel.getDirections("entrance_exit_gate", "gorillas");
        assertEquals(2, directions.size());
    }
}
