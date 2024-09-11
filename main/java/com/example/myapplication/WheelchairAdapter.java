package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WheelchairAdapter extends RecyclerView.Adapter<WheelchairAdapter.WheelchairViewHolder> {

    private List<Wheelchair> wheelchairList;

    public WheelchairAdapter(List<Wheelchair> wheelchairList) {
        this.wheelchairList = wheelchairList;
    }

    @NonNull
    @Override
    public WheelchairViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wheelchair, parent, false);
        return new WheelchairViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WheelchairViewHolder holder, int position) {
        Wheelchair wheelchair = wheelchairList.get(position);
        holder.wheelchairId.setText(wheelchair.getId());
        holder.wheelchairStatus.setText(wheelchair.getStatus());
    }

    @Override
    public int getItemCount() {
        return wheelchairList.size();
    }

    public void updateList(List<Wheelchair> newList) {
        wheelchairList.clear();
        wheelchairList.addAll(newList);
        notifyDataSetChanged();
    }

    static class WheelchairViewHolder extends RecyclerView.ViewHolder {

        TextView wheelchairId;
        TextView wheelchairStatus;

        public WheelchairViewHolder(@NonNull View itemView) {
            super(itemView);
            wheelchairId = itemView.findViewById(R.id.wheelchairId);
            wheelchairStatus = itemView.findViewById(R.id.wheelchairStatus);
        }
    }
}
