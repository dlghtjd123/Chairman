package com.example.chairman.network;

import com.example.chairman.model.InstitutionData;
import com.example.chairman.model.LoginRequest;
import com.example.chairman.model.LoginResponse;
import com.example.chairman.model.RentalRequest;
import com.example.chairman.model.RentalResponse;
import com.example.chairman.model.UserCreateRequest;
import com.example.chairman.model.UserUpdateRequest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("/normal/login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    // 관리자 로그인
    @POST("/admin/login")
    Call<LoginResponse> adminLogin(
            @Query("code") String code
    );

    // 회원가입 API
    @POST("/normal/signup")
    Call<Void> signup(@Body UserCreateRequest userCreateRequest);

    // 유저 공공기관 목록 조회
    @GET("/user/institutions")
    Call<List<InstitutionData>> getInstitutions();

    // 특정 공공기관에서 대여 가능한 휠체어 개수 조회
    @GET("/user/{institutionCode}/available-count")
    Call<Map<String, Integer>> getAvailableWheelchairCounts(@Path("institutionCode") Long institutionCode);

    // 사용자 정보 조회 API
    @GET("/user/info")
    Call<Map<String, String>> getUserInfo(@Header("Authorization") String authorization);

    @PUT("/user/update")
    Call<Void> updateUserInfo(@Header("Authorization") String authorization, @Body UserUpdateRequest updateRequest);

    @GET("/user/institutions/{institutionCode}")
    Call<InstitutionData> getInstitutionDetails(@Path("institutionCode") Long institutionCode);

    // 특정 기관의 휠체어 상태별 개수 조회
    @GET("admin/{institutionCode}/wheelchair/count")
    Call<Map<String, Integer>> getWheelchairCountsByInstitution(@Path("institutionCode") Long institutionCode);

    //대여 요청
    @POST("/rental/{institutionCode}/rent")
    Call<RentalResponse> rentWheelchair(
            @Header("Authorization") String authorization,
            @Path("institutionCode") Long institutionCode,
            @Body RentalRequest rentalRequest
    );

    // 대여 정보 조회 API
    @GET("/rental/info")
    Call<RentalResponse> getRentalInfo(@Header("Authorization") String authorization);

    // 대여 목록 조회
    @GET("/rental/list")
    Call<List<RentalResponse>> getWaitingRentals();

    // 대여 승인
    @POST("/rental/approve/{rentalId}")
    Call<RentalResponse> approveRental(
            @Path("rentalId") Long rentalId
    );

    // 대여 거절
    @POST("/rental/reject/{rentalId}")
    Call<RentalResponse> rejectRental(
            @Path("rentalId") Long rentalId
    );

    @POST("/rental/{institutionCode}/cancel")
    Call<Void> cancelRental(
            @Path("institutionCode") Long institutionCode,
            @Header("Authorization") String authorization
    );

    @POST("/rental/rent")
    Call<Void> rentWheelchair(
            @Query("institutionCode") Long institutionCode,
            @Query("wheelchairType") String wheelchairType,
            @Query("rentalDate") String rentalDate
    );

}
