package com.team2.zooseeker.viewModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.team2.zooseeker.model.ReplanModel;
import com.team2.zooseeker.model.PathDao;
import com.team2.zooseeker.model.PathDatabase;
import com.team2.zooseeker.model.PathModel;
import com.team2.zooseeker.model.RouteModel;

import org.jgrapht.Graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private boolean reroute = true;

    public DirectionListViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        ExhibitsListDatabase db = ExhibitsListDatabase.getSingleton(context);
        exhibitsListDao = db.exhibitsListDao();
        pathDao = PathDatabase.getSingleton(context).pathDao();
        this.currentExhibit = pathDao.getAllVisited(true).size();
        Log.d("DEBUG", "This the current current exhibit " + currentExhibit);

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
    public void populateList(DirectionListAdapter adapter, TextView prev, TextView next) {
        ArrayList<String> exhibits = ExhibitModel
                .getExhibitNames(exhibitsListDao.getAllSelected(true));
        ArrayList<String> validatedExhibits = routeModel.validateExhibitList(exhibits);
        ArrayList<String> route = routeModel.genRoute(validatedExhibits);
        populatePathDatabase(route);

        updatePath();
        updateDirections(adapter, prev, next);
    }

    public void updateDirections(DirectionListAdapter adapter, TextView prev, TextView next) {
        ArrayList<String> directions;
        if (DirectionModeManager.getSingleton().getIsInDetailedMode()) {
            directions = routeModel.getDirections(pathToNext.get(0), pathToNext.get(pathToNext.size() - 1));
        } else {
            directions = routeModel.getBriefDirections(pathToNext.get(0), pathToNext.get(pathToNext.size() - 1));
        }
        adapter.setDirections(directions);
        for (int i = 0; i < directions.size() - 1; i++) {
            adapter.incrementNumToDisplay();
        }
        updatePrevNext(prev, next);
    }

    public void updateDirectionsBack(DirectionListAdapter adapter, TextView prev, TextView next) {
        ArrayList<String> directions;
//        for (String p: pathToNext) {
//            Log.d("path", p);
//        }
//        Log.d("directions", pathToNext.get(currentExhibit + 1));
//        Log.d("directions", pathToNext.get(currentExhibit ));
        if (DirectionModeManager.getSingleton().getIsInDetailedMode()) {
            directions = routeModel.getDirections(getNextExhibit().id, getPrevExhibit().id);
        } else {
            directions = routeModel.getBriefDirections(getNextExhibit().id, getPrevExhibit().id);
        }
        for (String d: directions) {
            Log.d("directions", d);
        }
        adapter.setDirections(directions);
        for (int i = 0; i < directions.size() - 1; i++) {
            adapter.incrementNumToDisplay();
        }

        updatePrevNextBackButton(prev, next);
    }

    public void reloadDirections(DirectionListAdapter adapter, TextView prev, TextView next) {
        updateDirections(adapter, prev, next);
    }

    public void nextExhibit(DirectionListAdapter adapter, TextView prev, TextView next) {
//        currentExhibit = pathDao.getAllVisited(true).size();
        if (currentExhibit == pathDao.getAll().size() - 2) {
            Log.d("DEBUG", "max size");
            return;
        }
        reroute = true;
        currentExhibit++;
        for(int i = 0; i < currentExhibit; i ++){
            PathModel p = pathDao.getAll().get(i);
            p.setVisited(true);
            pathDao.update(p);
        }
        Log.d("DEBUG", "Number of visited Exhbits " + pathDao.getAllVisited(true).size());
        Log.d("DEBUG", "This the current current exhibit " + currentExhibit);
        updatePath();
        updateDirections(adapter, prev, next);
    }

    public void skipExhibit(DirectionListAdapter adapter, TextView prev, TextView next) {
//        currentExhibit = pathDao.getAllVisited(true).size();
        if (currentExhibit == pathDao.getAll().size() - 2) {
            Log.d("DEBUG", "max size");
            return;
        }
        reroute = true;
        currentExhibit += 2;
        for(int i = 0; i < currentExhibit; i ++){
            PathModel p = pathDao.getAll().get(i);
            p.setVisited(true);
            pathDao.update(p);
        }
        Log.d("DEBUG", "Number of visited Exhbits" + pathDao.getAllVisited(true).size());
        Log.d("DEBUG", "This the current current exhibit " + currentExhibit);
        updatePath();
        updateDirections(adapter, prev, next);
    }

    public boolean prevExhibit(DirectionListAdapter adapter, TextView prev, TextView next) {
//        currentExhibit = pathDao.getAllVisited(true).size();
        if (currentExhibit < 0) {
            Log.d("DEBUG", "min size");
            return false;
        }
        Log.d("currentExhibit", String.valueOf(currentExhibit));
        reroute = true;
        PathModel p = pathDao.getAll().get(currentExhibit);
        p.setVisited(false);
        pathDao.update(p);
        Log.d("DEBUG", "Number of visited Exhbits" + pathDao.getAllVisited(true).size());
        Log.d("DEBUG", "This the current current exhibit " + currentExhibit);
        updatePath();
        updateDirectionsBack(adapter, prev, next);
        currentExhibit--;
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
        pathToNext = routeModel.getPath(RouteModel.getId(pathDao.getAll().get(currentExhibit)), pathDao.getAll().get(currentExhibit + 1).id);
    }

    public void populatePathDatabase(ArrayList<String> exhibitIds) {
        pathDao.deleteAll();
        for (int i = 0; i < exhibitIds.size(); i++) {
            pathDao.insert(new PathModel(Objects.requireNonNull(vertexInfo.get(exhibitIds.get(i))), i));
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
    public void autoUpdateRoute(Activity activity, DirectionListAdapter adapter, TextView prev, TextView next) {
        // Set up location listener
        String provider = LocationManager.GPS_PROVIDER;
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = location -> {
            if (MockLocationStore.getSingleton().gpsEnabled()) {
                performAutoUpdate(activity, adapter, prev, next, location);
            }
        };
        locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);
    }

    private void performAutoUpdate(Activity activity, DirectionListAdapter adapter, TextView prev, TextView next, Location location) {
        ReplanModel replan = new ReplanModel(activity, "zoo_node_info.json");
        Log.d("DEBUG REPLAN", String.format("Location changed: %s", location));
        if (replan.offTrack(location, pathToNext)) {
            ZooData.VertexInfo currentNode = replan.getNearestLandmark(location);
            Log.d("DEBUG REPLAN", String.format("Nearest landmark is now %s", currentNode.name));

            // Generate route from current position for comparison
            List<PathModel> fullPath = pathDao.getAll();
            ArrayList<String> newRoute = genRemainingRoute(currentNode);

            Log.d("DEBUG REPLAN", String.format("Current next exhibit is %s, nearest next exhibit is %s",
                    RouteModel.getId(fullPath.get(currentExhibit + 1)), newRoute.get(1)));

            if (!newRoute.get(1).equals(RouteModel.getId(fullPath.get(currentExhibit + 1)))) {
                attemptReroute(activity, currentNode, adapter, prev, next);
            }
            updatePath(RouteModel.getId(currentNode), RouteModel.getId(getNextExhibit()));
            updateDirections(adapter, prev, next);
        }
    }

    public void reloadAutoUpdate(Activity activity, DirectionListAdapter adapter, TextView prev, TextView next) {
        if (MockLocationStore.getSingleton().gpsEnabled()) {
            autoUpdateRoute(activity, adapter, prev, next);
        } else {
            performAutoUpdate(activity, adapter, prev, next, MockLocationStore.getSingleton());
        }
    }

    public ArrayList<String> genRemainingRoute(ZooData.VertexInfo currentNode) {
        ArrayList<String> routeRemaining = new ArrayList<>();
        List<PathModel> fullPath = pathDao.getAll();
        for (int i = currentExhibit + 1; i < fullPath.size() - 1; i++) {
            routeRemaining.add(RouteModel.getId(fullPath.get(i)));
        }
        routeModel.setExhibits(routeRemaining);
        return routeModel.genSubRoute(RouteModel.getId(currentNode), "entrance_exit_gate");
    }

    public void attemptReroute(Activity activity, ZooData.VertexInfo currentNode, DirectionListAdapter adapter, TextView prevDisplay, TextView nextDisplay) {
        if (!reroute) {
            return;
        }
        AlertDialog prompt = new AlertDialog.Builder(activity).setMessage("Off Track. Replan?").setPositiveButton("Yes",
                (DialogInterface dialog, int which) -> {
            reRoute(currentNode);
            updatePath();
            updateDirections(adapter, prevDisplay, nextDisplay);
            reroute = true;
            dialog.dismiss();
        }).setNegativeButton("No",
                (DialogInterface dialog, int which) -> {
            dialog.dismiss();
                }).create();
        reroute = false;
        prompt.create();
        prompt.show();
    }

    public void reRoute(ZooData.VertexInfo currentNode) {
        ArrayList<String> remainingRoute = genRemainingRoute(currentNode);
        List<PathModel> currentRoute = pathDao.getAll();

        ArrayList<String> newPathIds = new ArrayList<>();
        for (int i = 0; i <= currentExhibit; i++) {
            newPathIds.add(RouteModel.getId(currentRoute.get(i)));
        }
        for (int i = 1; i < remainingRoute.size(); i++) {
            newPathIds.add(remainingRoute.get(i));
        }
        Log.d("DEBUG REPLAN", newPathIds.toString());

        populatePathDatabase(newPathIds);
    }

    public PathModel getNextExhibit() {
        return pathDao.getAll().get(currentExhibit + 1);
    }

    public PathModel getPrevExhibit() {
        return pathDao.getAll().get(currentExhibit);
    }

    public void updatePrevNext(TextView prevDisplay, TextView nextDisplay) {
        String prev = getPrevExhibit().name;
        String next = getNextExhibit().name;
        prevDisplay.setText(String.format("From: %s", prev));
        nextDisplay.setText(String.format("To: %s", next));
    }

    public void updatePrevNextBackButton(TextView prevDisplay, TextView nextDisplay) {
        String prev = getNextExhibit().name;
        String next = getPrevExhibit().name;
        prevDisplay.setText(String.format("From: %s", prev));
        nextDisplay.setText(String.format("To: %s", next));
    }
}
