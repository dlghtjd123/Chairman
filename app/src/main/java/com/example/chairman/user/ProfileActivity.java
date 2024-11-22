package com.example.chairman.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chairman.R;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;
import com.example.chairman.normal.LoginActivity;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName, userEmail, userPhone, userAddress;
    private Button editButton, logoutButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // XML 레이아웃 연결

        // UI 요소 초기화
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userPhone = findViewById(R.id.user_phone);
        userAddress = findViewById(R.id.user_address);
        editButton = findViewById(R.id.edit_button);
        logoutButton = findViewById(R.id.logout_button);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());


        // SharedPreferences에서 JWT 토큰 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if (jwtToken == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 사용자 정보 불러오기
        loadUserInfo(jwtToken);

        // 로그아웃 버튼 클릭 이벤트
        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("jwtToken"); // 토큰 삭제
            editor.apply();

            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // 프로필 수정 버튼 클릭 이벤트
        editButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileEditActivity.class));
        });
    }

    private void loadUserInfo(String jwtToken) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Map<String, String>> call = apiService.getUserInfo("Bearer " + jwtToken);

        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, String> userInfo = response.body();
                    userName.setText(userInfo.get("name"));
                    userEmail.setText("이메일: " + userInfo.get("email"));
                    userPhone.setText("전화번호: " + userInfo.get("phoneNumber"));
                    userAddress.setText("주소: " + userInfo.get("address"));
                } else {
                    Toast.makeText(ProfileActivity.this, "사용자 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ProfileActivity", "API 호출 실패: " + t.getMessage());
            }
        });
    }
}