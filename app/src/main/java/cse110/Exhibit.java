package cse110;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity(tableName = "exhibits_database")
public class Exhibit {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public boolean selected;
    public String name;
    public String dataId;
    public String kind;
    public String tags;

    public Exhibit(){
        name = null;
        dataId = null;
    }

    public Exhibit(boolean selected, String name){
        this.selected = false;
        this.kind = "exhibit";
        this.tags = "[]";
    }

    public Exhibit(boolean selected, String name, String kind, List<String> tags){
        this.selected = selected;
        this.name = name;
        this.kind = kind;
        this.tags = tags.toString();
    }

    public Exhibit(ZooData.VertexInfo vertexInfo){
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
        else{
            this.kind = "intersection";
        }
        this.tags = vertexInfo.tags.toString();
    }

    public static List<Exhibit> convert(Map<String, ZooData.VertexInfo> map){
        List<Exhibit> list = new ArrayList<>();

        Set keys = map.keySet();
        Iterator key = keys.iterator();

        while(key.hasNext()){
            String current = key.next().toString();
            Exhibit newExhibit = new Exhibit(map.get(current));
            list.add(newExhibit);
        }
        return list;
    }

    public static ArrayList<String> getExhibitNames(List<Exhibit> exhibits) {
        ArrayList<String> exhibitNames = new ArrayList<>();
        for (Exhibit e : exhibits) {
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
