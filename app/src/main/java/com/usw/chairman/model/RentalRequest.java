package com.usw.chairman.model;

public class RentalRequest {
    private Long institutionCode;
    private String email;
    private String wheelchairType; // 예: "ADULT", "CHILD"
    private String rentalDate; // ISO 형식: "yyyy-MM-ddTHH:mm:ss"
    private String returnDate; // ISO 형식: "yyyy-MM-ddTHH:mm:ss"

    public Long getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(Long institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getWheelchairType() {
        return wheelchairType;
    }

    public void setWheelchairType(String wheelchairType) {
        this.wheelchairType = wheelchairType;
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

}
