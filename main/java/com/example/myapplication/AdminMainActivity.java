package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity {

    private TextView titleText;
    private ImageButton settingsButton;
    public static final String PREFS_NAME = "TextSizePrefs";
    public static final String TEXT_SIZE_KEY = "textSize";
    private static final int SETTINGS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        titleText = findViewById(R.id.title);
        settingsButton = findViewById(R.id.settingsButton);

        // SharedPreferences에서 저장된 글자 크기 불러오기
        applyTextSize();

        // 설정 버튼 클릭 이벤트 처리
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, SETTINGS_REQUEST_CODE);
        });

        // 하단 네비게이션 설정
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navigation_home) {
                selectedFragment = new AdminHomeFragment();
            } else if (item.getItemId() == R.id.navigation_rentals) {
                selectedFragment = new AdminRentalsFragment();
            } else if (item.getItemId() == R.id.navigation_reservations) {
                selectedFragment = new AdminReservationFragment();
            } else if (item.getItemId() == R.id.navigation_account) {
                selectedFragment = new AdminAccountFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });

        // 초기 화면으로 HomeFragment 설정
        loadFragment(new AdminHomeFragment());
    }

    // SharedPreferences에서 저장된 글자 크기를 적용
    private void applyTextSize() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        float textSize = preferences.getFloat(TEXT_SIZE_KEY, 20); // 기본값 20sp
        titleText.setTextSize(textSize);
    }

    // Fragment를 로드하는 메서드
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SettingsActivity에서 돌아왔을 때 글자 크기 적용
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK) {
            applyTextSize(); // 변경된 글자 크기 적용
        }
    }
}
