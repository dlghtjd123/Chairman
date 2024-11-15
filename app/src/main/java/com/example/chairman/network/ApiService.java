package com.example.chairman.network;

import com.example.chairman.model.InstitutionData;
import com.example.chairman.model.LoginRequest;
import com.example.chairman.model.LoginResponse;
import com.example.chairman.model.UserCreateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // 유저 로그인 API
    @POST("/normal/login")
    Call<Void> userLogin(@Query("email") String email, @Query("password") String password);

    // 관리자 로그인 API
    @POST("/admin/login")
    Call<Void> adminLogin(@Query("code") String code);

    // 회원가입 API
    @POST("/normal/signup")
    Call<Void> signup(@Body UserCreateRequest userCreateRequest);

    // 유저 공공기관 목록 조회
    @GET("/user/institutions")
    Call<List<InstitutionData>> getInstitutions();

}
