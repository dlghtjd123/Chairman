package com.example.chairman.normal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chairman.R;
import com.example.chairman.model.UserCreateRequest;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private EditText editTextAddress;
    private Button btnSignUp;
    private RadioButton radioAdmin;
    private LinearLayout userModeLayout;
    private LinearLayout adminModeLayout;
    private RadioGroup radioGroupMode;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextAddress = findViewById(R.id.editTextAddress);
        btnSignUp = findViewById(R.id.btnSignUp);
        radioAdmin = findViewById(R.id.radioAdmin);
        userModeLayout = findViewById(R.id.userModeLayout);
        adminModeLayout = findViewById(R.id.adminModeLayout);
        radioGroupMode = findViewById(R.id.radioGroupMode);

        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // RadioGroup 리스너 설정
        radioGroupMode.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioAdmin) {
                adminModeLayout.setVisibility(View.VISIBLE);
                userModeLayout.setVisibility(View.GONE);
            } else {
                adminModeLayout.setVisibility(View.GONE);
                userModeLayout.setVisibility(View.VISIBLE);
            }
        });

        btnSignUp.setOnClickListener(v -> signUpUser());
    }

    private void signUpUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        boolean isAdmin = radioAdmin.isChecked();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)
                || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name, phoneNumber, address, isAdmin);

        Call<Void> call = apiService.signup(userCreateRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "회원가입 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}