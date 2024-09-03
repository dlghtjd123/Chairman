package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        // 하단 네비게이션 바 설정
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.navigation_home) {

                }
                else if(item.getItemId() == R.id.navigation_rentals) {

                }
                else if(item.getItemId() == R.id.navigation_reservations) {

                }
                else if(item.getItemId() == R.id.navigation_account) {

                }
                return false;
            }
        });

        // 예약 수락 버튼 클릭 리스너
        Button acceptButton = findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 예약 수락 처리
                Toast.makeText(AdminMainActivity.this, "예약이 수락되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 예약 취소 버튼 클릭 리스너
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 예약 취소 처리
                Toast.makeText(AdminMainActivity.this, "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}