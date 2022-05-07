package com.team2.zooseeker.model;

import android.content.Context;
import android.util.Log;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import cse110.IdentifiedWeightedEdge;
import cse110.ZooData;

public class RouteModel {
    ArrayList<String> list;
    InputStream fileIn;

    public RouteModel(ArrayList<String> list, InputStream fileIn) {
        this.list = new ArrayList<>(list);
        this.fileIn = fileIn;
    }

    public RouteModel(InputStream fileIn) {
        this.list = new ArrayList<>();
        this.fileIn = fileIn;
    }

    public void setExhibits(ArrayList<String> exhibitsList) {
        this.list = exhibitsList;
    }

    public ArrayList<String> genRoute() {
        // "source" and "sink" are graph terms for the start and end
        String entrance_exit = "entrance_exit_gate";

        // 1. Load the graph...
        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(fileIn);

        String prev = entrance_exit;
        ArrayList<String> route = new ArrayList<>();
//        route.add(0, entrance_exit);
//        list.add(0, entrance_exit);
        route.add(entrance_exit);
//        Log.d("DEBUG ROUTE", route.toString());
        while (!list.isEmpty()) {
            String closest = "";
            int closestDist = Integer.MAX_VALUE;
            for (String next : list) {
                int pathDist = DijkstraShortestPath.findPathBetween(g, prev, next).getLength();
                //System.out.printf("From %s to %s: %d\n", prev, next, pathDist);
                if (pathDist < closestDist) {
                    closest = next;
                    closestDist = pathDist;
                }
            }
            list.remove(closest);
            route.add(closest);
            prev = closest;
        }
        route.add(entrance_exit);

        return route;

    }
}
