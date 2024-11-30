package com.example.chairman.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chairman.R;
import com.example.chairman.model.WaitingRentalResponse;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WaitingListAdapter adapter;
    private List<WaitingRentalResponse> rentalList;
    private TextView textViewInstitutionTitle;
    private Button buttonToStatus; // 휠체어 상태 보기 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_list);

        recyclerView = findViewById(R.id.recyclerViewWaitingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rentalList = new ArrayList<>();
        textViewInstitutionTitle = findViewById(R.id.textViewInstitutionName);
        buttonToStatus = findViewById(R.id.buttonToStatus); // 버튼 ID 연결

        adapter = new WaitingListAdapter(rentalList, new WaitingListAdapter.OnRentalActionListener() {
            @Override
            public void onApprove(WaitingRentalResponse rental) {
                approveRental(rental.getRentalId());
            }

            @Override
            public void onReject(WaitingRentalResponse rental) {
                rejectRental(rental.getRentalId());
            }
        });
        recyclerView.setAdapter(adapter);

        fetchWaitingList();

        // "휠체어 상태 보기" 버튼 클릭 이벤트
        buttonToStatus.setOnClickListener(v -> {
            Long institutionCode = getIntent().getLongExtra("institutionCode", -1);
            if (institutionCode == -1) {
                Toast.makeText(this, "공공기관 코드가 전달되지 않았습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // WheelchairStatusActivity로 이동
            Intent intent = new Intent(WaitingListActivity.this, WheelchairStatusActivity.class);
            intent.putExtra("institutionCode", institutionCode);
            startActivity(intent);
        });
    }

    private void fetchWaitingList() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        Long institutionCode = getIntent().getLongExtra("institutionCode", -1);
        Log.d("WaitingListActivity", "전달받은 institutionCode: " + institutionCode);

        if (institutionCode == -1) {
            Log.e("WaitingListActivity", "공공기관 코드가 전달되지 않았습니다.");
            Toast.makeText(this, "공공기관 코드가 필요합니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // API 호출
        apiService.getWaitingRentals(institutionCode).enqueue(new Callback<List<WaitingRentalResponse>>() {
            @Override
            public void onResponse(Call<List<WaitingRentalResponse>> call, Response<List<WaitingRentalResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rentalList.clear();
                    rentalList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (!rentalList.isEmpty() && rentalList.get(0).getInstitution() != null) {
                        textViewInstitutionTitle.setText(rentalList.get(0).getInstitution().getName());
                    } else {
                        textViewInstitutionTitle.setText("공공기관: 정보 없음");
                    }

                    Log.d("WaitingListActivity", "대기 목록 불러오기 성공: " + rentalList.size() + "건");
                } else {
                    Log.e("WaitingListActivity", "대기 목록 불러오기 실패: " + response.message());
                    Toast.makeText(WaitingListActivity.this, "대기 목록을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<WaitingRentalResponse>> call, Throwable t) {
                Log.e("WaitingListActivity", "API 호출 실패: " + t.getMessage());
                Toast.makeText(WaitingListActivity.this, "서버와의 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void approveRental(Long rentalId) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        Long institutionCode = getIntent().getLongExtra("institutionCode", -1);
        if (institutionCode == -1) {
            Toast.makeText(this, "공공기관 코드가 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.approveRental(institutionCode, rentalId).enqueue(new Callback<WaitingRentalResponse>() {
            @Override
            public void onResponse(Call<WaitingRentalResponse> call, Response<WaitingRentalResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(WaitingListActivity.this, "승인 완료!", Toast.LENGTH_SHORT).show();
                    fetchWaitingList(); // 목록 새로고침
                } else {
                    Toast.makeText(WaitingListActivity.this, "승인 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WaitingRentalResponse> call, Throwable t) {
                Toast.makeText(WaitingListActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rejectRental(Long rentalId) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        Long institutionCode = getIntent().getLongExtra("institutionCode", -1);
        if (institutionCode == -1) {
            Toast.makeText(this, "공공기관 코드가 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.rejectRental(institutionCode, rentalId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(WaitingListActivity.this, "거절 완료!", Toast.LENGTH_SHORT).show();
                    fetchWaitingList(); // 목록 새로고침
                } else {
                    Toast.makeText(WaitingListActivity.this, "거절 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(WaitingListActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
