package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

public final class RoundedButtonBackgroundBinding implements ViewBinding {
    @NonNull
    private final Button rootView;

    private RoundedButtonBackgroundBinding(@NonNull Button rootView) {
        this.rootView = rootView;
    }

    @Override
    @NonNull
    public Button getRoot() {
        return rootView;
    }

    @NonNull
    public static RoundedButtonBackgroundBinding inflate(@NonNull LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    @NonNull
    public static RoundedButtonBackgroundBinding inflate(@NonNull LayoutInflater inflater,
                                                         @Nullable ViewGroup parent, boolean attachToParent) {
        // 올바른 레이아웃 파일 로드 (Button이 포함된 레이아웃 사용)
        View root = inflater.inflate(R.layout.fragment_admin_rentals, parent, false);
        if (attachToParent && parent != null) {
            parent.addView(root);
        }
        return bind(root);
    }

    @NonNull
    public static RoundedButtonBackgroundBinding bind(@NonNull View rootView) {
        // 올바른 타입 확인 및 캐스팅
        if (!(rootView instanceof Button)) {
            throw new IllegalStateException("Expected a Button view.");
        }
        Button button = (Button) rootView;
        // Drawable 배경 설정
        button.setBackgroundResource(R.drawable.rounded_button_background);
        return new RoundedButtonBackgroundBinding(button);
    }
}
