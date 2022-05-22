package com.team2.zooseeker.model;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.nio.json.JSONImporter;

public class ZooData {
    public static class VertexInfo {
        public static enum Kind {
            // The SerializedName annotation tells GSON how to convert
            // from the strings in our JSON to this Enum.
            @SerializedName("gate") GATE,
            @SerializedName("exhibit") EXHIBIT,
            @SerializedName("intersection") INTERSECTION
        }

        public String id;
        public Kind kind;
        public String name;
        public List<String> tags;
    }

    public static class EdgeInfo {
        public String id;
        public String street;
    }

    /**
     * Load Vertex Info from a json in the assets folder, use this method in the app itself
     * @param context current android Activity
     * @param path filename of json file, assuming it is in the assets folder
     * @return a Map with the names of exhibits as keys and the VertexInfo as values
     * @throws IOException
     */
    public static Map<String, ZooData.VertexInfo> loadVertexInfoJSON(Context context, String path) throws IOException {
        return loadVertexInfoJSON(context.getAssets().open(path));
    }

    /**
     * Load vertex info from an arbitrary input stream, use this method for non-instrumented testing
     * @param inputStream input stream representing the JSON, FileInputStream works well for this
     * @return a Map with the names of exhibits as keys and the VertexInfo as values
     */
    public static Map<String, ZooData.VertexInfo> loadVertexInfoJSON(InputStream inputStream) {
        Reader reader = new InputStreamReader(inputStream);

        Gson gson = new Gson();
        Type type = new TypeToken<List<ZooData.VertexInfo>>(){}.getType();
        List<ZooData.VertexInfo> zooData = gson.fromJson(reader, type);

        // This code is equivalent to:
        //
        // Map<String, ZooData.VertexInfo> indexedZooData = new HashMap();
        // for (ZooData.VertexInfo datum : zooData) {
        //   indexedZooData[datum.id] = datum;
        // }
        //
        Map<String, ZooData.VertexInfo> indexedZooData = zooData
                .stream()
                .collect(Collectors.toMap(v -> v.id, datum -> datum));

        return indexedZooData;
    }

    /**
     * Load Vertex Info from a json in the assets folder, use this method in the app itself
     * @param context current android Activity
     * @param path filename of json file, assuming it is in the assets folder
     * @return a Map with the names of streets/paths as keys and the EdgeInfo as values
     */
    public static Map<String, ZooData.EdgeInfo> loadEdgeInfoJSON(Context context, String path) {
        try {
            return loadEdgeInfoJSON(context.getAssets().open(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load edge info from an arbitrary input stream, use this method for non-instrumented testing
     * @param inputStream input stream representing the JSON, FileInputStream works well for this
     * @return a Map with the names of streets/paths as keys and the EdgeInfo as values
     */
    public static Map<String, ZooData.EdgeInfo> loadEdgeInfoJSON(InputStream inputStream) {
        Reader reader = new InputStreamReader(inputStream);

        Gson gson = new Gson();
        Type type = new TypeToken<List<ZooData.EdgeInfo>>(){}.getType();
        List<ZooData.EdgeInfo> zooData = gson.fromJson(reader, type);

        Map<String, ZooData.EdgeInfo> indexedZooData = zooData
                .stream()
                .collect(Collectors.toMap(v -> v.id, datum -> datum));

        return indexedZooData;
    }

    /**
     * Load ZooData as a Graph from a json in the assets folder, use this method in the app itself
     * @param context current android Activity
     * @param path filename of json file, assuming it is in the assets folder
     * @return ZooData in Graph format
     * @throws IOException
     */
    public static Graph<String, IdentifiedWeightedEdge> loadZooGraphJSON(Context context, String path) throws IOException{
        return loadZooGraphJSON(context.getAssets().open(path));
    }

    /**
     * Load edge ZooData as a graph from an arbitrary input stream, use this method for non-instrumented testing
     * @param inputStream input stream representing the JSON, FileInputStream works well for this
     * @return ZooData in Graph format
     */
    public static Graph<String, IdentifiedWeightedEdge> loadZooGraphJSON(InputStream inputStream) {
        // Create an empty graph to populate.
        Graph<String, IdentifiedWeightedEdge> g = new DefaultUndirectedWeightedGraph<>(IdentifiedWeightedEdge.class);

        // Create an importer that can be used to populate our empty graph.
        JSONImporter<String, IdentifiedWeightedEdge> importer = new JSONImporter<>();

        // We don't need to convert the vertices in the graph, so we return them as is.
        importer.setVertexFactory(v -> v);

        // We need to make sure we set the IDs on our edges from the 'id' attribute.
        // While this is automatic for vertices, it isn't for edges. We keep the
        // definition of this in the IdentifiedWeightedEdge class for convenience.
        importer.addEdgeAttributeConsumer(IdentifiedWeightedEdge::attributeConsumer);

        // On Android, you would use context.getAssets().open(path) here like in Lab 5.
        // InputStream inputStream = App.class.getClassLoader().getResourceAsStream(path);
        Reader reader = new InputStreamReader(inputStream);

        // And now we just import it!
        importer.importGraph(g, reader);

        return g;
    }
}