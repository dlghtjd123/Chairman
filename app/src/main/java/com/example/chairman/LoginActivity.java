package com.example.chairman;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chairman.model.LoginRequest;
import com.example.chairman.model.LoginResponse;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;  // 이메일 필드
    private EditText editTextPassword;
    private Button btnLogin;
    private Button btnSignUp;
    private ApiService apiService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // View 연결
        editTextEmail = findViewById(R.id.editTextEmail);  // 이메일 필드
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Retrofit API 서비스 초기화
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // 로그인 버튼 클릭 이벤트
        btnLogin.setOnClickListener(v -> loginUser());

        // 회원가입 버튼 클릭 이벤트 (회원가입 화면으로 이동)
        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();  // 이메일 사용
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 로그인 요청 객체 생성
        LoginRequest loginRequest = new LoginRequest(email, password);  // 이메일 사용

        // 서버로 로그인 요청 전송
        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                    goToMainActivity(); // 로그인 성공 시 메인 화면으로 이동
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // 현재 로그인 화면 종료
    }
}
