package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoticeAdapter adapter;
    private List<String> noticeList;
    private EditText noticeInput;
    private Button addNoticeButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        // RecyclerView와 Adapter 설정
        recyclerView = view.findViewById(R.id.recyclerView);
        noticeList = new ArrayList<>();  // 공지사항 리스트 초기화
        adapter = new NoticeAdapter(noticeList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // 공지사항 입력 필드와 추가 버튼 참조
        noticeInput = view.findViewById(R.id.noticeInput);
        addNoticeButton = view.findViewById(R.id.addNoticeButton);

        // 공지사항 추가 버튼 클릭 리스너
        addNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNotice = noticeInput.getText().toString().trim();
                if (!newNotice.isEmpty()) {
                    noticeList.add(newNotice);  // 공지사항 추가
                    adapter.notifyItemInserted(noticeList.size() - 1);  // 리스트 업데이트
                    noticeInput.setText("");  // 입력 필드 초기화
                }
            }
        });

        return view;
    }
}
