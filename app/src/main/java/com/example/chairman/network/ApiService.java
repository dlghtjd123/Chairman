package com.example.chairman.network;

import com.example.chairman.model.LoginRequest;
import com.example.chairman.model.LoginResponse;
import com.example.chairman.model.UserCreateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/user/signup")
    Call<Void> signup(@Body UserCreateRequest userCreateRequest);

    @POST("/user/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
