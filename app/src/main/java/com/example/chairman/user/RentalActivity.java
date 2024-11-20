package com.example.chairman.user;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chairman.R;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentalActivity extends AppCompatActivity {

    private Button selectDateButton, rentButton;
    private ImageView wheelchairImage;
    private Set<String> availableDates = new HashSet<>(); // 대여 가능한 날짜
    private String selectedDate; // 선택한 날짜를 저장

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental);

        selectDateButton = findViewById(R.id.select_date_button);
        rentButton = findViewById(R.id.rent_button);
        wheelchairImage = findViewById(R.id.wheelchair_image);

        // 기관 코드와 휠체어 타입을 이전 액티비티에서 전달받음
        Long institutionCode = getIntent().getLongExtra("institutionCode", -1L);
        String wheelchairType = getIntent().getStringExtra("wheelchairType");

        if (institutionCode == -1L || wheelchairType == null) {
            Toast.makeText(this, "기관 정보가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 대여 가능한 날짜 API 호출
        fetchAvailableDates(institutionCode, wheelchairType);

        // 달력 버튼 클릭 시 달력 표시
        selectDateButton.setOnClickListener(v -> showDatePicker());

        // 대여하기 버튼 클릭 시 대여 요청
        rentButton.setOnClickListener(v -> {
            if (selectedDate == null) {
                Toast.makeText(this, "대여 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                rentWheelchair(institutionCode, wheelchairType, selectedDate);
            }
        });
    }

    private void fetchAvailableDates(Long institutionCode, String wheelchairType) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<String>> call = apiService.getAvailableDates(institutionCode, wheelchairType);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    availableDates.addAll(response.body()); // 서버에서 받아온 날짜 저장
                } else {
                    Toast.makeText(RentalActivity.this, "대여 가능한 날짜를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(RentalActivity.this, "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String formattedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            if (availableDates.contains(formattedDate)) {
                selectedDate = formattedDate;
                Toast.makeText(this, "선택된 날짜: " + formattedDate, Toast.LENGTH_SHORT).show();
            } else {
                selectedDate = null;
                Toast.makeText(this, "선택한 날짜는 대여가 불가능합니다.", Toast.LENGTH_SHORT).show();
            }
        }, year, month, day);

        // 최소 날짜를 현재 날짜로 설정
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        // 최대 날짜는 1개월 후로 설정
        calendar.add(Calendar.MONTH, 1);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    private void rentWheelchair(Long institutionCode, String wheelchairType, String rentalDate) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.rentWheelchair(institutionCode, wheelchairType, rentalDate);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RentalActivity.this, "대여가 성공적으로 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish(); // 대여 완료 후 액티비티 종료
                } else {
                    Toast.makeText(RentalActivity.this, "대여에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RentalActivity.this, "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
