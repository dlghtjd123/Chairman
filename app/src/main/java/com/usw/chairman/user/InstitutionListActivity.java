package com.usw.chairman.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.usw.chairman.R;
import com.usw.chairman.model.InstitutionData;
import com.usw.chairman.network.ApiClient;
import com.usw.chairman.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstitutionListActivity extends AppCompatActivity {

    private ListView institutionListView;
    private List<String> institutionNames = new ArrayList<>();
    private List<Long> institutionCodes = new ArrayList<>(); // 기관 코드 리스트
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_list);

        institutionListView = findViewById(R.id.institutionListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, institutionNames);
        institutionListView.setAdapter(adapter);

        // 공공기관 클릭 이벤트
        institutionListView.setOnItemClickListener((adapterView, view, position, id) -> {
            Long selectedInstitutionCode = institutionCodes.get(position); // 선택한 기관 코드 가져오기
            Intent intent = new Intent(InstitutionListActivity.this, WheelchairListActivity.class);
            intent.putExtra("institutionCode", selectedInstitutionCode); // 코드 전달
            startActivity(intent);
        });

        // 서버에서 공공기관 데이터를 가져옴
        fetchInstitutionsFromServer();
    }

    private void fetchInstitutionsFromServer() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        Call<List<InstitutionData>> call = apiService.getInstitutions();
        call.enqueue(new Callback<List<InstitutionData>>() {
            @Override
            public void onResponse(Call<List<InstitutionData>> call, Response<List<InstitutionData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<InstitutionData> institutions = response.body();

                    // 공공기관 데이터 추가
                    for (InstitutionData institution : institutions) {
                        institutionNames.add(institution.getName());
                        institutionCodes.add(institution.getInstitutionCode()); // 기관 코드 추가
                    }

                    // 어댑터에 변경 사항 적용
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(InstitutionListActivity.this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<InstitutionData>> call, Throwable t) {
                Toast.makeText(InstitutionListActivity.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
