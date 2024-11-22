package com.example.chairman.user;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chairman.R;
import com.example.chairman.model.RentalRequest;
import com.example.chairman.model.RentalResponse;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;
import com.google.android.material.timepicker.MaterialTimePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Calendar;

public class RentalActivity extends AppCompatActivity {

    private Button rentDateButton, returnDateButton, rentButton;
    private String selectedRentDate = null;  // 대여일
    private String selectedReturnDate = null;  // 반납일

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental);

        // SharedPreferences에서 JWT 토큰 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if (jwtToken == null) {
            Log.e("RentalActivity", "SharedPreferences에서 JWT 토큰을 찾을 수 없습니다.");
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            Log.d("RentalActivity", "불러온 JWT 토큰: " + jwtToken);
        }



        // Intent 데이터 가져오기
        Long institutionCode = getIntent().getLongExtra("institutionCode", -1L);
        String wheelchairType = getIntent().getStringExtra("wheelchairType");

        if (institutionCode == -1L || wheelchairType == null) {
            Toast.makeText(this, "필요한 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 버튼 초기화
        rentDateButton = findViewById(R.id.rent_date_button);
        returnDateButton = findViewById(R.id.return_date_button);
        rentButton = findViewById(R.id.rent_button);

        rentDateButton.setOnClickListener(v -> showDateTimePicker(true));
        returnDateButton.setOnClickListener(v -> showDateTimePicker(false));
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());


        // 대여하기 버튼 클릭 이벤트
        rentButton.setOnClickListener(v -> {
            if (selectedRentDate == null || selectedReturnDate == null) {
                Toast.makeText(this, "대여일과 반납일을 모두 선택해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 대여 요청 데이터 생성
            RentalRequest rentalRequest = new RentalRequest();
            rentalRequest.setInstitutionCode(institutionCode);
            rentalRequest.setWheelchairType(wheelchairType.toUpperCase());
            rentalRequest.setRentalDate(selectedRentDate);
            rentalRequest.setReturnDate(selectedReturnDate);

            // 로그로 확인
            Log.d("RentalActivity", "Request Body: " + rentalRequest);

            // 서버로 요청 전송
            ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
            Call<RentalResponse> call = apiService.rentWheelchair("Bearer " + jwtToken, institutionCode, rentalRequest);

            call.enqueue(new Callback<RentalResponse>() {
                @Override
                public void onResponse(Call<RentalResponse> call, Response<RentalResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        RentalResponse rentalResponse = response.body();
                        Toast.makeText(RentalActivity.this,
                                "대여 성공!\n대여 코드: " + rentalResponse.getRentalCode() +
                                        "\n상태: " + rentalResponse.getStatus(),
                                Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Log.e("RentalActivity", "대여 요청 실패 - 코드: " + response.code() + ", 메시지: " + response.message());
                        Log.e("RentalActivity", "에러 내용: " + response.errorBody());
                        Toast.makeText(RentalActivity.this, "이미 대여중입니다." + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RentalResponse> call, Throwable t) {
                    Toast.makeText(RentalActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private void showDateTimePicker(boolean isRentDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 날짜 선택
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (dateView, selectedYear, selectedMonth, selectedDay) -> {
            // 시간 선택
            MaterialTimePicker.Builder timePickerBuilder = new MaterialTimePicker.Builder()
                    .setTimeFormat(android.text.format.DateFormat.is24HourFormat(this)
                            ? com.google.android.material.timepicker.TimeFormat.CLOCK_24H
                            : com.google.android.material.timepicker.TimeFormat.CLOCK_12H)
                    .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                    .setMinute(calendar.get(Calendar.MINUTE))
                    .setTitleText("시간 선택");

            MaterialTimePicker timePicker = timePickerBuilder.build();

            timePicker.addOnPositiveButtonClickListener(view -> {
                int selectedHour = timePicker.getHour();
                int selectedMinute = timePicker.getMinute();

                // 날짜와 시간 결합하여 형식 지정
                String formattedDateTime = formatDateTime(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);

                if (isRentDate) {
                    selectedRentDate = formattedDateTime;
                    rentDateButton.setText("대여일: " + formattedDateTime.replace("T", " "));
                    Toast.makeText(this, "대여일 선택: " + formattedDateTime, Toast.LENGTH_SHORT).show();

                    // 대여일 선택 후 반납일 초기화
                    selectedReturnDate = null;
                    returnDateButton.setText("반납일 선택");
                } else {
                    if (selectedRentDate != null && !isReturnDateValid(selectedRentDate, formattedDateTime)) {
                        Toast.makeText(this, "반납일은 대여일 이후여야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    selectedReturnDate = formattedDateTime;
                    returnDateButton.setText("반납일: " + formattedDateTime.replace("T", " "));
                    Toast.makeText(this, "반납일 선택: " + formattedDateTime, Toast.LENGTH_SHORT).show();
                }
            });

            timePicker.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
        }, year, month, day);

        // 최소 날짜 설정
        if (isRentDate) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        } else {
            if (selectedRentDate != null) {
                Calendar rentDate = Calendar.getInstance();
                String[] rentDateParts = selectedRentDate.split("T")[0].split("-");
                rentDate.set(Calendar.YEAR, Integer.parseInt(rentDateParts[0]));
                rentDate.set(Calendar.MONTH, Integer.parseInt(rentDateParts[1]) - 1);
                rentDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(rentDateParts[2]));
                datePickerDialog.getDatePicker().setMinDate(rentDate.getTimeInMillis());
            }
        }

        // 최대 날짜 설정 (2주 후)
        calendar.add(Calendar.WEEK_OF_YEAR, 2);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    /**
     * 날짜와 시간을 "yyyy-MM-dd'T'HH:mm:ss" 형식으로 포맷팅
     */
    private String formatDateTime(int year, int month, int day, int hour, int minute) {
        return String.format("%04d-%02d-%02dT%02d:%02d:00", year, month + 1, day, hour, minute);
    }

    /**
     * 반납일이 대여일 이후인지 검증
     */
    private boolean isReturnDateValid(String rentDate, String returnDate) {
        return returnDate.compareTo(rentDate) >= 0;
    }



}
