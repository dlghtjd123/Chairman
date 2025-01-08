package com.example.chairman.user;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.chairman.R;

public class CustomTimePickerDialog extends DialogFragment {

    private OnTimeSelectedListener listener;

    public interface OnTimeSelectedListener {
        void onTimeSelected(int hour, int minute);
    }

    public void setOnTimeSelectedListener(OnTimeSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_custom_time_picker, null);

        // 시간과 분 선택
        NumberPicker hourPicker = view.findViewById(R.id.hour_picker);
        NumberPicker minutePicker = view.findViewById(R.id.minute_picker);
        NumberPicker amPmPicker = view.findViewById(R.id.am_pm_picker);

        // 시간 설정 (1~12)
        hourPicker.setMinValue(1);
        hourPicker.setMaxValue(12);
        hourPicker.setWrapSelectorWheel(true);

        // 분 설정 (5분 단위)
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(11);
        minutePicker.setDisplayedValues(new String[]{
                "00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"
        });
        minutePicker.setWrapSelectorWheel(true);

        // AM/PM 설정
        amPmPicker.setMinValue(0);
        amPmPicker.setMaxValue(1);
        amPmPicker.setDisplayedValues(new String[]{"AM", "PM"});
        amPmPicker.setWrapSelectorWheel(true);

        // 확인 버튼
        view.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            int selectedHour = hourPicker.getValue();
            int selectedMinute = minutePicker.getValue() * 5;
            boolean isPM = (amPmPicker.getValue() == 1);

            // 24시간 형식으로 변환
            if (isPM && selectedHour != 12) {
                selectedHour += 12;
            } else if (!isPM && selectedHour == 12) {
                selectedHour = 0;
            }

            if (listener != null) {
                listener.onTimeSelected(selectedHour, selectedMinute);
            }
            dismiss();
        });

        // 취소 버튼
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dismiss());

        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // 투명 배경
        return dialog;
    }
}



