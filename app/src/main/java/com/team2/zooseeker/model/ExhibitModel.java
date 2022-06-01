package com.team2.zooseeker.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity(tableName = "exhibits_database")
public class ExhibitModel {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public boolean selected;
    public String name;
    public String dataId;
    public String groupId;
    public String kind;
    public String tags;

    public ExhibitModel(){
        name = null;
        dataId = null;
    }

    public ExhibitModel(boolean selected, String name){
        this.selected = false;
        this.kind = "exhibit";
        this.tags = "[]";
    }

    public void setSelected(boolean selected) {this.selected = selected;}

    public ExhibitModel(boolean selected, String name, String kind, List<String> tags){
        this.selected = selected;
        this.name = name;
        this.kind = kind;
        this.tags = tags.toString();
    }

    public ExhibitModel(ZooData.VertexInfo vertexInfo){
        this.selected = false;
        this.name = vertexInfo.name;
        this.dataId = vertexInfo.id;
//        this.kind = vertexInfo.kind.name();
        if(vertexInfo.kind.equals(ZooData.VertexInfo.Kind.EXHIBIT)){
            this.kind = "exhibit";
        }
        else if(vertexInfo.kind.equals(ZooData.VertexInfo.Kind.GATE)) {
            this.kind = "gate";
        }
        else if (vertexInfo.kind.equals(ZooData.VertexInfo.Kind.EXHIBIT_GROUP)) {
            this.kind = "exhibit_group";
        }
        else{
            this.kind = "intersection";
        }
        this.tags = vertexInfo.tags.toString();
        if(vertexInfo.group_id == null) this.groupId = vertexInfo.id;
        else this.groupId = vertexInfo.group_id;
    }

    public static List<ExhibitModel> convert(Map<String, ZooData.VertexInfo> map){
        List<ExhibitModel> list = new ArrayList<>();

        Set keys = map.keySet();
        Iterator key = keys.iterator();

        while(key.hasNext()){
            String current = key.next().toString();
            ExhibitModel newExhibitModel = new ExhibitModel(map.get(current));
            list.add(newExhibitModel);
        }
        return list;
    }

    public static ArrayList<String> getExhibitNames(List<ExhibitModel> exhibitModels) {
        ArrayList<String> exhibitNames = new ArrayList<>();
        for (ExhibitModel e : exhibitModels) {
            exhibitNames.add(e.dataId);
        }
        return exhibitNames;
    }

    @Override
    public String toString() {
//        return "Exhibit{" +
//                "id=" + id +
//                ", selected=" + selected +
//                ", name='" + name + '\'' +
//                '}';
        return "Exhibit: " + name;
    }
}
