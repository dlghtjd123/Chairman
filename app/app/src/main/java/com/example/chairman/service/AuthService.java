package com.example.chairman.service;

import com.example.chairman.controller.UserServiceApi;
import com.example.chairman.domain.LoginRequest;
import com.example.chairman.domain.LoginResponse;
import com.example.chairman.domain.User;
import com.example.chairman.domain.UserCreateRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthService {
    private UserServiceApi userServiceApi;
    private String token;

    public AuthService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://YOUR_SERVER_BASE_URL")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userServiceApi = retrofit.create(UserServiceApi.class);
    }

    public void signup(UserCreateRequest userCreateRequest, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = userServiceApi.signup(userCreateRequest);
        call.enqueue(callback);
    }

    public void login(LoginRequest loginRequest, Callback<LoginResponse> callback) {
        Call<LoginResponse> call = userServiceApi.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    token = response.body().getToken();  // 토큰 저장
                    callback.onResponse(call, response);
                } else {
                    callback.onFailure(call, new Throwable("Login failed"));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void logout(Callback<ResponseBody> callback) {
        if (token != null) {
            Call<ResponseBody> call = userServiceApi.logout("Bearer " + token);
            call.enqueue(callback);
        }
    }

    public void getCurrentUser(Callback<User> callback) {
        if (token != null) {
            Call<User> call = userServiceApi.getCurrentUser("Bearer " + token);
            call.enqueue(callback);
        }
    }

    public void getAuthStatus(Callback<ResponseBody> callback) {
        if (token != null) {
            Call<ResponseBody> call = userServiceApi.getAuthStatus("Bearer " + token);
            call.enqueue(callback);
        }
    }
}
