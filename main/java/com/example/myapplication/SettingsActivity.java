package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private TextView sampleText;
    private SeekBar textSizeSeekBar;
    private Button logoutButton;

    public static final String PREFS_NAME = "TextSizePrefs";
    public static final String TEXT_SIZE_KEY = "textSize";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Toolbar 설정 및 뒤로가기 버튼 활성화
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // UI 요소 초기화
        sampleText = findViewById(R.id.sampleText);
        textSizeSeekBar = findViewById(R.id.textSizeSeekBar);
        logoutButton = findViewById(R.id.logoutButton);

        // SharedPreferences에서 저장된 글자 크기 값 가져오기
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        float savedTextSize = preferences.getFloat(TEXT_SIZE_KEY, 16); // 기본값 16sp

        // 글자 크기 설정
        sampleText.setTextSize(savedTextSize);
        textSizeSeekBar.setProgress((int) ((savedTextSize - 12) / 0.5f)); // SeekBar 업데이트

        // SeekBar의 값이 변경될 때마다 글자 크기를 저장
        textSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float textSize = 12 + (progress * 0.5f); // 기본 크기 12sp, 최대 크기 24sp
                sampleText.setTextSize(textSize);

                // SharedPreferences에 글자 크기 저장
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat(TEXT_SIZE_KEY, textSize);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 로그아웃 버튼 클릭 시 로그인 화면으로 이동
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    // 뒤로가기 버튼 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 이전 화면으로 이동 (뒤로가기)
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish(); // SettingsActivity 종료
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
