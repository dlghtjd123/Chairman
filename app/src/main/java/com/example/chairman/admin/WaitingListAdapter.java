package com.example.chairman.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chairman.R;
import com.example.chairman.model.RentalResponse;

import java.util.List;

public class WaitingListAdapter extends RecyclerView.Adapter<WaitingListAdapter.ViewHolder> {

    private final List<RentalResponse> rentalList;
    private final OnRentalActionListener actionListener;

    public WaitingListAdapter(List<RentalResponse> rentalList, OnRentalActionListener actionListener) {
        this.rentalList = rentalList;
        this.actionListener = actionListener;
    }

    public interface OnRentalActionListener {
        void onApprove(RentalResponse rental);
        void onReject(RentalResponse rental);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_waiting_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RentalResponse rental = rentalList.get(position);

        holder.textViewInstitutionName.setText("공공기관: " + rental.getInstitutionName());
        holder.textViewWheelchairId.setText("휠체어 ID: " + rental.getRentalId());
        holder.textViewWheelchairType.setText("휠체어 타입: " + rental.getWheelchairType());
        holder.textViewRentalDates.setText("대여일: " + rental.getRentalDate() + " ~ 반납일: " + rental.getReturnDate());
        holder.textViewRenterName.setText("대여인: " + rental.getInstitutionName()); // 대여인 정보 표시

        holder.buttonApprove.setOnClickListener(v -> actionListener.onApprove(rental));
        holder.buttonReject.setOnClickListener(v -> actionListener.onReject(rental));
    }

    @Override
    public int getItemCount() {
        return rentalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewInstitutionName, textViewWheelchairId, textViewWheelchairType,
                textViewRentalDates, textViewRenterName;
        Button buttonApprove, buttonReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewInstitutionName = itemView.findViewById(R.id.textViewInstitutionName);
            textViewWheelchairId = itemView.findViewById(R.id.textViewWheelchairId);
            textViewWheelchairType = itemView.findViewById(R.id.textViewWheelchairType);
            textViewRentalDates = itemView.findViewById(R.id.textViewRentalDates);
            textViewRenterName = itemView.findViewById(R.id.textViewRenterName);
            buttonApprove = itemView.findViewById(R.id.buttonApprove);
            buttonReject = itemView.findViewById(R.id.buttonReject);
        }
    }
}
