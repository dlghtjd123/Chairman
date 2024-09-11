package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    private List<String> noticeList;

    // 생성자에서 공지사항 리스트를 받습니다.
    public NoticeAdapter(List<String> noticeList) {
        this.noticeList = noticeList;
    }

    // ViewHolder 생성
    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new NoticeViewHolder(view);
    }

    // ViewHolder에 데이터를 바인딩
    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        String notice = noticeList.get(position);
        holder.noticeTextView.setText(notice);
    }

    // 공지사항 개수 반환
    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    // ViewHolder 클래스
    class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView noticeTextView;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            noticeTextView = itemView.findViewById(android.R.id.text1); // 기본 TextView 사용
        }
    }
}
