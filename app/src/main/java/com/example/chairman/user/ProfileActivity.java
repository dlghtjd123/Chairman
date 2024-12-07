package com.example.chairman.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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
    private ImageView profileImageView; // 프로필 사진 추가
    private Button editButton, logoutButton;

    private static final int REQUEST_CODE_EDIT_PROFILE = 1; // 요청 코드

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // XML 레이아웃 연결

        // UI 요소 초기화
        profileImageView = findViewById(R.id.profile_image); // 프로필 사진 ImageView
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
            Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
            startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE); // 수정 화면으로 이동
        });
    }

    // 사용자 정보 로드
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

                    // 프로필 이미지 로드 (Glide 사용)
                    String profileImageUrl = userInfo.get("profileImageUrl");
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Glide.with(ProfileActivity.this)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.user_profile) // 기본 이미지
                                .error(R.drawable.user_profile) // 오류 시 기본 이미지
                                .into(profileImageView);
                    }
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

    // ProfileEditActivity에서 돌아올 때 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT_PROFILE && resultCode == RESULT_OK) {
            // 수정된 데이터를 다시 로드
            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String jwtToken = sharedPreferences.getString("jwtToken", null);
            if (jwtToken != null) {
                loadUserInfo(jwtToken);
            }
        }
    }
}
