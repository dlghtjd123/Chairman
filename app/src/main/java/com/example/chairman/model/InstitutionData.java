package com.example.chairman.model;

public class InstitutionData {
    private Long id;
    private String name;
    private Long institutionCode;

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

    public void setInstitutionCode(Long institutionCode) {
        this.institutionCode = institutionCode;
    }
}
