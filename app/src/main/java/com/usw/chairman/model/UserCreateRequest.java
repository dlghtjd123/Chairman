package com.usw.chairman.model;

public class UserCreateRequest {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String address;
    private boolean isAdmin;  // 관리자 모드 추가
    private boolean agreeTerms;
    private boolean agreePrivacy;
    private boolean agreeThirdParty;

    public UserCreateRequest(String email, String password, String name, String phoneNumber, String address, boolean isAdmin, boolean agreeTerms, boolean agreePrivacy, boolean agreeThirdParty) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.isAdmin = isAdmin;
        this.agreeTerms = agreeTerms;
        this.agreePrivacy = agreePrivacy;
        this.agreeThirdParty = agreeThirdParty;
    }

    // Getters and setters (생략 가능)
    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
