package com.example.chairman.network;

import com.example.chairman.model.InstitutionData;
import com.example.chairman.model.LoginRequest;
import com.example.chairman.model.LoginResponse;
import com.example.chairman.model.RentalRequest;
import com.example.chairman.model.UserRentalResponse;
import com.example.chairman.model.WaitingRentalResponse;
import com.example.chairman.model.UserCreateRequest;
import com.example.chairman.model.UserUpdateRequest;
import com.example.chairman.model.WheelchairDetailResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("/normal/login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @GET("/normal/check-email")
    Call<ResponseBody> checkEmailDuplication(@Query("email") String email);

    @GET("/normal/check-email")
    Call<ResponseBody> checkPhoneDuplication(@Query("phoneNumber") String phoneNumber);

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

    //프로필 사진 업로드
    @Multipart
    @POST("/user/profile/photo")
    Call<Void> uploadProfilePhoto(
            @Header("Authorization") String authorization,
            @Part MultipartBody.Part photo
    );

    // 상태별 통계 조회
    @GET("/admin/{institutionCode}/status-count")
    Call<Map<String, Long>> getStatusCounts(@Path("institutionCode") Long institutionCode);

    @GET("/admin/wheelchair/count")
    Call<Map<String, Integer>> getWheelchairCounts();

    @PUT("/admin/wheelchair/{wheelchairId}/status")
    Call<Void> updateWheelchairAndRentalStatus(
            @Path("wheelchairId") Long wheelchairId,
            @Query("wheelchairStatus") String wheelchairStatus,
            @Query("rentalStatus") String rentalStatus
    );

    @PUT("admin/wheelchair/{wheelchairId}/onlyStatus")
    Call<Void> updateWheelchairStatus(
            @Path("wheelchairId") Long wheelchairId,
            @Query("status") String status
    );

    // 상태별 휠체어 세부 정보 조회
    @GET("/admin/{institutionCode}/details")
    Call<List<WheelchairDetailResponse>> getWheelchairDetails(
            @Path("institutionCode") Long institutionCode,
            @Query("status") String status
    );

    //대여 요청
    @POST("/rental/{institutionCode}/rent")
    Call<WaitingRentalResponse> rentWheelchair(
            @Header("Authorization") String authorization,
            @Path("institutionCode") Long institutionCode,
            @Body RentalRequest rentalRequest
    );

    // 대여 정보 조회 API
    @GET("/rental/info")
    Call<UserRentalResponse> getRentalInfo(@Header("Authorization") String authorization);

    // 대여 목록 조회
    @GET("/rental/{institutionCode}/list")
    Call<List<WaitingRentalResponse>> getWaitingRentals(@Path("institutionCode") Long institutionCode);

    // 대여 요청 승인
    @PUT("/rental/{institutionCode}/accept/{rentalId}")
    Call<WaitingRentalResponse> approveRental(
            @Path("institutionCode") Long institutionCode,
            @Path("rentalId") Long rentalId
    );

    // 대여 요청 거절
    @DELETE("/rental/{institutionCode}/reject/{rentalId}")
    Call<Void> rejectRental(
            @Path("institutionCode") Long institutionCode,
            @Path("rentalId") Long rentalId
    );



    @POST("/rental/{institutionCode}/cancel")
    Call<Void> cancelRental(
            @Path("institutionCode") Long institutionCode,
            @Header("Authorization") String authorization
    );

}
