package com.team2.zooseeker.viewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team2.zooseeker.R;
import com.team2.zooseeker.model.DirectionModel;

import java.util.Collections;
import java.util.List;

public class DirectionListAdapter extends RecyclerView.Adapter<DirectionListAdapter.ViewHolder> {
    private List<DirectionModel> directionModels = Collections.emptyList();

    public void setDirectionModels(List<DirectionModel> newDirectionModels) {
        this.directionModels.clear();
        this.directionModels = newDirectionModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.directions_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DirectionListAdapter.ViewHolder holder, int position) {
        holder.setDirectionModel(directionModels.get(position));
    }

    @Override
    public int getItemCount() {
        return directionModels.size();
    }

    @Override
    public long getItemId(int position) {return directionModels.get(position).id;}

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private DirectionModel directionModel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.direction_item_text);
        }

        // TODO: it needs to be ex: turn left at canal street, in x feet turn left
        public void setDirectionModel(DirectionModel directionModel) {
            this.directionModel = directionModel;
            this.textView.setText(directionModel.street);
        }
    }
}
