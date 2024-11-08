package com.example.chairman.network;

import com.example.chairman.model.LoginRequest;
import com.example.chairman.model.LoginResponse;
import com.example.chairman.model.UserCreateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // 유저 로그인 API
    @POST("/user/login")
    Call<Void> userLogin(@Query("email") String email, @Query("password") String password);

    // 관리자 로그인 API
    @POST("/admin/login")
    Call<Void> adminLogin(@Query("code") String code);

    // 회원가입 API (기존에 있던 메서드일 가능성)
    @POST("/user/signup")
    Call<Void> signup(@Body UserCreateRequest userCreateRequest);
}