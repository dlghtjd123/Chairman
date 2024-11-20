package com.example.chairman.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chairman.R;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WheelchairListActivity extends AppCompatActivity {

    private Button adultButton;
    private Button childButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheelchair_list);

        // 버튼 초기화
        adultButton = findViewById(R.id.adult_button);
        childButton = findViewById(R.id.child_button);

        // Intent로부터 institutionCode 가져오기
        Long institutionCode = getIntent().getLongExtra("institutionCode", -1L);

        if (institutionCode == -1L) {
            Log.e("WheelchairListActivity", "공공기관 코드가 전달되지 않았습니다.");
            Toast.makeText(this, "기관 선택이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // institutionCode 로그로 확인
        Log.d("WheelchairListActivity", "Received institutionCode: " + institutionCode);

        // API 호출로 데이터 가져오기
        fetchAvailableCounts(institutionCode);

        // 성인용 버튼 클릭 시 RentalActivity로 이동
        adultButton.setOnClickListener(v -> openRentalActivity(institutionCode, "adult"));

        // 유아용 버튼 클릭 시 RentalActivity로 이동
        childButton.setOnClickListener(v -> openRentalActivity(institutionCode, "child"));
    }

    /**
     * 선택한 공공기관의 대여 가능한 휠체어 수를 가져오는 함수
     *
     * @param institutionCode 공공기관 코드
     */
    private void fetchAvailableCounts(Long institutionCode) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Map<String, Integer>> call = apiService.getAvailableWheelchairCounts(institutionCode);

        call.enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Integer> counts = response.body();

                    // 버튼 텍스트 업데이트
                    adultButton.setText("성인용 대여 가능:\n" + counts.getOrDefault("ADULT", 0));
                    childButton.setText("유아용 대여 가능:\n" + counts.getOrDefault("CHILD", 0));
                } else {
                    Toast.makeText(WheelchairListActivity.this, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("WheelchairListActivity", "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(WheelchairListActivity.this, "서버 통신 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("WheelchairListActivity", "API call failed", t);
            }
        });
    }

    /**
     * RentalActivity로 이동하는 함수
     *
     * @param institutionCode 공공기관 코드
     * @param wheelchairType  휠체어 타입 (adult or child)
     */
    private void openRentalActivity(Long institutionCode, String wheelchairType) {
        Intent intent = new Intent(this, RentalActivity.class);
        intent.putExtra("institutionCode", institutionCode);
        intent.putExtra("wheelchairType", wheelchairType);
        startActivity(intent);
    }
}
