package com.usw.chairman.model;

import com.google.gson.annotations.SerializedName;

public class WaitingRentalResponse {
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

    @SerializedName("user") // 중첩된 user 객체
    private User user;

    @SerializedName("wheelchair") // 중첩된 wheelchair 객체
    private Wheelchair wheelchair;

    @SerializedName("institution") // 중첩된 institution 객체
    private Institution institution;

    // User 클래스 정의
    public static class User {
        @SerializedName("name")
        private String name;

        @SerializedName("phoneNumber") // JSON 응답의 전화번호 필드
        private String phoneNumber;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

    // Wheelchair 클래스 정의
    public static class Wheelchair {
        @SerializedName("wheelchairId")
        private Long wheelchairId;

        @SerializedName("type")
        private String type;

        public Long getWheelchairId() {
            return wheelchairId;
        }

        public void setWheelchairId(Long wheelchairId) {
            this.wheelchairId = wheelchairId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    // RentalResponse.java
    public static class Institution {
        @SerializedName("name")
        private String name;

        @SerializedName("address")
        private String address;

        @SerializedName("telNumber")
        private String telNumber;

        @SerializedName("institutionCode") // JSON 응답의 institutionCode 필드
        private Long institutionCode;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTelNumber() {
            return telNumber;
        }

        public void setTelNumber(String telNumber) {
            this.telNumber = telNumber;
        }

        public Long getInstitutionCode() { // institutionCode Getter
            return institutionCode;
        }

        public void setInstitutionCode(Long institutionCode) { // institutionCode Setter
            this.institutionCode = institutionCode;
        }
    }


    // Getters and Setters for main RentalResponse
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Wheelchair getWheelchair() {
        return wheelchair;
    }

    public void setWheelchair(Wheelchair wheelchair) {
        this.wheelchair = wheelchair;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }
}
