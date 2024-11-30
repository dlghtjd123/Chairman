package com.example.chairman.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chairman.R;
import com.example.chairman.model.WheelchairDetailResponse;

import java.util.List;

public class WheelchairDetailsAdapter extends RecyclerView.Adapter<WheelchairDetailsAdapter.ViewHolder> {

    private final List<WheelchairDetailResponse> wheelchairList;

    public WheelchairDetailsAdapter(List<WheelchairDetailResponse> wheelchairList) {
        this.wheelchairList = wheelchairList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wheelchair_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WheelchairDetailResponse wheelchair = wheelchairList.get(position);

        holder.textViewWheelchairId.setText("ID: " + wheelchair.getWheelchairId());
        holder.textViewStatus.setText("상태: " + wheelchair.getStatus());

        if ("RENTED".equalsIgnoreCase(wheelchair.getStatus()) || "WAITING".equalsIgnoreCase(wheelchair.getStatus())) {
            holder.textViewUserName.setVisibility(View.VISIBLE);
            holder.textViewUserPhone.setVisibility(View.VISIBLE);
            holder.textViewUserName.setText("대여인: " + wheelchair.getUserName());
            holder.textViewUserPhone.setText("전화번호: " + wheelchair.getUserPhone());
        } else {
            holder.textViewUserName.setVisibility(View.GONE);
            holder.textViewUserPhone.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return wheelchairList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewWheelchairId, textViewStatus, textViewUserName, textViewUserPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWheelchairId = itemView.findViewById(R.id.textViewWheelchairId);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserPhone = itemView.findViewById(R.id.textViewUserPhone);
        }
    }
}
