package com.example.chairman.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chairman.R;
import com.example.chairman.model.UserRentalResponse;
import com.example.chairman.model.WaitingRentalResponse;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WheelchairListActivity extends AppCompatActivity {

    private Button adultButton;
    private Button childButton;
    private Button checkRentalButton;
    private Button profileButton; // 프로필 버튼 추가
    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheelchair_list);

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // 모든 버튼 초기화
        adultButton = findViewById(R.id.adult_button);
        childButton = findViewById(R.id.child_button);
        checkRentalButton = findViewById(R.id.check_rental_button);
        profileButton = findViewById(R.id.profile_button);

        // "대여 확인" 버튼 클릭 이벤트 추가
        checkRentalButton.setOnClickListener(v -> checkRentalStatus());

        // 프로필 버튼 클릭 이벤트 추가
        profileButton.setOnClickListener(v -> openProfileActivity());

        // Toolbar 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Intent에서 institutionCode 가져오기
        Long institutionCode = getIntent().getLongExtra("institutionCode", -1L);

        if (institutionCode == -1L) {
            Log.e("WheelchairListActivity", "공공기관 코드가 전달되지 않았습니다.");
            Toast.makeText(this, "기관 선택이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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
        // 버튼 초기화 (데이터 로드 전 상태)
        adultButton.setText("성인용 대여 가능: 로딩 중...");
        childButton.setText("유아용 대여 가능: 로딩 중...");

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Map<String, Integer>> call = apiService.getAvailableWheelchairCounts(institutionCode);

        call.enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Integer> counts = response.body();

                    // 대여 가능한 휠체어 개수 가져오기
                    int adultCount = counts.getOrDefault("ADULT", 0);
                    int childCount = counts.getOrDefault("CHILD", 0);

                    // 버튼 텍스트 업데이트
                    adultButton.setText(String.format("성인용 대여 가능: %d대", adultCount));
                    childButton.setText(String.format("유아용 대여 가능: %d대", childCount));
                } else {
                    // 오류 처리
                    Toast.makeText(WheelchairListActivity.this, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    adultButton.setText("성인용 대여 가능: 0대");
                    childButton.setText("유아용 대여 가능: 0대");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                // 네트워크 오류 처리
                Toast.makeText(WheelchairListActivity.this, "서버 통신 오류 발생", Toast.LENGTH_SHORT).show();
                adultButton.setText("성인용 대여 가능: 0대");
                childButton.setText("유아용 대여 가능: 0대");
            }
        });
    }




    public void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class); // ProfileActivity로 이동
        startActivity(intent);
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

    // 대여 상태 확인 메서드
    // RentalInfoActivity로 이동하는 코드
    private void checkRentalStatus() {
        if (sharedPreferences == null) {
            Toast.makeText(this, "로그인 정보가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if (jwtToken == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<UserRentalResponse> call = apiService.getRentalInfo("Bearer " + jwtToken);

        call.enqueue(new Callback<UserRentalResponse>() {
            @Override
            public void onResponse(Call<UserRentalResponse> call, Response<UserRentalResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserRentalResponse rental = response.body();

                    // RentalInfoActivity로 이동
                    Intent intent = new Intent(WheelchairListActivity.this, RentalInfoActivity.class);
                    intent.putExtra("rentalStatus", rental.getStatus());
                    intent.putExtra("rentalDate", rental.getRentalDate());
                    intent.putExtra("returnDate", rental.getReturnDate());
                    startActivity(intent);

                } else {
                    Toast.makeText(WheelchairListActivity.this, "현재 대여 내역이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserRentalResponse> call, Throwable t) {
                Toast.makeText(WheelchairListActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        boolean skipStatusCheck = intent.getBooleanExtra("skipStatusCheck", false);

        // 상태 확인을 건너뛰는 경우
        if (skipStatusCheck) {
            intent.removeExtra("skipStatusCheck");
            return;
        }

        // Intent에서 institutionCode 가져오기
        Long institutionCode = intent.getLongExtra("institutionCode", -1L);

        if (institutionCode != -1L) {
            // 데이터를 새로 가져오기 (화면 갱신)
            fetchAvailableCounts(institutionCode);
        } else {
            Log.e("WheelchairListActivity", "기관 코드가 전달되지 않았습니다.");
            Toast.makeText(this, "기관 선택이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // 뒤로가기 버튼 동작
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
