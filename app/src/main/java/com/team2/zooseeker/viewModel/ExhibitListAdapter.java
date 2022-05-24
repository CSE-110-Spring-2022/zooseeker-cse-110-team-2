package com.team2.zooseeker.viewModel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team2.zooseeker.R;
import com.team2.zooseeker.model.ExhibitModel;
import com.team2.zooseeker.model.SearchModel;
import com.team2.zooseeker.view.MainActivity;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ExhibitListAdapter extends RecyclerView.Adapter<ExhibitListAdapter.ViewHolder>{

    private List<ExhibitModel> exhibitModelList = Collections.emptyList();
    private Consumer<ExhibitModel> onCheckBoxClicked;
    public static TextView counter;

    public void setExhibits(List<ExhibitModel> newExhibitModel){
        this.exhibitModelList.clear();
        this.exhibitModelList = newExhibitModel;
        Log.d("DEBUG", this.exhibitModelList.toString());
        notifyDataSetChanged();
    }

    public void setCounter(TextView textView){
        counter = textView;

    }

    public void setOnCheckBoxClickedListener(Consumer<ExhibitModel> onCheckBoxClicked){
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
        holder.setExhibit(exhibitModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.exhibitModelList.size();
    }

    @Override
    public long getItemId(int position){
        return exhibitModelList.get(position).id;
    }

    public List<ExhibitModel> getExhibitList(){
        return this.exhibitModelList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ExhibitModel exhibitModel;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View exhibitView){
            super(exhibitView);
            this.checkBox = exhibitView.findViewById(R.id.exhibitModel);

            this.checkBox.setOnClickListener(view -> {
                if (onCheckBoxClicked == null) return;
                onCheckBoxClicked.accept(exhibitModel);
                SearchModel.setExhibitSelected(exhibitModel);
                int count = SearchModel.getCount();

                counter.setText(String.valueOf(count));

            });

        }

        public ExhibitModel getExhibit(){
            return this.exhibitModel;
        }

        public void setExhibit(ExhibitModel vertexInfo){
            this.exhibitModel = vertexInfo;
            this.checkBox.setChecked(this.exhibitModel.selected);
            this.checkBox.setText(this.exhibitModel.name);
        }
    }
}
