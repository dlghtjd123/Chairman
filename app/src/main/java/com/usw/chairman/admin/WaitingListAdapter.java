package com.usw.chairman.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usw.chairman.R;
import com.usw.chairman.model.WaitingRentalResponse;

import java.util.List;

public class WaitingListAdapter extends RecyclerView.Adapter<WaitingListAdapter.ViewHolder> {

    private final List<WaitingRentalResponse> rentalList;
    private final OnRentalActionListener actionListener;

    public WaitingListAdapter(List<WaitingRentalResponse> rentalList, OnRentalActionListener actionListener) {
        this.rentalList = rentalList;
        this.actionListener = actionListener;
    }

    public interface OnRentalActionListener {
        void onApprove(WaitingRentalResponse rental);
        void onReject(WaitingRentalResponse rental);
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
        WaitingRentalResponse rental = rentalList.get(position);

        // 휠체어 ID
        holder.textViewWheelchairId.setText("휠체어 ID: " +
                (rental.getWheelchair() != null && rental.getWheelchair().getWheelchairId() != null
                        ? rental.getWheelchair().getWheelchairId()
                        : "정보 없음"));

        // 대여 ID
        holder.textViewRentalId.setText("대여 ID: " + rental.getRentalId());

        // 휠체어 타입
        holder.textViewWheelchairType.setText("휠체어 타입: " +
                (rental.getWheelchair() != null && rental.getWheelchair().getType() != null
                        ? rental.getWheelchair().getType()
                        : "정보 없음"));

        // 대여일 및 반납일
        holder.textViewRentalDates.setText("대여일: " + rental.getRentalDate() +
                "\n반납일: " + rental.getReturnDate());

        // 대여인 이름
        holder.textViewuserName.setText("대여인: " +
                (rental.getUser() != null && rental.getUser().getName() != null
                        ? rental.getUser().getName()
                        : "정보 없음"));

        // 대여인 전화번호 설정
        holder.textViewUserPhone.setText("전화번호: " +
                (rental.getUser() != null && rental.getUser().getPhoneNumber() != null
                        ? rental.getUser().getPhoneNumber()
                        : "정보 없음"));

        // 승인 버튼 클릭 리스너
        holder.buttonApprove.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onApprove(rental); // Listener의 승인 동작 호출
            }
        });

        // 거절 버튼 클릭 리스너
        holder.buttonReject.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onReject(rental); // Listener의 거절 동작 호출
            }
        });
    }

    @Override
    public int getItemCount() {
        return rentalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewInstitutionName, textViewWheelchairId, textViewRentalId, textViewWheelchairType,
                textViewRentalDates, textViewuserName, textViewUserPhone;
        Button buttonApprove, buttonReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewInstitutionName = itemView.findViewById(R.id.textViewInstitutionName);
            textViewRentalId = itemView.findViewById(R.id.textViewRentalId);
            textViewWheelchairId = itemView.findViewById(R.id.textViewWheelchairId);
            textViewWheelchairType = itemView.findViewById(R.id.textViewWheelchairType);
            textViewRentalDates = itemView.findViewById(R.id.textViewRentalDates);
            textViewuserName = itemView.findViewById(R.id.textViewuserName);
            textViewUserPhone = itemView.findViewById(R.id.textViewUserPhone); // 전화번호 초기화
            buttonApprove = itemView.findViewById(R.id.buttonApprove);
            buttonReject = itemView.findViewById(R.id.buttonReject);
        }
    }
}
