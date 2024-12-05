package com.example.chairman.model;

import com.google.gson.annotations.SerializedName;

public class WheelchairDetailResponse {

    @SerializedName("wheelchairId")
    private Long wheelchairId;

    @SerializedName("status")
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

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
