package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdminReservationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_reservation, container, false); // fragment_admin_reservation.xml 사용

        // 예약 수락 버튼 클릭 리스너
        Button acceptButton = view.findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 예약 수락 처리
                Toast.makeText(getActivity(), "예약이 수락되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 예약 취소 버튼 클릭 리스너
        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 예약 취소 처리
                Toast.makeText(getActivity(), "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
