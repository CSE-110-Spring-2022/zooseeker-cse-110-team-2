package cse110;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team2.zooseeker.R;
import com.team2.zooseeker.model.SearchModel;
import com.team2.zooseeker.viewModel.SearchViewModel;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ExhibitListAdapter extends RecyclerView.Adapter<ExhibitListAdapter.ViewHolder>{

    private List<Exhibit> exhibitList = Collections.emptyList();
    private Consumer<Exhibit> onCheckBoxClicked;
    public static TextView counter;

    public void setExhibits(List<Exhibit> newExhibit){
        this.exhibitList.clear();
        this.exhibitList = newExhibit;
        Log.d("DEBUG", this.exhibitList.toString());
        notifyDataSetChanged();
    }

    public void setCounter(TextView textView){
        counter = textView;

    }

    public void setOnCheckBoxClickedListener(Consumer<Exhibit> onCheckBoxClicked){
        this.onCheckBoxClicked = onCheckBoxClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.exhibit_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setExhibit(exhibitList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.exhibitList.size();
    }

    @Override
    public long getItemId(int position){
        return exhibitList.get(position).id;
    }

    public List<Exhibit> getExhibitList(){
        return this.exhibitList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private Exhibit exhibit;
        private CheckBox checkBox;


        public ViewHolder(@NonNull View exhibitView){
            super(exhibitView);
            this.checkBox = exhibitView.findViewById(R.id.exhibit);

            this.checkBox.setOnClickListener(view -> {
                if (onCheckBoxClicked == null) return;
                onCheckBoxClicked.accept(exhibit);
                SearchModel.setExhibitSelected(exhibit);
                int count = SearchModel.getCount();

                counter.setText(String.valueOf(count));

            });
        }

        public Exhibit getExhibit(){
            return this.exhibit;
        }

        public void setExhibit(Exhibit vertexInfo){
            this.exhibit = vertexInfo;
            this.checkBox.setChecked(this.exhibit.selected);
            this.checkBox.setText(this.exhibit.name);
        }


    }


}
