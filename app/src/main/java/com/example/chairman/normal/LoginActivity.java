package com.example.chairman.normal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chairman.R;
import com.example.chairman.admin.WaitingListActivity;
import com.example.chairman.model.LoginRequest;
import com.example.chairman.model.LoginResponse;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;
import com.example.chairman.user.InstitutionListActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextCode;
    private TextView TextEmail, TextPassword, TextCode;
    private Button btnLogin, btnSignUp;
    private RadioGroup radioGroupMode;
    private RadioButton radioUser, radioAdmin;
    private ApiService apiService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // View 초기화
        editTextEmail = findViewById(R.id.editTextEmail);
        TextEmail = findViewById(R.id.TextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        TextPassword = findViewById(R.id.TextPassword);
        editTextCode = findViewById(R.id.editTextCode);
        TextCode = findViewById(R.id.TextCode);
        btnLogin = findViewById(R.id.btnLogin);
        radioGroupMode = findViewById(R.id.radioGroupMode);
        radioUser = findViewById(R.id.radioUser);
        radioAdmin = findViewById(R.id.radioAdmin);
        btnSignUp = findViewById(R.id.btnSignUp);

        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // 관리자/사용자 선택 시 UI 변경
        radioGroupMode.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioAdmin) {
                editTextEmail.setVisibility(View.GONE);
                TextEmail.setVisibility(View.GONE);
                editTextPassword.setVisibility(View.GONE);
                TextPassword.setVisibility(View.GONE);
                editTextCode.setVisibility(View.VISIBLE);
                TextCode.setVisibility(View.VISIBLE);
            } else {
                editTextEmail.setVisibility(View.VISIBLE);
                TextEmail.setVisibility(View.VISIBLE);
                editTextPassword.setVisibility(View.VISIBLE);
                TextPassword.setVisibility(View.VISIBLE);
                editTextCode.setVisibility(View.GONE);
                TextCode.setVisibility(View.GONE);
            }
        });

        // 로그인 버튼 클릭 이벤트
        btnLogin.setOnClickListener(v -> {
            if (radioAdmin.isChecked()) {
                String code = editTextCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(this, "코드를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    loginAdmin(code);
                }
            } else {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password);
                }
            }
        });

        // 회원가입 버튼 클릭 이벤트
        btnSignUp.setOnClickListener(v -> openSignUpActivity());
    }

    private void openSignUpActivity() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void loginUser(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        apiService.userLogin(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String jwtToken = response.body().getJwtToken();

                    if (jwtToken != null) {
                        // JWT 토큰 저장
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("jwtToken", jwtToken);
                        editor.apply();

                        // 로그 확인
                        Log.d("LoginActivity", "JWT 토큰 저장 완료: " + jwtToken);

                        // 다음 화면으로 이동
                        Intent intent = new Intent(LoginActivity.this, InstitutionListActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("LoginActivity", "JWT 토큰이 null입니다.");
                        Toast.makeText(LoginActivity.this, "로그인 실패: JWT 토큰이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("LoginActivity", "로그인 실패: " + response.message());
                    Toast.makeText(LoginActivity.this, "로그인 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginActivity", "네트워크 오류: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loginAdmin(String code) {
        apiService.adminLogin(code).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    String jwtToken = loginResponse.getJwtToken();
                    Long institutionCode = loginResponse.getInstitution().getInstitutionCode();

                    // JWT 토큰 저장
                    saveJwtToken(jwtToken);

                    if (institutionCode != null) {
                        // WaitingListActivity로 institutionCode 전달
                        Intent intent = new Intent(LoginActivity.this, WaitingListActivity.class);
                        intent.putExtra("institutionCode", institutionCode);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "기관 코드가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
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


    private void saveJwtToken(String jwtToken) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jwtToken", jwtToken);
        editor.apply();
    }
}
