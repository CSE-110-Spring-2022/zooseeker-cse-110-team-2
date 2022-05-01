package com.team2.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.team2.zooseeker.model.RouteModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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
}
