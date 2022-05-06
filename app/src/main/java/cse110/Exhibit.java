package cse110;

import android.content.Context;

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

    public Exhibit(){
        name = null;
    }

    public Exhibit(boolean selected, String name){
        this.selected = selected;
        this.name = name;
    }

    public Exhibit(ZooData.VertexInfo vertexInfo){
        this.selected = false;
        this.name = vertexInfo.name;
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
}
