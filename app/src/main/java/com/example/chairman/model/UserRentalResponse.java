package com.example.chairman.model;

import com.google.gson.annotations.SerializedName;

public class UserRentalResponse {
    @SerializedName("rentalId")
    private Long rentalId;

    @SerializedName("rentalCode")
    private String rentalCode;

    @SerializedName("status")
    private String status;

    @SerializedName("rentalDate")
    private String rentalDate;

    @SerializedName("returnDate")
    private String returnDate;

    @SerializedName("wheelchairType")
    private String wheelchairType;

    @SerializedName("institutionName")
    private String institutionName;

    @SerializedName("institutionAddress")
    private String institutionAddress;

    @SerializedName("institutionPhone")
    private String institutionPhone;

    @SerializedName("institutionCode")
    private Long institutionCode;

    @SerializedName("userPhoneNumber") // JSON 필드 이름과 매핑
    private String userPhoneNumber;

    // Getters and Setters
    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public String getRentalCode() {
        return rentalCode;
    }

    public void setRentalCode(String rentalCode) {
        this.rentalCode = rentalCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getWheelchairType() {
        return wheelchairType;
    }

    public void setWheelchairType(String wheelchairType) {
        this.wheelchairType = wheelchairType;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getInstitutionAddress() {
        return institutionAddress;
    }

    public void setInstitutionAddress(String institutionAddress) {
        this.institutionAddress = institutionAddress;
    }

    public String getInstitutionPhone() {
        return institutionPhone;
    }

    public void setInstitutionPhone(String institutionPhone) {
        this.institutionPhone = institutionPhone;
    }

    public Long getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(Long institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
}
