package com.usw.chairman.model;

public class InstitutionData {
    private Long id;
    private String name;
    private Long institutionCode;
    private String address;
    private String telNumber;

    // Getter와 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(Long institutionCode) { this.institutionCode = institutionCode; }

    public String getTelNumber() { return telNumber; }

    public void setTelNumber(String telNumber) { this.telNumber = telNumber; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }
}
