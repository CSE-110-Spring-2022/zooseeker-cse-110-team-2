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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.team2.zooseeker.model.ExhibitModel;
import com.team2.zooseeker.model.ExhibitsListDao;
import com.team2.zooseeker.model.ExhibitsListDatabase;
import com.team2.zooseeker.model.IdentifiedWeightedEdge;
import com.team2.zooseeker.model.ZooData;

public class DirectionListViewModel extends AndroidViewModel {

    private final ExhibitsListDao exhibitsListDao;
    private RouteModel routeModel;
    private final PathDao pathDao;
    private Map<String, ZooData.VertexInfo> vertexInfo;
    private List<String> pathToNext;
    private int currentExhibit = 0;

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
        ArrayList<String> route = routeModel.genRoute(validatedExhibits);
        populatePathDatabase(route);

        updatePath();
        updateDirections(adapter);
//        ArrayList<String> directions = routeModel.getDirections(route);
//        adapter.setDirections(directions);
    }

    public void updateDirections(DirectionListAdapter adapter) {
//        ArrayList<String> exhibits = new ArrayList<>();
//        List<PathModel> pathModels = pathDao.getAll();
//        for (PathModel path : pathModels) {
//            exhibits.add(path.name);
//        }
//        ArrayList<String> route = routeModel.genRoute(exhibits);
        ArrayList<String> directions = routeModel.getDirections(pathToNext.get(0), pathToNext.get(pathToNext.size() - 1));
        adapter.setDirections(directions);
        for (int i = 0; i < directions.size() - 1; i++) {
            adapter.incrementNumToDisplay();
        }
    }

    public void nextExhibit(DirectionListAdapter adapter) {
        if (currentExhibit == pathDao.getAll().size() - 2) {
            Log.d("DEBUG", "max size");
            return;
        }
        currentExhibit++;
        updatePath();
        updateDirections(adapter);
    }

    public boolean prevExhibit(DirectionListAdapter adapter) {
        if (currentExhibit == 0) {
            Log.d("DEBUG", "min size");
            return false;
        }
        currentExhibit--;
        updatePath();
        updateDirections(adapter);
        return true;
    }

    public boolean exhibitsRemaining() {
        return currentExhibit != pathDao.getAll().size() - 2;
    }

    /**
     * Updates the current path with starting and ending positions
     * @param start name of starting exhibit/vertex
     * @param end name of ending exhibit
     */
    public void updatePath(String start, String end) {
        pathToNext = routeModel.getPath(start, end);
    }

    /**
     * Updates the current path based on pathDao and currentExhibit
     */
    public void updatePath() {
        pathToNext = routeModel.getPath(pathDao.getAll().get(currentExhibit).id, pathDao.getAll().get(currentExhibit + 1).id);
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
