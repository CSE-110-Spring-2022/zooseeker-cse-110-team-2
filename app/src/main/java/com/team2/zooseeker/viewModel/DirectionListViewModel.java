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
import com.team2.zooseeker.view.DirectionListActivity;

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
                    .loadVertexInfoJSON(application, "sample_node_info.json");
            Map<String, ZooData.EdgeInfo> edgeInfo = ZooData
                    .loadEdgeInfoJSON(application, "sample_edge_info.json");
            Graph<String, IdentifiedWeightedEdge> graph = ZooData
                    .loadZooGraphJSON(application, "sample_zoo_graph.json");
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
        routeModel.setExhibits(exhibits);
        ArrayList<String> route = routeModel.genRoute();
        ArrayList<String> directions = routeModel.getDirections(route);
        adapter.setDirections(directions);
        Log.d("DEBUG ROUTE", directions.toString());
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

        // Set up location listener
        String provider = LocationManager.GPS_PROVIDER;
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = location -> {
            Log.d("DEBUG", String.format("Location changed: %s", location));
                /*if (replan.offTrack(location)) { //TODO: needs nodes at each end of current edge to pass in
                    if (generate route from position != current route remaining) {
                        prompt to replan (check that user hasn't been prompted recently)
                    }
                    auto-update directions
                    //TODO: Prompt user (once) to re-plan route
                }*/
        };
        locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);
    }
}
