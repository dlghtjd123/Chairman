package com.example.chairman.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chairman.R;
import com.example.chairman.model.WheelchairDetailResponse;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WheelchairStatusActivity extends AppCompatActivity {

    private Button buttonTotal, buttonAvailable, buttonRented, buttonBroken, buttonWaiting, buttonAccepted;
    private RecyclerView recyclerViewDetails;
    private WheelchairDetailsAdapter wheelchairDetailsAdapter;
    private List<WheelchairDetailResponse> wheelchairDetailsList = new ArrayList<>();
    private ApiService apiService;
    private Long institutionCode;
    private String selectedStatus = "ALL"; // 현재 선택된 상태

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheelchair_status);

        // View 초기화
        initializeViews();

        // RecyclerView 초기화
        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this));
        wheelchairDetailsAdapter = new WheelchairDetailsAdapter(wheelchairDetailsList, new WheelchairDetailsAdapter.OnActionListener() {
            @Override
            public void onRent(WheelchairDetailResponse wheelchair) {
                updateWheelchairStatusAndRentalStatus(wheelchair.getWheelchairId(), "RENTED", "ACTIVE");
            }

            @Override
            public void onReturn(WheelchairDetailResponse wheelchair) {
                updateWheelchairStatusAndRentalStatus(wheelchair.getWheelchairId(), "AVAILABLE", "NORMAL");
            }

            @Override
            public void onAvailable(WheelchairDetailResponse wheelchair) {
                updateWheelchairStatus(wheelchair.getWheelchairId(), "AVAILABLE");
            }

            @Override
            public void onBroken(WheelchairDetailResponse wheelchair) {
                updateWheelchairStatus(wheelchair.getWheelchairId(), "BROKEN");
            }
        });
        recyclerViewDetails.setAdapter(wheelchairDetailsAdapter);

        // Institution Code 확인
        institutionCode = getIntent().getLongExtra("institutionCode", -1);
        if (institutionCode == -1) {
            Toast.makeText(this, "기관 코드가 필요합니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 초기 데이터 로드
        fetchStatusCounts();
        fetchWheelchairDetails();

        // 상태 버튼 클릭 이벤트 설정
        buttonTotal.setOnClickListener(v -> onStatusButtonClick("ALL", buttonTotal));
        buttonAvailable.setOnClickListener(v -> onStatusButtonClick("AVAILABLE", buttonAvailable));
        buttonRented.setOnClickListener(v -> onStatusButtonClick("RENTED", buttonRented));
        buttonBroken.setOnClickListener(v -> onStatusButtonClick("BROKEN", buttonBroken));
        buttonWaiting.setOnClickListener(v -> onStatusButtonClick("WAITING", buttonWaiting));
        buttonAccepted.setOnClickListener(v -> onStatusButtonClick("ACCEPTED", buttonAccepted));
    }

    // View 초기화 메서드
    private void initializeViews() {
        // XML 레이아웃에 정의된 버튼 및 RecyclerView를 초기화
        buttonTotal = findViewById(R.id.buttonTotal);
        buttonAvailable = findViewById(R.id.buttonAvailable);
        buttonRented = findViewById(R.id.buttonRented);
        buttonBroken = findViewById(R.id.buttonBroken);
        buttonWaiting = findViewById(R.id.buttonWaiting);
        buttonAccepted = findViewById(R.id.buttonAccepted);
        recyclerViewDetails = findViewById(R.id.recyclerViewWheelchairDetails);

        // API 클라이언트 초기화
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    }

    private void onStatusButtonClick(String status, Button selectedButton) {
        resetButtonColors();
        selectedButton.setBackgroundColor(Color.GRAY);
        selectedStatus = status; // 선택된 상태 업데이트
        fetchWheelchairDetails();
    }

    private void fetchStatusCounts() {
        apiService.getStatusCounts(institutionCode).enqueue(new Callback<Map<String, Long>>() {
            @Override
            public void onResponse(Call<Map<String, Long>> call, Response<Map<String, Long>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Long> statusCounts = response.body();
                    buttonAvailable.setText("사용 가능: " + statusCounts.getOrDefault("AVAILABLE", 0L));
                    buttonRented.setText("대여 중: " + statusCounts.getOrDefault("RENTED", 0L));
                    buttonBroken.setText("수리 중: " + statusCounts.getOrDefault("BROKEN", 0L));
                    buttonWaiting.setText("예약 대기: " + statusCounts.getOrDefault("WAITING", 0L));
                    buttonAccepted.setText("대여 대기: " + statusCounts.getOrDefault("ACCEPTED", 0L));
                    buttonTotal.setText("총합: " + statusCounts.values().stream().mapToLong(Long::longValue).sum());
                } else {
                    Toast.makeText(WheelchairStatusActivity.this, "상태 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Long>> call, Throwable t) {
                Toast.makeText(WheelchairStatusActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("WheelchairStatus", "Error fetching status counts", t);
            }
        });
    }

    private void resetButtonColors() {
        buttonTotal.setBackgroundColor(Color.WHITE);
        buttonAvailable.setBackgroundColor(Color.WHITE);
        buttonRented.setBackgroundColor(Color.WHITE);
        buttonBroken.setBackgroundColor(Color.WHITE);
        buttonWaiting.setBackgroundColor(Color.WHITE);
        buttonAccepted.setBackgroundColor(Color.WHITE);
    }

    private void fetchWheelchairDetails() {
        apiService.getWheelchairDetails(institutionCode, selectedStatus).enqueue(new Callback<List<WheelchairDetailResponse>>() {
            @Override
            public void onResponse(Call<List<WheelchairDetailResponse>> call, Response<List<WheelchairDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<WheelchairDetailResponse> details = response.body();
                    wheelchairDetailsList.clear();
                    wheelchairDetailsList.addAll(details);
                    wheelchairDetailsAdapter.notifyDataSetChanged(); // 데이터 갱신
                    Log.d("WheelchairStatus", "Fetched details: " + details.size());
                } else {
                    Toast.makeText(WheelchairStatusActivity.this, "세부 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<WheelchairDetailResponse>> call, Throwable t) {
                Toast.makeText(WheelchairStatusActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("WheelchairStatus", "Error fetching details", t);
            }
        });
    }


    private void updateWheelchairStatusAndRentalStatus(Long wheelchairId, String wheelchairStatus, String rentalStatus) {
        apiService.updateWheelchairAndRentalStatus(wheelchairId, wheelchairStatus, rentalStatus).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(WheelchairStatusActivity.this, "상태가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    fetchStatusCounts(); // 상태 버튼 업데이트
                    fetchWheelchairDetails(); // RecyclerView 데이터 갱신
                } else {
                    Toast.makeText(WheelchairStatusActivity.this, "상태 변경 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(WheelchairStatusActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void updateWheelchairStatus(Long wheelchairId, String wheelchairStatus) {
        apiService.updateWheelchairStatus(wheelchairId, wheelchairStatus).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(WheelchairStatusActivity.this, "휠체어가 " + wheelchairStatus + " 상태로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    fetchStatusCounts(); // 상태 버튼 업데이트
                    fetchWheelchairDetails(); // RecyclerView 데이터 갱신
                } else {
                    Toast.makeText(WheelchairStatusActivity.this, "상태 변경 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(WheelchairStatusActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}