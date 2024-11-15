package com.example.chairman.user;// WheelchairRentalActivity.java

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chairman.R;

public class WheelchairListActivity extends AppCompatActivity {
    private Long institutionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheelchair_list);

        institutionId = getIntent().getLongExtra("institutionId", -1);

        if (institutionId != -1) {
            // institutionId로 서버에서 휠체어 정보를 가져와서 화면에 표시
            fetchWheelchairs(institutionId);
        }
    }

    private void fetchWheelchairs(Long institutionId) {
        // 서버 요청을 통해 선택한 공공기관의 휠체어 정보를 가져오고 UI에 표시
        // 여기서 Retrofit을 사용하여 institutionId에 따라 데이터를 로드합니다.
    }
}
