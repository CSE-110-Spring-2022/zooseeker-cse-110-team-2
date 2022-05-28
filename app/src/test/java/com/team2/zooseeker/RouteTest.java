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
            var g = ZooData.loadZooGraphJSON(new FileInputStream("src/main/assets/sample_zoo_graph.json"));
            var vertexInfo = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/sample_node_info.json"));
            var edgeInfo = ZooData.loadEdgeInfoJSON(new FileInputStream("src/main/assets/sample_edge_info.json"));
            routeModel = new RouteModel(g, vertexInfo, edgeInfo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(routeModel);
        ArrayList<String> route = routeModel.genRoute(mockList);
        assertEquals(5, route.size());
        assertEquals("entrance_exit_gate", route.get(0));
        assertEquals("entrance_exit_gate", route.get(route.size() - 1));
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
            var g = ZooData.loadZooGraphJSON(new FileInputStream("src/main/assets/sample_zoo_graph.json"));
            var vertexInfo = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/sample_node_info.json"));
            var edgeInfo = ZooData.loadEdgeInfoJSON(new FileInputStream("src/main/assets/sample_edge_info.json"));
            routeModel = new RouteModel(g, vertexInfo, edgeInfo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(routeModel);
        ArrayList<String> route = routeModel.genRoute(mockList);
        assertNotEquals("lions", route.get(0));
        assertNotEquals("elephant_odyssey", route.get(0));
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

    @Test
    public void testGenRouteLargeGraph() {
        ArrayList<String> mockList = new ArrayList<>();
        mockList.add("crocodile");
        mockList.add("hippo");

        RouteModel routeModel = null;
        try {
            var g = ZooData.loadZooGraphJSON(new FileInputStream("src/main/assets/zoo_graph.json"));
            var vertexInfo = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/zoo_node_info.json"));
            var edgeInfo = ZooData.loadEdgeInfoJSON(new FileInputStream("src/main/assets/zoo_edge_info.json"));
            routeModel = new RouteModel(g, vertexInfo, edgeInfo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(routeModel);
        ArrayList<String> route = routeModel.genRoute(mockList);
        assertEquals(4, route.size());
        assertEquals("entrance_exit_gate", route.get(0));
        assertEquals("entrance_exit_gate", route.get(route.size() - 1));
    }

    @Test
    public void testGenRouteWithExhibitGroup() {
        ArrayList<String> mockList = new ArrayList<>();
        mockList.add("crocodile");
        mockList.add("toucan");

        RouteModel routeModel = null;
        try {
            var g = ZooData.loadZooGraphJSON(new FileInputStream("src/main/assets/zoo_graph.json"));
            var vertexInfo = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/zoo_node_info.json"));
            var edgeInfo = ZooData.loadEdgeInfoJSON(new FileInputStream("src/main/assets/zoo_edge_info.json"));
            routeModel = new RouteModel(g, vertexInfo, edgeInfo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(routeModel);
        ArrayList<String> validatedMockList = routeModel.validateExhibitList(mockList);
        ArrayList<String> route = routeModel.genRoute(validatedMockList);
        assertEquals(route.size(), 4);
        assertEquals(route.get(0), "entrance_exit_gate");
        assertEquals(route.get(route.size() - 1), "entrance_exit_gate");
    }

    @Test
    public void testGetExhibitParent() {
        RouteModel routeModel = null;
        try {
            var g = ZooData.loadZooGraphJSON(new FileInputStream("src/main/assets/zoo_graph.json"));
            var vertexInfo = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/zoo_node_info.json"));
            var edgeInfo = ZooData.loadEdgeInfoJSON(new FileInputStream("src/main/assets/zoo_edge_info.json"));
            routeModel = new RouteModel(g, vertexInfo, edgeInfo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(routeModel);
        assertEquals("parker_aviary", routeModel.getExhibitParent("toucan"));
        assertEquals("owens_aviary", routeModel.getExhibitParent("mynah"));
        assertEquals("scripps_aviary", routeModel.getExhibitParent("spoonbill"));
        assertEquals("crocodile", routeModel.getExhibitParent("crocodile"));
    }

    @Test
    public void testGenRouteMultipleExhibitGroups() {
        ArrayList<String> mockList = new ArrayList<>();
        mockList.add("crocodile");
        mockList.add("toucan");
        mockList.add("motmot");
        mockList.add("mynah");
        RouteModel routeModel = null;
        try {
            var g = ZooData.loadZooGraphJSON(new FileInputStream("src/main/assets/zoo_graph.json"));
            var vertexInfo = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/zoo_node_info.json"));
            var edgeInfo = ZooData.loadEdgeInfoJSON(new FileInputStream("src/main/assets/zoo_edge_info.json"));
            routeModel = new RouteModel(g, vertexInfo, edgeInfo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(routeModel);
        ArrayList<String> validatedMockList = routeModel.validateExhibitList(mockList);
        ArrayList<String> route = routeModel.genRoute(validatedMockList);
        System.out.println(validatedMockList);
        assertEquals(route.size(), 5);
        assertEquals(route.get(0), "entrance_exit_gate");
        assertEquals(route.get(route.size() - 1), "entrance_exit_gate");
    }

    @Test
    public void testGetDistanceBetween() {
        RouteModel routeModel = null;
        try {
            var g = ZooData.loadZooGraphJSON(new FileInputStream("src/main/assets/zoo_graph.json"));
            var vertexInfo = ZooData.loadVertexInfoJSON(new FileInputStream("src/main/assets/zoo_node_info.json"));
            var edgeInfo = ZooData.loadEdgeInfoJSON(new FileInputStream("src/main/assets/zoo_edge_info.json"));
            routeModel = new RouteModel(g, vertexInfo, edgeInfo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(routeModel);
        var distance = routeModel.getDistanceBetween("entrance_exit_gate", "intxn_front_treetops");
        assertEquals(1100, distance, 0.01);
        var distance2 = routeModel.getDistanceBetween("entrance_exit_gate", "intxn_front_monkey");
        assertEquals(3800, distance2, 0.01);

    }
}
