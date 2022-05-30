package com.team2.zooseeker.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.team2.zooseeker.model.IdentifiedWeightedEdge;
import com.team2.zooseeker.model.PathDao;
import com.team2.zooseeker.model.PathDatabase;
import com.team2.zooseeker.model.PathModel;
import com.team2.zooseeker.model.RouteModel;
import com.team2.zooseeker.model.ZooData;

import org.jgrapht.Graph;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteSummaryViewModel extends AndroidViewModel {

    private PathDao pathDao;
    private RouteModel routeModel;

    public RouteSummaryViewModel(@NonNull Application application) {
        super(application);
        Context c = application.getApplicationContext();
        pathDao = PathDatabase.getSingleton(c).pathDao();

        try {
            var vertexInfo = ZooData
                    .loadVertexInfoJSON(application, "zoo_node_info.json");
            var edgeInfo = ZooData
                    .loadEdgeInfoJSON(application, "zoo_edge_info.json");
            var graph = ZooData
                    .loadZooGraphJSON(application, "zoo_graph.json");
            routeModel = new RouteModel(graph, vertexInfo, edgeInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateExhibits(RouteSummaryAdapter adp) {
        List<PathModel> exhibits = pathDao.getAll();
        adp.setExhibits(formatSummaryTexts(exhibits));
    }

    public List<String> formatSummaryTexts(List<PathModel> exhibits) {
        List<String> summaryTexts = new ArrayList<>();
        double dist = 0;
        for (int i = 0; i < exhibits.size(); i++) {
            var currentPathModel = exhibits.get(i);
            if (i == 0) {
                summaryTexts.add(currentPathModel.name);
            } else {
                dist = dist + routeModel.getDistanceBetween(RouteModel.getId(exhibits.get(i-1)), currentPathModel.id);
                summaryTexts.add(formatPathModel(currentPathModel, dist));
            }
        }
        return summaryTexts;
    }

    public String formatPathModel(PathModel p, double distance) {
        return p.name + ", " + distance + " feet";
    }

}
