package com.example.chairman.normal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chairman.R;
import com.example.chairman.model.UserCreateRequest;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextPasswordConfirm;
    private EditText editTextName, editTextPhoneNumber, editTextAddress;
    private TextView textViewEmailError, textViewPhoneError, textViewPasswordError, textViewPasswordConfirmError;
    private Button btnCheckEmail; // 중복 확인 버튼 추가
    private Button btnSignup;
    private CheckBox checkBoxTerms, checkBoxPrivacy, checkBoxThirdParty;
    private ApiService apiService;
    private final Handler handler = new Handler();
    private Runnable emailCheckRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //UI 초기화
        initUI();
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        //이메일 중복 확인 버튼 클릭 이벤트 설정
        setupCheckEmailButton();
        //전화번호 입력 이벤트 처리
        setupPhoneValidation();
        //비밀번호 입력 및 확인 이벤트 처리
        setupPasswordValidation();
        //체크박스 클릭 이벤트 설정
        setupCheckBoxPopups();
        //회원가입 버튼 클릭 이벤트
        btnSignup.setOnClickListener(v -> signUpUser());
    }
    
    private void initUI() {

        editTextEmail = findViewById(R.id.editTextEmail);
        btnCheckEmail = findViewById(R.id.btnCheckEmail); //중복확인 버튼 ID 매핑
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = findViewById(R.id.editTextConfirmPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextAddress = findViewById(R.id.editTextAddress);
        textViewEmailError = findViewById(R.id.textViewEmailError);
        textViewPhoneError = findViewById(R.id.textViewPhoneError);
        textViewPasswordError = findViewById(R.id.textViewPasswordError);
        textViewPasswordConfirmError = findViewById(R.id.textViewPasswordConfirmError);
        btnSignup = findViewById(R.id.btnSignUp);
        checkBoxTerms = findViewById(R.id.checkBoxTerms);
        checkBoxPrivacy = findViewById(R.id.checkBoxPrivacy);
        checkBoxThirdParty = findViewById(R.id.checkBoxThirdParty);
    }

    private void setupCheckEmailButton() {
        btnCheckEmail.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                //이메일이 비어있을 때 오류 표시
                textViewEmailError.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                textViewEmailError.setText("이메일을 입력하세요.");
                textViewEmailError.setVisibility(View.VISIBLE);
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                //이메일 형식이 올바르지 않을 때 오류 표시
                textViewEmailError.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                textViewEmailError.setText("유효한 이메일 주소를 입력하세요.");
                textViewEmailError.setVisibility(View.VISIBLE);
            }
            else {
                //이메일 형식이 올바르면 중목 검사 호출
                textViewEmailError.setVisibility(View.GONE);
                checkEmailDuplication(email);
            }
        });
    }

    private void setupPhoneValidation() {
        editTextPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phoneNumber = s.toString().trim();
                if (!phoneNumber.matches("\\d{3}-\\d{4}-\\d{4}")) {
                    textViewPhoneError.setText("전화번호 형식이 잘못되었습니다. 예: 010-0000-0000");
                    textViewPhoneError.setVisibility(View.VISIBLE);
                }
                else {
                    textViewPhoneError.setVisibility(View.GONE);
                    checkPhoneNumberDuplication(phoneNumber);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupPasswordValidation() {
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString().trim();
                if (!isPasswordValid(password)) {
                    textViewPasswordError.setText("비밀번호는 최소 8자이며, 숫자, 특수문자, 대문자, 소문자를 포함해야 합니다.");
                    textViewPasswordError.setVisibility(View.VISIBLE);
                } else {
                    textViewPasswordError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editTextPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = s.toString().trim();

                if (!password.equals(confirmPassword)) {
                    textViewPasswordConfirmError.setText("비밀번호가 일치하지 않습니다.");
                    textViewPasswordConfirmError.setVisibility(View.VISIBLE);
                } else {
                    textViewPasswordConfirmError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupCheckBoxPopups() {
        setCheckBoxWithPopup(checkBoxTerms, "이용 약관", getStringFromFile("terms.txt"));
        setCheckBoxWithPopup(checkBoxPrivacy, "개인정보 수집 및 이용 동의", getStringFromFile("privacy_policy.txt"));
        setCheckBoxWithPopup(checkBoxThirdParty, "개인정보 제3자 제공 동의", getStringFromFile("third_party_consent.txt"));
    }

    private void setCheckBoxWithPopup(CheckBox checkBox, String title, String message) {
        CheckBox.OnCheckedChangeListener listener = new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showAgreementDialog(
                            title,
                            message,
                            (dialog, which) -> checkBox.setChecked(true),
                            (dialog, which) -> {
                                checkBox.setOnCheckedChangeListener(null);
                                checkBox.setChecked(false);
                                checkBox.setOnCheckedChangeListener(this);
                            }
                    );
                }
            }
        };
        checkBox.setOnCheckedChangeListener(listener);
    }

    private void showAgreementDialog(String title, String message, DialogInterface.OnClickListener agreeListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("동의", agreeListener)
                .setNegativeButton("취소", cancelListener)
                .create()
                .show();
    }


    private void signUpUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordConfirm = editTextPasswordConfirm.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        if (!isPasswordValid(password)) {
            textViewPasswordError.setText("비밀번호는 최소 8자이며, 숫자, 특수문자, 영소문자를 포함해야 합니다.");
            textViewPasswordError.setVisibility(View.VISIBLE);
            return;
        } else {
            textViewPasswordError.setVisibility(View.GONE);
        }

        if (!password.equals(passwordConfirm)) {
            textViewPasswordConfirmError.setText("비밀번호가 일치하지 않습니다.");
            textViewPasswordConfirmError.setVisibility(View.VISIBLE);
            return;
        } else {
            textViewPasswordConfirmError.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)
                || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkBoxTerms.isChecked() || !checkBoxPrivacy.isChecked() || !checkBoxThirdParty.isChecked()) {
            Toast.makeText(this, "모든 동의 항목에 체크하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name, phoneNumber, address, false, true, true, true);
        apiService.signup(userCreateRequest).enqueue(new Callback<Void>() {
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

    private boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=]).{8,}$";
        return password.matches(passwordPattern);
    }

    // 이메일 중복 검사 API 호출
    private void checkEmailDuplication(String email) {
        apiService.checkEmailDuplication(email).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 사용 가능한 이메일
                    textViewEmailError.setTextColor(getResources().getColor(android.R.color.holo_green_dark)); // 초록색
                    textViewEmailError.setText("사용 가능한 이메일입니다.");
                    textViewEmailError.setVisibility(View.VISIBLE);
                } else if (response.code() == 409) {
                    // 이미 존재하는 이메일
                    textViewEmailError.setTextColor(getResources().getColor(android.R.color.holo_red_dark)); // 빨간색
                    textViewEmailError.setText("이미 존재하는 이메일입니다.");
                    textViewEmailError.setVisibility(View.VISIBLE);
                } else {
                    // 기타 오류
                    textViewEmailError.setTextColor(getResources().getColor(android.R.color.holo_red_dark)); // 빨간색
                    textViewEmailError.setText("오류가 발생했습니다.");
                    textViewEmailError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 네트워크 오류 처리
                textViewEmailError.setTextColor(getResources().getColor(android.R.color.holo_red_dark)); // 빨간색
                textViewEmailError.setText("네트워크 오류가 발생했습니다.");
                textViewEmailError.setVisibility(View.VISIBLE);
            }
        });
    }

    // 전화번호 중복 검사 호출
    public void checkPhoneNumberDuplication(String phoneNumber) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        apiService.checkPhoneDuplication(phoneNumber).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 사용 가능한 전화번호
                    Toast.makeText(getApplicationContext(), "사용 가능한 전화번호입니다.", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 409) {
                    // 이미 존재하는 전화번호
                    Toast.makeText(getApplicationContext(), "이미 존재하는 전화번호입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 기타 오류
                    Toast.makeText(getApplicationContext(), "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 네트워크 오류 처리
                Toast.makeText(getApplicationContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getStringFromFile(String fileName) {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = getAssets().open(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}