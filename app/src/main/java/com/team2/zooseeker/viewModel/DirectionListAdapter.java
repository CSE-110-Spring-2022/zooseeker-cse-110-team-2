package com.team2.zooseeker.viewModel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team2.zooseeker.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

public class DirectionListAdapter extends RecyclerView.Adapter<DirectionListAdapter.ViewHolder> {
    private ArrayList<String> directions = new ArrayList<>();
    private int numToDisplay = 1;

    public void incrementNumToDisplay() {
        if (numToDisplay < directions.size()) {
            numToDisplay++;
        } else {
            numToDisplay = 1;
        }
        Log.d("DEBUG", Integer.toString(numToDisplay));
        notifyDataSetChanged();
    }

    public void setDirections(ArrayList<String> directions) {
        this.directions.clear();
        this.numToDisplay = 1;
        this.directions = directions;
        Log.d("DEBUG ROUTE ADAPTER", directions.toString());
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
        if (directions.size() > 0) {
            holder.setTextView(directions.get(position));
        } else {
            holder.setTextView("Please Select Exhibit to Visit");
        }
    }

    @Override
    public int getItemCount() {
        return numToDisplay;
    }

    /**
     * @DylanB5402
     * See: https://stackoverflow.com/questions/44932450/wrong-order-of-restored-items-in-recyclerview
     * code here is used to force the RecyclerView to display directions in order
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private String direction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.direction_item_text);
        }

        public void setTextView(String direction) {
            this.direction = direction;
            this.textView.setText(direction);
        }
    }
}
