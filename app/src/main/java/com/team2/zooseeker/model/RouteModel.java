package com.team2.zooseeker.model;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RouteModel {
    ArrayList<String> list;
    InputStream fileIn;
    Graph<String, IdentifiedWeightedEdge> graph;
    Map<String, ZooData.VertexInfo> vertexInfo;
    Map<String, ZooData.EdgeInfo> edgeInfo;

//    /**
//     * Creates a RouteModel for generating routes
//     * @param list list of exhibits in the route, excluding start and endpoints
//     * @param fileIn file containing the graph with all exhibits
//     */
//    public RouteModel(ArrayList<String> list, InputStream fileIn) {
//        this.list = new ArrayList<>(list);
//        this.fileIn = fileIn;
//        graph = ZooData.loadZooGraphJSON(fileIn);
//    }
//
//    /**
//     * Creates a RouteModel for generating routes, starting with an empty exhibit list
//     * @param fileIn file containing the graph with all exhibits
//     */
//    public RouteModel(InputStream fileIn) {
//        this.list = new ArrayList<>();
//        this.fileIn = fileIn;
//        graph = ZooData.loadZooGraphJSON(fileIn);
//    }

    /**
     * Creates a RouteModel with pre-generated graph and vertex/edge maps
     * @param graph graph structure containing all exhibits
     * @param vertexInfo map of all vertices with vertex info
     * @param edgeInfo map of all edges with edge info
     */
    public RouteModel(Graph<String, IdentifiedWeightedEdge> graph, Map<String, ZooData.VertexInfo> vertexInfo, Map<String, ZooData.EdgeInfo> edgeInfo) {
        this.list = new ArrayList<>();
        this.fileIn = null;
        this.graph = graph;
        this.vertexInfo = vertexInfo;
        this.edgeInfo = edgeInfo;
    }

    /**
     * Sets the list of exhibits for generating the plan, excluding start and endpoints
     * @param exhibitsList list of all exhibits to include in plan, cannot include any child exhibits
     */
    public void setExhibits(ArrayList<String> exhibitsList) {
        this.list = exhibitsList;
    }


    /**
     * Combines setExhibits() and genRoutes() into one function call
     * @param exhibitsList the given exhibit list cannot have any child exhibits
     * @return
     */
    public ArrayList<String> genRoute(ArrayList<String> exhibitsList) {
        setExhibits(exhibitsList);
        return genRoute();
    }

    /**
     * Generates the order of exhibits to go through with included start and endpoints
     * @return list of names of all exhibits in plan in a semi-optimal route, including default start and endpoints
     */
    public ArrayList<String> genRoute() {
        // "source" and "sink" are graph terms for the start and end
        String entrance_exit = "entrance_exit_gate";
        return genSubRoute(entrance_exit, entrance_exit);
    }

    /**
     * Version of genRoute used for a given start and end point
     * @param start exhibit to start route from, default should be entrance_exit_gate
     * @param end exhibit to end at, presumably entrance_exit_gate
     * @return list of names of all exhibits in plan in a semi-optimal route
     */
    public ArrayList<String> genSubRoute(String start, String end) {
        // "source" and "sink" are graph terms for the start and end
        String prev = start;
        ArrayList<String> route = new ArrayList<>();
        route.add(start);
        while (!list.isEmpty()) {
            String closest = "";
            int closestDist = Integer.MAX_VALUE;
            for (String next : list) {
                GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(graph, prev, next);
                int pathDist = path.getLength();

                //System.out.printf("From %s to %s: %d\n", prev, next, pathDist);
                if (pathDist < closestDist) {

                    closest = next;
                    closestDist = pathDist;
                }
//                Log.d("DEBUG PATH", path.toString());
            }
            list.remove(closest);
            route.add(closest);
            prev = closest;
        }
        route.add(end);
        return route;
    }

    /**
     * Generate list of directions based on routes generated by genRoute()
     * @param route plan generated by genRoute()
     * @return String list of directions, in order
     */
    public ArrayList<String> getDirections(ArrayList<String> route) {
        ArrayList<String> directions = new ArrayList<>();
        for (int i = 0; i < route.size() - 1; i++) {
            String edgeStartVertex = route.get(i);
            String edgeEndVertex = route.get(i + 1);
            ArrayList<String> path = getDirections(edgeStartVertex, edgeEndVertex);
            directions.addAll(path);
        }
        if (directions.size() > 0) {
            directions.set(0, "Proceed " + directions.get(0).substring(9));
            for (int i = 0; i < directions.size(); i++) {
                directions.set(i, (i + 1) + ". " + directions.get(i));
            }
        }
        return directions;
    }

    /**
     * Generates a list of directions between two vertices
     * @param start name of the starting vertex
     * @param end name of the ending vertex
     * @return String list of directions to get from the start to end vertex
     */
    public ArrayList<String> getDirections(String start, String end) {
        ArrayList<String> directions = new ArrayList<>();
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(graph, start, end);
        String startVertex = path.getStartVertex();
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            String edge = "";
            if (startVertex.equals(Objects.requireNonNull(vertexInfo.get(graph.getEdgeSource(e))).id)) {
                edge = formatEdge(e) + Objects.requireNonNull(vertexInfo.get(graph.getEdgeTarget(e))).name;
                startVertex = Objects.requireNonNull(vertexInfo.get(graph.getEdgeTarget(e))).id;
            } else {
                edge = formatEdge(e) + Objects.requireNonNull(vertexInfo.get(graph.getEdgeSource(e))).name;
                startVertex = Objects.requireNonNull(vertexInfo.get(graph.getEdgeSource(e))).id;
            }
            directions.add(edge);
        }
        return directions;
    }

    class SimpleEdgeData {
        private String streetName;
        private double weight;
        private String destination;

        public SimpleEdgeData(IdentifiedWeightedEdge e, String destination) {
            streetName = edgeInfo.get(e.getId()).street;
            weight = graph.getEdgeWeight(e);
            this.destination = destination;
        }

        public SimpleEdgeData(String streetName, double weight, String destination) {
            this.streetName = streetName;
            this.weight = weight;
            this.destination = destination;
        }

        @Override
        public String toString() {
            return "Continue on " + streetName + " " + (int) weight + " ft towards " + destination;
        }
    }

    /**
     * Generates a list of directions between two vertices
     * @param start name of the starting vertex
     * @param end name of the ending vertex
     * @return String list of directions to get from the start to end vertex
     */
    public ArrayList<String> getBriefDirections(String start, String end) {
        List<SimpleEdgeData> edges = new ArrayList<>();
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(graph, start, end);
        String startVertex = path.getStartVertex();
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            if (startVertex.equals(Objects.requireNonNull(vertexInfo.get(graph.getEdgeSource(e))).id)) {
                edges.add(new SimpleEdgeData(e, vertexInfo.get(graph.getEdgeTarget(e)).name));
                startVertex = Objects.requireNonNull(vertexInfo.get(graph.getEdgeTarget(e))).id;
            } else {
                edges.add(new SimpleEdgeData(e, vertexInfo.get(graph.getEdgeTarget(e)).name));
                startVertex = Objects.requireNonNull(vertexInfo.get(graph.getEdgeSource(e))).id;
            }
        }
//        System.out.println(edges);
        ArrayList<SimpleEdgeData> briefEdges = new ArrayList<>();
        for (int i = 0; i < edges.size(); i++) {
            if (i == 0) {
                briefEdges.add(edges.get(i));
            } else {
                SimpleEdgeData lastEdge = briefEdges.get(briefEdges.size() - 1);
                SimpleEdgeData currentEdge = edges.get(i);
                if (lastEdge.streetName.equals(currentEdge.streetName)) {
                    briefEdges.remove(briefEdges.size() - 1);
                    briefEdges.add(new SimpleEdgeData(lastEdge.streetName, lastEdge.weight + currentEdge.weight, currentEdge.destination));
                } else {
                    briefEdges.add(currentEdge);
                }
            }
        }
//        System.out.println(briefEdges);
        ArrayList<String> directions = new ArrayList<>();
        for (SimpleEdgeData edge : briefEdges) {
            directions.add(edge.toString());
        }
        return directions;

    }



    /**
     * Formats a single edge into a text-based direction
     * @param e edge to be formatted
     * @return String direction corresponding to the edge
     */
    public String formatEdge(IdentifiedWeightedEdge e) {
        return "Continue on " +  Objects.requireNonNull(edgeInfo.get(e.getId())).street + " " + (int) graph.getEdgeWeight(e) + " ft towards ";
    }

    /**
     * Get an exhibits parent's id
     * @param exhibit an exhibit's id
     * @return the exhibit's parent's if it the given exhibit has a parent, return the given id otherwise
     */
    public String getExhibitParent(String exhibit) {
        var parentId = vertexInfo.get(exhibit).group_id;
        System.out.println(parentId);
        if (parentId != null) {
            return parentId;
        } else {
            return exhibit;
        }
    }

    /**
     * Validate a list of exhibits, making sure there are no exhibits which have parents and making sure there are no duplciate parent exhibits
     *
     * @param exhibits
     * @return a validated list of exhibits
     */
    public ArrayList<String> validateExhibitList(ArrayList<String> exhibits) {
        ArrayList<String> validatedExhibits = new ArrayList<>();
        for (String e : exhibits) {
            var parentExhibit = getExhibitParent(e);
            if (!validatedExhibits.contains(parentExhibit)) {
                validatedExhibits.add(parentExhibit);
            }
        }
        return validatedExhibits;
    }

    public List<String> getPath(String source, String sink) {
        return DijkstraShortestPath.findPathBetween(graph, source, sink).getVertexList();
    }

    public double getDistanceBetween(String source, String sink) {
        return DijkstraShortestPath.findPathBetween(graph, source, sink).getWeight();
    }

    public static String getId(ZooData.VertexInfo node) {
        if (node.group_id != null) {
            return node.group_id;
        } else {
            return node.id;
        }
    }

    public static String getId(PathModel node) {
        if (node.parent_id != null) {
            return node.parent_id;
        } else {
            return node.id;
        }
    }
}
