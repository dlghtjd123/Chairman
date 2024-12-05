package com.example.chairman.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chairman.R;
import com.example.chairman.model.UserRentalResponse;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentalInfoActivity extends AppCompatActivity {

    private TextView textViewRentalPeriod, textViewInstitutionName,
            textViewInstitutionAddress, textViewInstitutionPhone, textViewWheelchairType, textViewCurrentStatus;

    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_info);

        // UI 요소 초기화
        textViewRentalPeriod = findViewById(R.id.textViewRentalPeriod);
        textViewInstitutionName = findViewById(R.id.textViewInstitutionName);
        textViewInstitutionAddress = findViewById(R.id.textViewInstitutionAddress);
        textViewInstitutionPhone = findViewById(R.id.textViewInstitutionPhone);
        textViewWheelchairType = findViewById(R.id.textViewWheelchairType);
        textViewCurrentStatus = findViewById(R.id.textViewCurrentStatus);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setEnabled(false); // 초기에는 비활성화
        cancelButton.setOnClickListener(v -> {
            Long institutionCode = getIntent().getLongExtra("institutionCode", -1L);
            if (institutionCode != -1L) {
                cancelRental(institutionCode);
            } else {
                Toast.makeText(this, "기관 코드가 누락되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // SharedPreferences에서 JWT 토큰 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if (jwtToken != null) {
            fetchRentalDetails(jwtToken);
        } else {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchRentalDetails(String jwtToken) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        Call<UserRentalResponse> rentalCall = apiService.getRentalInfo("Bearer " + jwtToken);
        rentalCall.enqueue(new Callback<UserRentalResponse>() {
            @Override
            public void onResponse(Call<UserRentalResponse> call, Response<UserRentalResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserRentalResponse rental = response.body();

                    // 대여 기간 설정
                    textViewRentalPeriod.setText(rental.getRentalDate() + " - " + rental.getReturnDate());

                    // 휠체어 타입 설정
                    textViewWheelchairType.setText(rental.getWheelchairType() != null
                            ? rental.getWheelchairType()
                            : "정보 없음");

                    // 공공기관 정보 설정
                    textViewInstitutionName.setText(rental.getInstitutionName() != null
                            ? rental.getInstitutionName()
                            : "정보 없음");
                    textViewInstitutionAddress.setText(rental.getInstitutionAddress() != null
                            ? rental.getInstitutionAddress()
                            : "정보 없음");
                    textViewInstitutionPhone.setText(rental.getInstitutionPhone() != null
                            ? rental.getInstitutionPhone()
                            : "정보 없음");

                    // 대여 상태 처리
                    String rentalStatus = rental.getStatus();
                    String translatedStatus;
                    switch (rentalStatus) {
                        case "WAITING":
                            translatedStatus = "대여 대기";
                            cancelButton.setEnabled(false); // 대여 대기 상태에서는 취소 불가능
                            break;
                        case "ACCEPTED":
                            translatedStatus = "대여 수락";
                            cancelButton.setEnabled(true); // 대여 수락 상태에서는 취소 가능
                            break;
                        case "ACTIVE":
                            translatedStatus = "대여 중";
                            cancelButton.setEnabled(true); // 대여 중 상태에서도 취소 가능
                            break;
                        default:
                            translatedStatus = "알 수 없음";
                            cancelButton.setEnabled(false); // 알 수 없는 상태에서는 취소 불가능
                            break;
                    }
                    textViewCurrentStatus.setText(translatedStatus);

                    // 공공기관 코드 가져오기
                    Long institutionCode = rental.getInstitutionCode();
                    Log.d("RentalInfoActivity", "Institution Code: " + institutionCode);

                    // Intent로 공공기관 코드 전달
                    getIntent().putExtra("institutionCode", institutionCode);
                } else {
                    Toast.makeText(RentalInfoActivity.this, "대여 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserRentalResponse> call, Throwable t) {
                Toast.makeText(RentalInfoActivity.this, "네트워크 오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelRental(Long institutionCode) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if (jwtToken == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.cancelRental(institutionCode, "Bearer " + jwtToken);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RentalInfoActivity.this, "대여가 성공적으로 취소되었습니다.", Toast.LENGTH_SHORT).show();

                    // WheelchairListActivity로 돌아가면서 화면 갱신
                    Intent intent = new Intent(RentalInfoActivity.this, WheelchairListActivity.class);
                    intent.putExtra("institutionCode", institutionCode);
                    intent.putExtra("skipStatusCheck", true); // 상태 확인을 건너뛰기 위한 플래그
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RentalInfoActivity.this, "대여 취소에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RentalInfoActivity.this, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
