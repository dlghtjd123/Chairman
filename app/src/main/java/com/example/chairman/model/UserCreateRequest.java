package com.example.chairman.model;

public class UserCreateRequest {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String address;
    private boolean isAdmin;  // 관리자 모드 추가

    public UserCreateRequest(String email, String password, String name, String phoneNumber, String address, boolean isAdmin) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.isAdmin = isAdmin;
    }

    // Getters and setters (생략 가능)
    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
