package com.example.chairman.model;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // 게터 및 세터
    public String getemail() { return email; }
    public String getPassword() { return password; }
}
