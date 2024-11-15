package com.example.chairman.user;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chairman.R;

public class InstitutionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_detail);

        // Intent에서 institutionId 가져오기
        Long institutionId = getIntent().getLongExtra("institutionId", -1);

        // 화면에 공공기관 ID를 표시 (테스트용)
        TextView institutionIdTextView = findViewById(R.id.institutionIdTextView);
        institutionIdTextView.setText("Selected Institution ID: " + institutionId);
    }
}
