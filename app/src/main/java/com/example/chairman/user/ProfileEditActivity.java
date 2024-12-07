package com.example.chairman.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.chairman.R;
import com.example.chairman.model.UserUpdateRequest;
import com.example.chairman.network.ApiClient;
import com.example.chairman.network.ApiService;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_IMAGE = 1;

    private EditText editTextName, editTextEmail, editTextPhone, editTextAddress;
    private Button buttonSave, buttonCancel, buttonBack;
    private ImageView profileImageView;
    private ImageButton cameraButton;
    private Uri selectedImageUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        // UI 요소 초기화
        profileImageView = findViewById(R.id.profile_image);
        cameraButton = findViewById(R.id.camera_button);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAddress = findViewById(R.id.editTextAddress);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonBack = findViewById(R.id.buttonBack);

        // 뒤로가기 버튼 동작 설정
        buttonBack.setOnClickListener(v -> finish());

        // SharedPreferences에서 JWT 토큰 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if (jwtToken == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 사용자 정보 불러오기
        loadUserInfo(jwtToken);

        // 프로필 사진 변경 버튼 클릭 이벤트
        cameraButton.setOnClickListener(v -> openImagePicker());


        // 저장 버튼 클릭 이벤트
        buttonSave.setOnClickListener(v -> {
            saveUpdatedUserInfo(jwtToken);

            // 선택한 사진이 있으면 서버에 업로드
            if (selectedImageUri != null) {
                uploadProfilePhoto(jwtToken);
            }
        });

        // 취소 버튼 클릭 이벤트
        buttonCancel.setOnClickListener(v -> {
            Toast.makeText(this, "수정이 취소되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // 사용자 정보 로드
    private void loadUserInfo(String jwtToken) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Map<String, String>> call = apiService.getUserInfo("Bearer " + jwtToken);

        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, String> userInfo = response.body();
                    editTextName.setText(userInfo.get("name"));
                    editTextEmail.setText(userInfo.get("email"));
                    editTextPhone.setText(userInfo.get("phoneNumber"));
                    editTextAddress.setText(userInfo.get("address"));

                    // 서버에서 프로필 이미지 URL 로드 및 표시
                    String profileImageUrl = userInfo.get("profileImageUrl");
                    if (profileImageUrl != null) {
                        // Glide를 사용하여 프로필 이미지 로드
                        Glide.with(ProfileEditActivity.this).load(profileImageUrl).into(profileImageView);
                    }
                } else {
                    Toast.makeText(ProfileEditActivity.this, "사용자 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(ProfileEditActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUpdatedUserInfo(String jwtToken) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // 업데이트 요청 생성
        UserUpdateRequest updateRequest = new UserUpdateRequest(
                editTextName.getText().toString(),
                editTextPhone.getText().toString(),
                editTextAddress.getText().toString()
        );

        Call<Void> call = apiService.updateUserInfo("Bearer " + jwtToken, updateRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileEditActivity.this, "정보가 성공적으로 업데이트되었습니다.", Toast.LENGTH_SHORT).show();

                    // 결과 전달
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ProfileEditActivity.this, "정보 업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProfileEditActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 프로필 사진 업로드
    private void uploadProfilePhoto(String jwtToken) {
        if (selectedImageUri == null) return;

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        try {
            File file = new File(getRealPathFromURI(selectedImageUri));
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

            Call<Void> call = apiService.uploadProfilePhoto("Bearer " + jwtToken, body);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ProfileEditActivity.this, "프로필 사진이 성공적으로 업로드되었습니다.", Toast.LENGTH_SHORT).show();
                        loadUserInfo(jwtToken); // **업로드 후 UI 갱신**
                    } else {
                        Toast.makeText(ProfileEditActivity.this, "프로필 사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ProfileEditActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "파일 경로 변환 오류: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }

    private void openImagePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PICK_IMAGE);
                return;
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PICK_IMAGE);
                return;
            }
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri);
        }
    }
}
