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
        holder.textViewStatus.setText("휠체어 상태: " + wheelchair.getWheelchairStatus());
        holder.textViewType.setText("타입: " + wheelchair.getType());

        // 대여 정보 숨기기 또는 표시하기
        boolean shouldHideRentalInfo = "BROKEN".equalsIgnoreCase(wheelchair.getWheelchairStatus()) ||
                "AVAILABLE".equalsIgnoreCase(wheelchair.getWheelchairStatus());

        if (shouldHideRentalInfo) {
            // 대여 정보 숨기기
            holder.textViewRentalStatus.setVisibility(View.GONE);
            holder.textViewUserName.setVisibility(View.GONE);
            holder.textViewUserPhone.setVisibility(View.GONE);
        } else {
            // 대여 정보 표시
            holder.textViewRentalStatus.setVisibility(View.VISIBLE);
            holder.textViewRentalStatus.setText("대여 상태: " + (wheelchair.getRentalStatus() != null ? wheelchair.getRentalStatus() : "ERROR"));
            holder.textViewUserName.setVisibility(View.VISIBLE);
            holder.textViewUserName.setText("대여인: " + (wheelchair.getUserName() != null ? wheelchair.getUserName() : "정보 없음"));
            holder.textViewUserPhone.setVisibility(View.VISIBLE);
            holder.textViewUserPhone.setText("전화번호: " + (wheelchair.getUserPhone() != null ? wheelchair.getUserPhone() : "정보 없음"));
        }

        // 버튼 레이아웃 가시성 제어
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            holder.buttonLayout.setVisibility(View.VISIBLE);

            // 상태에 따른 버튼 가시성 설정
            switch (wheelchair.getWheelchairStatus().toUpperCase()) {
                case "ACCEPTED":
                    holder.buttonRent.setVisibility(View.VISIBLE);
                    holder.buttonReturn.setVisibility(View.GONE);
                    holder.buttonAvailable.setVisibility(View.GONE);
                    break;
                case "RENTED":
                    holder.buttonRent.setVisibility(View.GONE);
                    holder.buttonReturn.setVisibility(View.VISIBLE);
                    holder.buttonAvailable.setVisibility(View.GONE);
                    break;
                case "BROKEN":
                    holder.buttonRent.setVisibility(View.GONE);
                    holder.buttonReturn.setVisibility(View.GONE);
                    holder.buttonAvailable.setVisibility(View.VISIBLE);
                    break;
                default:
                    holder.buttonRent.setVisibility(View.GONE);
                    holder.buttonReturn.setVisibility(View.GONE);
                    holder.buttonAvailable.setVisibility(View.GONE);
                    break;
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
        TextView textViewWheelchairId, textViewStatus, textViewType, textViewRentalStatus, textViewUserName, textViewUserPhone;
        LinearLayout buttonLayout;
        Button buttonRent, buttonReturn, buttonAvailable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWheelchairId = itemView.findViewById(R.id.textViewWheelchairId);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewRentalStatus = itemView.findViewById(R.id.textViewRentalStatus);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserPhone = itemView.findViewById(R.id.textViewUserPhone);
            buttonLayout = itemView.findViewById(R.id.buttonLayout);
            buttonRent = itemView.findViewById(R.id.buttonRent);
            buttonReturn = itemView.findViewById(R.id.buttonReturn);
            buttonAvailable = itemView.findViewById(R.id.buttonAvailable);
        }
    }
}
