package com.example.chairman;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private CheckBox userModeCheckbox, adminModeCheckbox;
    private LinearLayout adminCodeLayout;
    private EditText editTextName, editTextEmail, editTextPhone, editTextCode;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // UI 요소 초기화
        userModeCheckbox = findViewById(R.id.checkbox_user_mode);
        adminModeCheckbox = findViewById(R.id.checkbox_admin_mode);
        adminCodeLayout = findViewById(R.id.layout_admin_code);
        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.Email);
        editTextPhone = findViewById(R.id.tel);
        editTextCode = findViewById(R.id.Code);
        btnSignUp = findViewById(R.id.btnSignUp);

        // 관리자 모드 체크박스 선택 시 Code 입력 필드 표시
        adminModeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                adminCodeLayout.setVisibility(View.VISIBLE); // 체크되면 Code 필드 보이기
                userModeCheckbox.setChecked(false); // 유저 모드는 체크 해제
            } else {
                adminCodeLayout.setVisibility(View.GONE); // 체크 해제되면 Code 필드 숨기기
            }
        });

        // 유저 모드 체크박스 선택 시 관리자 모드 체크박스 해제
        userModeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                adminModeCheckbox.setChecked(false); // 관리자 모드 체크 해제
                adminCodeLayout.setVisibility(View.GONE); // Code 필드 숨기기
            }
        });

        // 회원가입 버튼 클릭 리스너
        btnSignUp.setOnClickListener(v -> submitForm());
    }

    private void submitForm() {
        // 입력된 데이터 가져오기
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        String role = userModeCheckbox.isChecked() ? "유저 모드" : "관리자 모드";
        String adminCode = adminModeCheckbox.isChecked() ? editTextCode.getText().toString() : null;

        // 임시로 회원가입 완료 메시지 출력
        Toast.makeText(this, "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show();

        // 회원가입 후 로그인 페이지로 이동할 경우 Intent 사용
        // Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        // startActivity(intent);
    }
}
