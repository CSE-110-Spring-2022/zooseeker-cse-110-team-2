package com.team2.zooseeker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "paths_database")
public class PathModel {

    @PrimaryKey(autoGenerate = true)
    public long numericId;

    public String id;
    public ZooData.VertexInfo.Kind kind;
    public String name;
    public double lat;
    public double lng;
    public int position;
    public String parent_id;
    public boolean visited;

    public PathModel(ZooData.VertexInfo v, int pos) {
        this.id = v.id;
        this.kind = v.kind;
        this.name = v.name;
        this.lat = v.lat;
        this.lng = v.lng;
        this.parent_id = v.group_id;
        this.position = pos;
        this.visited = false;
    }

    public PathModel() {

    }

    public void setVisited(boolean v){
        this.visited = v;
    }

    @Override
    public String toString() {
        return "PathModel{" +
//                "numericId=" + numericId +
                ", id='" + id + '\'' +
//                ", kind=" + kind +
//                ", name='" + name + '\'' +
//                ", lat=" + lat +
//                ", lng=" + lng +
                ", position=" + position +
//                ", parent_id='" + parent_id + '\'' +
                '}';
    }
}
