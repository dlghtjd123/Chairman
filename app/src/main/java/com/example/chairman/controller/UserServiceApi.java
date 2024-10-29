package com.example.chairman.controller;

import com.example.chairman.domain.LoginRequest;
import com.example.chairman.domain.LoginResponse;
import com.example.chairman.domain.User;
import com.example.chairman.domain.UserCreateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import okhttp3.ResponseBody;

public interface UserServiceApi {

    @POST("/user/signup")
    Call<ResponseBody> signup(@Body UserCreateRequest userCreateRequest);

    @POST("/user/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/user/logout")
    Call<ResponseBody> logout(@Header("Authorization") String token);

    @GET("/user/current")
    Call<User> getCurrentUser(@Header("Authorization") String token);

    @GET("/user/authStatus")
    Call<ResponseBody> getAuthStatus(@Header("Authorization") String token);
}
