package com.example.chairman.normal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chairman.AdminMainActivity;
import com.example.chairman.R;
import com.example.chairman.UserMainActivity;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private TextView TextEmail;
    private EditText editTextPassword;
    private TextView TextPassword;
    private EditText editTextCode;
    private TextView TextCode;
    private Button btnLogin;
    private RadioGroup radioGroupMode;
    private RadioButton radioUser;
    private RadioButton radioAdmin;

    private ApiService apiService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

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

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        boolean isAdmin = radioAdmin.isChecked();

        if (isAdmin) {
            String code = editTextCode.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(this, "코드를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            loginAdmin(code);
        } else {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            loginUser(email, password);
        }
    }

    private void loginAdmin(String code) {
        apiService.adminLogin(code).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "관리자 로그인 성공!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password) {
        apiService.userLogin(email, password).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
