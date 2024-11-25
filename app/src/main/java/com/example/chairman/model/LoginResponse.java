package com.example.chairman.model;

public class LoginResponse {
    private String jwtToken;
    private Institution institution;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public class Institution {
        private Long institutionId;
        private Long institutionCode;
        private String name;
        private String telNumber;
        private String address;

        // Getter and Setter
        public Long getInstitutionCode() {
            return institutionCode;
        }

        public void setInstitutionCode(Long institutionCode) {
            this.institutionCode = institutionCode;
        }
    }
}
