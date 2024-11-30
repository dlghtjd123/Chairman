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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheelchair_status);

        // View 초기화
        buttonAvailable = findViewById(R.id.buttonAvailable);
        buttonRented = findViewById(R.id.buttonRented);
        buttonBroken = findViewById(R.id.buttonBroken);
        buttonWaiting = findViewById(R.id.buttonWaiting);
        buttonTotal = findViewById(R.id.buttonTotal);
        buttonAccepted = findViewById(R.id.buttonAccepted);
        recyclerViewDetails = findViewById(R.id.recyclerViewWheelchairDetails);

        // RecyclerView 및 Adapter 설정
        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this));
        wheelchairDetailsAdapter = new WheelchairDetailsAdapter(wheelchairDetailsList);
        recyclerViewDetails.setAdapter(wheelchairDetailsAdapter);

        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // 전달받은 institutionCode 가져오기
        institutionCode = getIntent().getLongExtra("institutionCode", -1);
        if (institutionCode == -1) {
            Toast.makeText(this, "기관 코드가 필요합니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 상태별 개수 갱신
        fetchStatusCounts(institutionCode);

        // 버튼 클릭 리스너 설정
        buttonTotal.setOnClickListener(v -> {
            resetButtonColors();
            buttonTotal.setBackgroundColor(Color.GRAY);
            fetchWheelchairDetails("ALL", institutionCode);
        });

        buttonAvailable.setOnClickListener(v -> {
            resetButtonColors();
            buttonAvailable.setBackgroundColor(Color.GRAY);
            fetchWheelchairDetails("AVAILABLE", institutionCode);
        });

        buttonRented.setOnClickListener(v -> {
            resetButtonColors();
            buttonRented.setBackgroundColor(Color.GRAY);
            fetchWheelchairDetails("RENTED", institutionCode);
        });

        buttonBroken.setOnClickListener(v -> {
            resetButtonColors();
            buttonBroken.setBackgroundColor(Color.GRAY);
            fetchWheelchairDetails("BROKEN", institutionCode);
        });

        buttonWaiting.setOnClickListener(v -> {
            resetButtonColors();
            buttonWaiting.setBackgroundColor(Color.GRAY);
            fetchWheelchairDetails("WAITING", institutionCode);
        });

        buttonAccepted.setOnClickListener(v -> {
            resetButtonColors();
            buttonAccepted.setBackgroundColor(Color.GRAY);
            fetchWheelchairDetails("ACCEPTED", institutionCode);
        });

    }

    private void fetchStatusCounts(Long institutionCode) {
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

    private void fetchAllWheelchairDetails(Long institutionCode) {
        apiService.getWheelchairDetails(institutionCode, "ALL").enqueue(new Callback<List<WheelchairDetailResponse>>() {
            @Override
            public void onResponse(Call<List<WheelchairDetailResponse>> call, Response<List<WheelchairDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<WheelchairDetailResponse> details = response.body();

                    // RecyclerView 데이터 업데이트
                    wheelchairDetailsList.clear();
                    wheelchairDetailsList.addAll(details);
                    wheelchairDetailsAdapter.notifyDataSetChanged();
                    Log.d("WheelchairStatus", "Fetched all details: " + details.size());
                } else {
                    Toast.makeText(WheelchairStatusActivity.this, "총합 데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<WheelchairDetailResponse>> call, Throwable t) {
                Toast.makeText(WheelchairStatusActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("WheelchairStatus", "Error fetching all details", t);
            }
        });
    }

    private void fetchWheelchairDetails(String status, Long institutionCode) {
        apiService.getWheelchairDetails(institutionCode, status).enqueue(new Callback<List<WheelchairDetailResponse>>() {
            @Override
            public void onResponse(Call<List<WheelchairDetailResponse>> call, Response<List<WheelchairDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<WheelchairDetailResponse> details = response.body();

                    // RecyclerView 데이터 업데이트
                    wheelchairDetailsList.clear();
                    wheelchairDetailsList.addAll(details);
                    wheelchairDetailsAdapter.notifyDataSetChanged();
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
}
