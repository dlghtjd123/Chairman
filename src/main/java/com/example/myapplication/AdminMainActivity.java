package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main); // AdminMainActivity에 맞는 레이아웃 파일 설정

        // 초기 화면으로 HomeFragment 설정
        loadFragment(new AdminHomeFragment());

        // 하단 네비게이션 바 설정
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.navigation_home) {
                    selectedFragment = new AdminHomeFragment();
                } else if (item.getItemId() == R.id.navigation_rentals) {
                    selectedFragment = new AdminRentalsFragment();
                } else if (item.getItemId() == R.id.navigation_reservations) {
                    selectedFragment = new AdminReservationFragment(); // 예약 Fragment로 이동
                } else if (item.getItemId() == R.id.navigation_account) {
                    selectedFragment = new AdminAccountFragment();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    return true;
                }
                return false;
            }
        });

        // 예약 수락 및 취소 버튼은 AdminReservationFragment에 포함되므로 이 Activity에서는 필요하지 않습니다.
    }

    private void loadFragment(Fragment fragment) {
        // Fragment 전환 처리
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
