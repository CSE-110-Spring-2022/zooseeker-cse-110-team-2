package com.team2.zooseeker.viewModel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team2.zooseeker.R;
import com.team2.zooseeker.model.PathDao;
import com.team2.zooseeker.model.PathModel;

import java.util.ArrayList;
import java.util.List;

public class RouteSummaryAdapter extends RecyclerView.Adapter<RouteSummaryAdapter.ViewHolder>{

    private List<PathModel> exhibits = new ArrayList<>();

    public void setExhibits(List<PathModel> e) {
        exhibits.clear();
        exhibits = e;
        Log.d("DEBUG SUMMARY", exhibits.toString());
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public RouteSummaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.route_summary_item, parent, false);
        return new RouteSummaryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteSummaryAdapter.ViewHolder holder, int position) {
        Log.d("DEBUG VIEW HOLDER", exhibits.get(position).toString());
        holder.setTextView(exhibits.get(position));
    }

    @Override
    public int getItemCount() {
        return exhibits.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private PathModel exhibit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.route_summary_item);
        }

        public void setTextView(PathModel e) {
            exhibit = e;
            textView.setText(e.name);
            Log.d("DEBUG VH", "687");

        }


    }
}
