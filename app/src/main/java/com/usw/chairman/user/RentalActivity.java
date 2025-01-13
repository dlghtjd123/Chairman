package com.usw.chairman.user;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.usw.chairman.R;
import com.usw.chairman.model.RentalRequest;
import com.usw.chairman.model.WaitingRentalResponse;
import com.usw.chairman.network.ApiClient;
import com.usw.chairman.network.ApiService;

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
            Call<WaitingRentalResponse> call = apiService.rentWheelchair("Bearer " + jwtToken, institutionCode, rentalRequest);

            call.enqueue(new Callback<WaitingRentalResponse>() {
                @Override
                public void onResponse(Call<WaitingRentalResponse> call, Response<WaitingRentalResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        WaitingRentalResponse rentalResponse = response.body();
                        Toast.makeText(RentalActivity.this,
                                "대여 성공!\n대여 코드: " + rentalResponse.getRentalCode() +
                                        "\n상태: " + rentalResponse.getStatus(),
                                Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Log.e("RentalActivity", "대여 요청 실패 - 코드: " + response.code() + ", 메시지: " + response.message());
                        Toast.makeText(RentalActivity.this, "이미 대여중입니다." + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<WaitingRentalResponse> call, Throwable t) {
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
            // Custom Time Picker Dialog (AM/PM 형식 + Rounded)
            Dialog timePickerDialog = new Dialog(this);
            timePickerDialog.setContentView(R.layout.dialog_custom_time_picker); // Rounded 및 Custom Layout 사용
            timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // 투명 배경

            // 시간 및 분 선택기 초기화
            NumberPicker hourPicker = timePickerDialog.findViewById(R.id.hour_picker);
            NumberPicker minutePicker = timePickerDialog.findViewById(R.id.minute_picker);
            NumberPicker amPmPicker = timePickerDialog.findViewById(R.id.am_pm_picker);

            // 시간 설정 (1~12)
            hourPicker.setMinValue(1);
            hourPicker.setMaxValue(12);
            hourPicker.setWrapSelectorWheel(true);

            // 분 설정 (5분 단위)
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(11);
            minutePicker.setDisplayedValues(new String[]{
                    "00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"
            });
            minutePicker.setWrapSelectorWheel(true);

            // AM/PM 설정
            amPmPicker.setMinValue(0);
            amPmPicker.setMaxValue(1);
            amPmPicker.setDisplayedValues(new String[]{"AM", "PM"});
            amPmPicker.setWrapSelectorWheel(true);

            // 확인 버튼
            timePickerDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                int selectedHour = hourPicker.getValue();
                int selectedMinute = minutePicker.getValue() * 5;
                boolean isPM = (amPmPicker.getValue() == 1);

                // 24시간 형식으로 변환
                if (isPM && selectedHour != 12) {
                    selectedHour += 12;
                } else if (!isPM && selectedHour == 12) {
                    selectedHour = 0;
                }

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
                        Toast.makeText(this, "반납일은 대여일 이후 시간이 되어야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    selectedReturnDate = formattedDateTime;
                    returnDateButton.setText("반납일: " + formattedDateTime.replace("T", " "));
                    Toast.makeText(this, "반납일 선택: " + formattedDateTime, Toast.LENGTH_SHORT).show();
                }

                timePickerDialog.dismiss();
            });

            // 취소 버튼
            timePickerDialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> timePickerDialog.dismiss());

            timePickerDialog.show();
        }, year, month, day);

        // 대여일 및 반납일 조건 설정
        if (isRentDate) {
            // 대여일: 오늘 이후 날짜만 선택 가능
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        } else {
            // 반납일: 대여일과 같은 날짜거나 이후로만 설정 가능
            if (selectedRentDate != null) {
                Calendar rentDate = Calendar.getInstance();
                String[] rentDateParts = selectedRentDate.split("T")[0].split("-");
                rentDate.set(Calendar.YEAR, Integer.parseInt(rentDateParts[0]));
                rentDate.set(Calendar.MONTH, Integer.parseInt(rentDateParts[1]) - 1);
                rentDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(rentDateParts[2]));

                // 반납일 최소 날짜: 대여일
                datePickerDialog.getDatePicker().setMinDate(rentDate.getTimeInMillis());

                // 반납일 최대 날짜: 대여일 기준 최대 2주까지
                rentDate.add(Calendar.WEEK_OF_YEAR, 2);
                datePickerDialog.getDatePicker().setMaxDate(rentDate.getTimeInMillis());
            } else {
                // 대여일이 선택되지 않은 경우
                Toast.makeText(this, "먼저 대여일을 선택해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

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
