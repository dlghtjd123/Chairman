package com.example.myapplication;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class ModeSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_selection);

        Button userModeButton = findViewById(R.id.userModeButton);
        Button adminModeButton = findViewById(R.id.adminModeButton);

        userModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 유저 모드로 이동
                Intent intent = new Intent(ModeSelectionActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        adminModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 관리자 모드로 이동
                Intent intent = new Intent(ModeSelectionActivity.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });
    }
}