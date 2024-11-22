package com.example.chairman.user;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chairman.R;
import com.example.chairman.model.UserUpdateRequest;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone, editTextAddress;
    private Button buttonSave, buttonCancel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        // UI 요소 초기화
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAddress = findViewById(R.id.editTextAddress);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

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

        // 저장 버튼 클릭 이벤트
        buttonSave.setOnClickListener(v -> {
            saveUpdatedUserInfo(jwtToken);
        });

        // 취소 버튼 클릭 이벤트
        buttonCancel.setOnClickListener(v -> {
            Toast.makeText(this, "수정이 취소되었습니다.", Toast.LENGTH_SHORT).show();
            finish(); // 현재 화면 닫기
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
                    // EditText에 정보 설정
                    editTextName.setText(userInfo.get("name"));
                    editTextEmail.setText(userInfo.get("email"));
                    editTextPhone.setText(userInfo.get("phoneNumber"));
                    editTextAddress.setText(userInfo.get("address"));
                } else {
                    Toast.makeText(ProfileEditActivity.this, "사용자 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(ProfileEditActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 수정된 사용자 정보 저장
    private void saveUpdatedUserInfo(String jwtToken) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // 업데이트 요청 생성
        UserUpdateRequest updateRequest = new UserUpdateRequest(
                editTextName.getText().toString(),
                editTextPhone.getText().toString(),
                editTextAddress.getText().toString()
        );

        Call<Void> call = apiService.updateUserInfo("Bearer " + jwtToken, updateRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileEditActivity.this, "정보가 성공적으로 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                    finish(); // 저장 후 화면 종료
                } else {
                    Toast.makeText(ProfileEditActivity.this, "정보 업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProfileEditActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
