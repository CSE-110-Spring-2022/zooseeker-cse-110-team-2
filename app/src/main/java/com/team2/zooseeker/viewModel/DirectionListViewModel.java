package com.team2.zooseeker.viewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.team2.zooseeker.model.RouteModel;

import org.jgrapht.Graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cse110.Exhibit;
import cse110.ExhibitsListDao;
import cse110.ExhibitsListDatabase;
import cse110.IdentifiedWeightedEdge;
import cse110.ZooData;

public class DirectionListViewModel extends AndroidViewModel {

    private final ExhibitsListDao exhibitsListDao;
    private RouteModel routeModel;

    public DirectionListViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        ExhibitsListDatabase db = ExhibitsListDatabase.getSingleton(context);
        exhibitsListDao = db.exhibitsListDao();
        try {
            Map<String, ZooData.VertexInfo> vertexInfo = ZooData.loadVertexInfoJSON(application, "sample_node_info.json");
            Map<String, ZooData.EdgeInfo> edgeInfo = ZooData.loadEdgeInfoJSON(application, "sample_edge_info.json");
            Graph<String, IdentifiedWeightedEdge> graph = ZooData.loadZooGraphJSON(application, "sample_zoo_graph.json");
            routeModel = new RouteModel(graph, vertexInfo, edgeInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateList(DirectionListAdapter adapter) {
        ArrayList<String> exhibits = Exhibit.getExhibitNames(exhibitsListDao.getAllSelected(true));
        routeModel.setExhibits(exhibits);
        ArrayList<String> route = routeModel.genRoute();
        ArrayList<String> directions = routeModel.getDirections(route);
        adapter.setDirections(directions);
        Log.d("DEBUG ROUTE", directions.toString());

    }
}
