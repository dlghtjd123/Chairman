package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Wheelchair;
import com.example.myapplication.WheelchairAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminRentalsFragment extends Fragment {

    private RecyclerView recyclerView;
    private WheelchairAdapter adapter;
    private List<Wheelchair> wheelchairList;
    private LinearLayout totalButton, availableButton, brokenButton, inUseButton;
    private LinearLayout searchSection;
    private EditText searchInput;
    private ImageView searchIcon;
    private LinearLayout selectedButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_rentals, container, false);

        // 휠체어 데이터를 추가
        initializeWheelchairs();

        // RecyclerView 설정
        recyclerView = view.findViewById(R.id.wheelchair);
        adapter = new WheelchairAdapter(new ArrayList<>(wheelchairList));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // 필터 버튼 참조
        totalButton = view.findViewById(R.id.totalButton);
        availableButton = view.findViewById(R.id.availableButton);
        brokenButton = view.findViewById(R.id.brokenButton);
        inUseButton = view.findViewById(R.id.inUseButton);

        // 검색창 섹션 참조
        searchSection = view.findViewById(R.id.searchSection);
        searchInput = view.findViewById(R.id.searchInput);
        searchIcon = view.findViewById(R.id.searchIcon);

        // 필터 버튼 클릭 리스너 설정 및 버튼 선택 상태 처리
        totalButton.setOnClickListener(v -> handleButtonClick(totalButton, "total"));
        availableButton.setOnClickListener(v -> handleButtonClick(availableButton, "available"));
        brokenButton.setOnClickListener(v -> handleButtonClick(brokenButton, "broken"));
        inUseButton.setOnClickListener(v -> handleButtonClick(inUseButton, "inUse"));

        return view;
    }

    // 휠체어 목록 초기화
    private void initializeWheelchairs() {
        wheelchairList = new ArrayList<>();
        wheelchairList.add(new Wheelchair("WC001", "사용 가능"));
        wheelchairList.add(new Wheelchair("WC002", "파손"));
        wheelchairList.add(new Wheelchair("WC003", "대여 중"));
        wheelchairList.add(new Wheelchair("WC004", "사용 가능"));
        wheelchairList.add(new Wheelchair("WC006", "파손"));
        wheelchairList.add(new Wheelchair("WC007", "대여 중"));
        wheelchairList.add(new Wheelchair("WC008", "대여 중"));
    }

    // 버튼 클릭 시 처리
    private void handleButtonClick(LinearLayout button, String filter) {
        if (selectedButton != null) {
            selectedButton.setBackgroundColor(getResources().getColor(R.color.gray_light));  // 선택 해제된 버튼은 밝은 회색
            selectedButton.setSelected(false);  // 이전에 선택된 버튼 해제
        }
        button.setBackgroundColor(getResources().getColor(R.color.gray_dark));  // 선택된 버튼은 어두운 회색
        button.setSelected(true);  // 새로운 버튼 선택
        selectedButton = button;  // 현재 선택된 버튼 업데이트

        filterWheelchairs(filter);  // 선택된 필터에 따라 휠체어 목록 갱신
    }

    // 휠체어 목록 필터링
    private void filterWheelchairs(String filter) {
        List<Wheelchair> filteredList = new ArrayList<>();
        for (Wheelchair wc : wheelchairList) {
            switch (filter) {
                case "total":
                    filteredList.add(wc);  // 모든 항목 추가
                    break;
                case "available":
                    if ("사용 가능".equals(wc.getStatus())) {
                        filteredList.add(wc);
                    }
                    break;
                case "broken":
                    if ("파손".equals(wc.getStatus())) {
                        filteredList.add(wc);
                    }
                    break;
                case "inUse":
                    if ("대여 중".equals(wc.getStatus())) {
                        filteredList.add(wc);
                    }
                    break;
            }
        }
        adapter.updateList(filteredList);  // 어댑터에 새 목록 적용
    }
}
