package com.example.chairman.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chairman.R;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WheelchairCountActivity extends AppCompatActivity {

    private TextView textViewAvailable, textViewRented, textViewBroken, textViewWaiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheelchair_count);

        // UI 요소 초기화
        textViewAvailable = findViewById(R.id.textViewAvailable);
        textViewRented = findViewById(R.id.textViewRented);
        textViewBroken = findViewById(R.id.textViewBroken);
        textViewWaiting = findViewById(R.id.textViewWaiting);

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        fetchWheelchairCounts(); // 데이터 가져오기
    }

    private void fetchWheelchairCounts() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Map<String, Integer>> call = apiService.getWheelchairCounts();

        call.enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Integer> counts = response.body();
                    textViewAvailable.setText("대여 가능: " + counts.getOrDefault("available", 0));
                    textViewRented.setText("대여 중: " + counts.getOrDefault("rented", 0));
                    textViewBroken.setText("고장: " + counts.getOrDefault("broken", 0));
                    textViewWaiting.setText("대기 중: " + counts.getOrDefault("waiting", 0));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                Toast.makeText(WheelchairCountActivity.this, "데이터 로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
