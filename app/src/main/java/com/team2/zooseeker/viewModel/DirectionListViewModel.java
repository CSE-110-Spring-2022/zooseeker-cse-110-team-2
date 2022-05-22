package com.team2.zooseeker.viewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.team2.zooseeker.model.RouteModel;

import org.jgrapht.Graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.team2.zooseeker.model.ExhibitModel;
import com.team2.zooseeker.model.ExhibitsListDao;
import com.team2.zooseeker.model.ExhibitsListDatabase;
import com.team2.zooseeker.model.IdentifiedWeightedEdge;
import com.team2.zooseeker.model.ZooData;

public class DirectionListViewModel extends AndroidViewModel {

    private final ExhibitsListDao exhibitsListDao;
    private RouteModel routeModel;

    public DirectionListViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        ExhibitsListDatabase db = ExhibitsListDatabase.getSingleton(context);
        exhibitsListDao = db.exhibitsListDao();
        try {
            Map<String, ZooData.VertexInfo> vertexInfo = ZooData
                    .loadVertexInfoJSON(application, "zoo_node_info.json");
            Map<String, ZooData.EdgeInfo> edgeInfo = ZooData
                    .loadEdgeInfoJSON(application, "zoo_edge_info.json");
            Graph<String, IdentifiedWeightedEdge> graph = ZooData
                    .loadZooGraphJSON(application, "zoo_graph.json");
            routeModel = new RouteModel(graph, vertexInfo, edgeInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exactly like a DefaultWeightedEdge, but has an id field we
     * @param adapter adapter to connect UI and direction ArrayList of String
     */
    public void populateList(DirectionListAdapter adapter) {
        ArrayList<String> exhibits = ExhibitModel
                .getExhibitNames(exhibitsListDao.getAllSelected(true));

//        routeModel.setExhibits(exhibits);
        ArrayList<String> validatedExhibits = routeModel.validateExhibitList(exhibits);
        Log.d("DEBUG", validatedExhibits.toString());
        ArrayList<String> route = routeModel.genRoute(validatedExhibits);
        ArrayList<String> directions = routeModel.getDirections(route);
        adapter.setDirections(directions);
        Log.d("DEBUG ROUTE", directions.toString());
    }
}
