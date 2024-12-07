package com.example.chairman.model;

import com.google.gson.annotations.SerializedName;

public class WheelchairDetailResponse {

    @SerializedName("wheelchairId")
    private Long wheelchairId;

    @SerializedName("wheelchairStatus") // 수정: 서버와 동일한 필드 이름
    private String wheelchairStatus;

    @SerializedName("rentalStatus")
    private String rentalStatus;

    @SerializedName("type")
    private String type;

    @SerializedName("userName")
    private String userName;

    @SerializedName("userPhone")
    private String userPhone;

    // Getters and Setters
    public Long getWheelchairId() {
        return wheelchairId;
    }

    public void setWheelchairId(Long wheelchairId) {
        this.wheelchairId = wheelchairId;
    }

    public String getWheelchairStatus() {
        return wheelchairStatus;
    }

    public void setWheelchairStatus(String wheelchairStatus) {
        this.wheelchairStatus = wheelchairStatus;
    }

    public String getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(String rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
