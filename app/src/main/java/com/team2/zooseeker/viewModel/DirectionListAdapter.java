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
//    private Consumer<String> onNextButtonClicked;

    public void setDirections(ArrayList<String> directions) {
        this.directions.clear();
        this.directions = directions;
        Log.d("DEBUG ROUTE ADAPTER", directions.toString());
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private String direction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.direction_item_text);
//            this.nextButton.setOnClickListener(view -> {
//                if(onNextButtonClicked == null) return;
//                onNextButtonClicked.accept(direction);
//            });
        }

        public void setTextView(String direction) {
            this.direction = direction;
            this.textView.setText(direction);
        }
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
        holder.setTextView(directions.get(position));
    }

    @Override
    public int getItemCount() {
        return directions.size();
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

}
