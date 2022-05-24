package com.team2.zooseeker.viewModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.team2.zooseeker.model.PermissionChecker;
import com.team2.zooseeker.model.ReplanModel;
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
import java.util.Objects;

import com.team2.zooseeker.model.ExhibitModel;
import com.team2.zooseeker.model.ExhibitsListDao;
import com.team2.zooseeker.model.ExhibitsListDatabase;
import com.team2.zooseeker.model.IdentifiedWeightedEdge;
import com.team2.zooseeker.model.ZooData;
import com.team2.zooseeker.view.DirectionListActivity;

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
    }

    public void updateDirections(DirectionListAdapter adapter) {
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

    /**
     * Contains logic for checking permissions and watching location
     * as well as what to do when location is off-route
     * @param activity DirectionListActivity
     */
    @SuppressLint("MissingPermission")
    public void autoUpdateRoute(Activity activity) {

        ReplanModel replan = new ReplanModel(activity, "zoo_node_info.json");

        // Get Location Permissions
        PermissionChecker perms = new PermissionChecker((ComponentActivity) activity);
        if (perms.ensurePermissions()) {
            return;
        }; // TODO: Account for user denying permissions
        // TODO: Also we want to reload this once anyways to actually enable permissions

        // Set up location listener
        String provider = LocationManager.GPS_PROVIDER;
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = location -> {
            Log.d("DEBUG", String.format("Location changed: %s", location));
            if (replan.offTrack(location, pathToNext)) {
                ZooData.VertexInfo currentNode = replan.getNearestLandmark(location);
                Log.d("DEBUG", String.format("Nearest landmark is now %s", currentNode.name));

                // Generate route from current position for comparison
                ArrayList<String> routeRemaining = new ArrayList<>();
                List<PathModel> fullPath = pathDao.getAll();
                for (int i = currentExhibit + 1; i < fullPath.size() - 1; i++) {
                    routeRemaining.add(fullPath.get(i).id);
                }
                routeModel.setExhibits(routeRemaining);
                ArrayList<String> newRoute = routeModel.genSubRoute(currentNode.id, "entrance_exit_gate");

                if (!newRoute.get(1).equals(fullPath.get(currentExhibit + 1).id)) {
                    Log.d("DEBUG NEW ROUTE", String.format("Current next exhibit is %s, nearest next exhibit is %s",
                            fullPath.get(currentExhibit + 1).id, newRoute.get(1)));
                    // TODO: Prompt user (once) to re-plan route, then replan and re-fill databases

                }
                // TODO: update directions either way from current position, since user is likely not on path yet
                
            }

        };
        locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);
    }
}
