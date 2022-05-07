package cse110;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
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

//    @Ignore
    public Exhibit(){
        name = null;
//        selected = false;
        dataId = null;
//        id = null;
    }

//    @Ignore
    public Exhibit(boolean selected, String name){
        this.selected = selected;
        this.name = name;
//        this.hidden = hidden;
    }

    public Exhibit(ZooData.VertexInfo vertexInfo){
        this.selected = false;
        this.name = vertexInfo.name;
//        this.hidden = false;
        this.dataId = vertexInfo.id;
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
