package com.team2.zooseeker.model;

import android.content.Context;
import android.location.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReplanModel {
    Map<String, ZooData.VertexInfo> nodeMap;

    public ReplanModel(Context context, String nodeFile) {
        try {
            nodeMap = ZooData.loadVertexInfoJSON(context, nodeFile);
            //Nodes needed for checking distances to nearest node
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ReplanModel(Map<String, ZooData.VertexInfo> nodeMap) {
        this.nodeMap = nodeMap;
    }

    /**
     * Returns true if the current location is not on the current route, in which case
     * the user should be prompted to re-plan the route
     * @param currentLoc current location of user
     * @param path all nodes in current path to next exhibit
     * @return true if location is off-route, false if on-route
     */
    public boolean offTrack(Location currentLoc, List<String> path) {
        // Currently uses two nearest nodes in given path as the 'current edge'
        List<String> tempPath = new ArrayList<>(path);
        ZooData.VertexInfo node1 = getNearestLandmark(currentLoc, tempPath);
        tempPath.remove(node1.id);
        ZooData.VertexInfo node2 = getNearestLandmark(currentLoc, tempPath);

        // Then compares distance to nearest landmark with distance to 'current edge'
        return distToNearestLandmark(currentLoc) < distToEdge(currentLoc, node1, node2);
    }

    /**
     * Returns the distance from the given location to the nearest landmark
     * @param currentLoc current location
     * @return distance to the nearest landmark
     */
    public double distToNearestLandmark(Location currentLoc) {
        ZooData.VertexInfo nearest = getNearestLandmark(currentLoc);
        return getDist(currentLoc.getLatitude(), currentLoc.getLongitude(), nearest.lat, nearest.lng);
    }

    /**
     * Returns the nearest landmark from the current position
     * @param currentLoc current location
     * @return VertexInfo of nearest landmark
     */
    public ZooData.VertexInfo getNearestLandmark(Location currentLoc) {
        double smallestDist = Double.MAX_VALUE;
        ZooData.VertexInfo currentNode = new ZooData.VertexInfo();

        //Iterate through all nodes, set distance to be min distance to any node
        for (ZooData.VertexInfo node : nodeMap.values()) {
            double compDist = getDist(currentLoc.getLatitude(),
                    currentLoc.getLongitude(),
                    node.lat, node.lng);
            if (compDist < smallestDist) {
                smallestDist = compDist;
                currentNode = node;
            }
        }
        return currentNode;
    }

    /**
     * Returns the nearest landmark from the current position
     * @param currentLoc current location
     * @param nodes list of landmarks to include in the search
     * @return VertexInfo of nearest landmark
     */
    public ZooData.VertexInfo getNearestLandmark(Location currentLoc, List<String> nodes) {
        double smallestDist = Double.MAX_VALUE;
        String currentNode = "";

        //Iterate through all nodes, set distance to be min distance to any node
        for (String node : nodes) {
            double compDist = getDist(currentLoc.getLatitude(),
                    currentLoc.getLongitude(),
                    nodeMap.get(node).lat, nodeMap.get(node).lng);
            if (compDist < smallestDist) {
                smallestDist = compDist;
                currentNode = node;
            }
        }
        return nodeMap.get(currentNode);
    }

    /**
     * Returns the distance from the given location to the given edge
     * @param currentLoc current location
     * @param node1 node at either side of current edge in route
     * @param node2 node at other side of current edge in route from node1
     * @return shortest distance from the given location to the given edge
     */
    public double distToEdge(Location currentLoc, ZooData.VertexInfo node1, ZooData.VertexInfo node2) {
        return distToEdge(
                node1.lat, node1.lng,
                node2.lat, node2.lng,
                currentLoc.getLatitude(), currentLoc.getLongitude());
    }

    //a and b are nodes, loc is current location
    public static double distToEdge(double ax, double ay, double bx, double by, double locx, double locy) {
        double a1 = locx - ax; // vector from point to edge of line
        double a2 = locy - ay;
        double l1 = bx - ax; // vector of line
        double l2 = by - ay;
        double o1 = -l2; // orthogonal
        double o2 = l1;

        double dot = a1 * o1 + a2 * o2; // dot product

        return Math.abs(dot) / Math.sqrt(o1 * o1 + o2 * o2); //should return correct value
    }

    //Helper static method used for nearestLandmark
    private static double getDist(double ax, double ay, double bx, double by) {
        return Math.pow(Math.pow(ax - bx, 2) + Math.pow(ay - by, 2), 0.5);
    }
}
