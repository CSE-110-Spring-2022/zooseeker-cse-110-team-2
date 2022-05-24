package com.team2.zooseeker.viewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.team2.zooseeker.model.PathDao;
import com.team2.zooseeker.model.PathDatabase;
import com.team2.zooseeker.model.PathModel;
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
    private PathDao pathDao;
    private Map<String, ZooData.VertexInfo> vertexInfo;

    public DirectionListViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        ExhibitsListDatabase db = ExhibitsListDatabase.getSingleton(context);
        exhibitsListDao = db.exhibitsListDao();
        pathDao = PathDatabase.getSingleton(context).pathDao();
        try {
             vertexInfo = ZooData
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
        ArrayList<String> validatedExhibits = routeModel.validateExhibitList(exhibits);
        populatePathDatabase(exhibits);
        ArrayList<String> route = routeModel.genRoute(validatedExhibits);
        ArrayList<String> directions = routeModel.getDirections(route);
        adapter.setDirections(directions);
    }

    public void populatePathDatabase(ArrayList<String> exhibitIds) {
        pathDao.deleteAll();
        for (int i = 0; i < exhibitIds.size(); i++) {
            pathDao.insert(new PathModel(vertexInfo.get(exhibitIds.get(i)), i));
        }
        Log.d("DEBUG PATH DAO", pathDao.getAll().toString());
        Log.d("DEBUG PATH DAO", Long.toString(pathDao.getAll().size()));
    }
}
