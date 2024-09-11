package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdminAccountFragment extends Fragment {

    private TextView accountText;
    public static final String PREFS_NAME = "TextSizePrefs";
    public static final String TEXT_SIZE_KEY = "textSize";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_account, container, false);

        accountText = view.findViewById(R.id.text_account);

        // SharedPreferences에서 글자 크기 값 가져오기
        SharedPreferences preferences = getContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        float textSize = preferences.getFloat(TEXT_SIZE_KEY, 20); // 기본값 20sp

        accountText.setTextSize(textSize); // 글자 크기 적용

        return view;
    }
}
