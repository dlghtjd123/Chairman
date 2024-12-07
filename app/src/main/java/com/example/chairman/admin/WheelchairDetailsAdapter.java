package com.example.chairman.admin;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chairman.R;
import com.example.chairman.model.WheelchairDetailResponse;

import java.util.List;

public class WheelchairDetailsAdapter extends RecyclerView.Adapter<WheelchairDetailsAdapter.ViewHolder> {

    private final List<WheelchairDetailResponse> wheelchairList;
    private final OnActionListener actionListener;
    private int selectedPosition = -1; // 선택된 항목 위치

    public interface OnActionListener {
        void onRent(WheelchairDetailResponse wheelchair);
        void onReturn(WheelchairDetailResponse wheelchair);
        void onAvailable(WheelchairDetailResponse wheelchair);
    }

    public WheelchairDetailsAdapter(List<WheelchairDetailResponse> wheelchairList, OnActionListener actionListener) {
        this.wheelchairList = wheelchairList;
        this.actionListener = actionListener;
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

        // 로그 추가: 데이터 상태 확인
        Log.d("Adapter", "Position: " + position + ", Wheelchair: " + wheelchair);

        // 기본 텍스트 설정
        holder.textViewWheelchairId.setText("ID: " + wheelchair.getWheelchairId());
        holder.textViewStatus.setText("휠체어 상태: " + wheelchair.getWheelchairStatus() + "\n대여 상태: " + wheelchair.getRentalStatus());
        holder.textViewType.setText("타입: " + wheelchair.getType());

        // 대여자 정보 표시
        if ("RENTED".equalsIgnoreCase(wheelchair.getWheelchairStatus()) ||
                "WAITING".equalsIgnoreCase(wheelchair.getWheelchairStatus()) ||
                "ACCEPTED".equalsIgnoreCase(wheelchair.getWheelchairStatus())) {
            holder.textViewUserName.setVisibility(View.VISIBLE);
            holder.textViewUserPhone.setVisibility(View.VISIBLE);
            holder.textViewUserName.setText("대여인: " + (wheelchair.getUserName() != null ? wheelchair.getUserName() : "정보 없음"));
            holder.textViewUserPhone.setText("전화번호: " + (wheelchair.getUserPhone() != null ? wheelchair.getUserPhone() : "정보 없음"));
        } else {
            holder.textViewUserName.setVisibility(View.GONE);
            holder.textViewUserPhone.setVisibility(View.GONE);
        }

        // 버튼 레이아웃 가시성 제어
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            holder.buttonLayout.setVisibility(View.VISIBLE);

            // 상태에 따라 버튼 가시성 설정
            if ("ACCEPTED".equalsIgnoreCase(wheelchair.getWheelchairStatus())) {
                holder.buttonReturn.setVisibility(View.GONE);   // 반납 버튼 숨기기
                holder.buttonAvailable.setVisibility(View.GONE); // 사용 가능 버튼 숨기기
                holder.buttonRent.setVisibility(View.VISIBLE); // 수락 버튼 활성화
            } else if ("RENTED".equalsIgnoreCase(wheelchair.getWheelchairStatus())) {
                holder.buttonReturn.setVisibility(View.VISIBLE); // 반납 버튼 활성화
                holder.buttonAvailable.setVisibility(View.GONE); // 사용 가능 버튼 숨기기
                holder.buttonRent.setVisibility(View.GONE);     // 수락 버튼 숨기기
            } else {
                holder.buttonReturn.setVisibility(View.GONE);
                holder.buttonAvailable.setVisibility(View.GONE);
                holder.buttonRent.setVisibility(View.GONE);
            }
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.buttonLayout.setVisibility(View.GONE);
        }

        // 항목 클릭 이벤트 처리
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = (selectedPosition == position) ? -1 : position; // 선택 토글
            notifyDataSetChanged();
        });

        // 버튼 클릭 이벤트
        holder.buttonRent.setOnClickListener(v -> actionListener.onRent(wheelchair)); // 수락 버튼 동작 정의
        holder.buttonReturn.setOnClickListener(v -> actionListener.onReturn(wheelchair)); // 반납 버튼 동작 정의
        holder.buttonAvailable.setOnClickListener(v -> actionListener.onAvailable(wheelchair)); // 사용 가능 버튼 동작 정의
    }


    @Override
    public int getItemCount() {
        return wheelchairList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewWheelchairId, textViewStatus, textViewType, textViewUserName, textViewUserPhone;
        LinearLayout buttonLayout;
        Button buttonRent, buttonReturn, buttonAvailable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWheelchairId = itemView.findViewById(R.id.textViewWheelchairId);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserPhone = itemView.findViewById(R.id.textViewUserPhone);
            buttonLayout = itemView.findViewById(R.id.buttonLayout);
            buttonRent = itemView.findViewById(R.id.buttonRent);
            buttonReturn = itemView.findViewById(R.id.buttonReturn);
            buttonAvailable = itemView.findViewById(R.id.buttonAvailable);
        }
    }
}
