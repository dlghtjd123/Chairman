package com.example.chairman.user;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chairman.R;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvailableWheelchairActivity extends AppCompatActivity {

    private TextView tvAvailableAdult, tvAvailableChild, tvAvailableDates;
    private ListView lvAvailableDates;
    private List<String> availableDatesList = new ArrayList<>();
    private ArrayAdapter<String> datesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_wheelchair);

        tvAvailableAdult = findViewById(R.id.tvAvailableAdult);
        tvAvailableChild = findViewById(R.id.tvAvailableChild);
        tvAvailableDates = findViewById(R.id.tvAvailableDates);
        lvAvailableDates = findViewById(R.id.lvAvailableDates);

        datesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, availableDatesList);
        lvAvailableDates.setAdapter(datesAdapter);

        Long institutionCode = getIntent().getLongExtra("institutionCode", 0);
        fetchAvailableCounts(institutionCode);
        fetchAvailableDates(institutionCode, "ADULT");

    }

    private void fetchAvailableCounts(Long institutionCode) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        apiService.getAvailableWheelchairCounts(institutionCode).enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Integer> counts = response.body();
                    tvAvailableAdult.setText("Available Adult Wheelchairs: " + counts.get("ADULT"));
                    tvAvailableChild.setText("Available Child Wheelchairs: " + counts.get("CHILD"));
                } else {
                    Toast.makeText(AvailableWheelchairActivity.this, "Failed to fetch counts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                Toast.makeText(AvailableWheelchairActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAvailableDates(Long institutionCode, String wheelchairType) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        apiService.getAvailableDates(institutionCode, wheelchairType).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    availableDatesList.clear();
                    availableDatesList.addAll(response.body());
                    datesAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AvailableWheelchairActivity.this, "Failed to fetch dates", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(AvailableWheelchairActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
