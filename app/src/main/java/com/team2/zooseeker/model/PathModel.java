package com.team2.zooseeker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "paths_database")
public class PathModel {

    @PrimaryKey
    public long numericId;

    public String id;
    public ZooData.VertexInfo.Kind kind;
    public String name;
    public double lat;
    public double lng;
    public int position;

    public PathModel(ZooData.VertexInfo v, int pos) {
        this.id = v.id;
        this.kind = v.kind;
        this.name = v.name;
        this.lat = v.lat;
        this.lng = v.lng;
        this.position = pos;
    }

    public PathModel() {

    }
}
