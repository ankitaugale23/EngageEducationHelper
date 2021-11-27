package com.example.educationhelper.FirebaseDataClass;

public class UploadTeacherRegistration {
    String name, email, password, mobile, loginId;

    public UploadTeacherRegistration() {
    }

    public UploadTeacherRegistration(String name, String email, String password, String mobile, String loginId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.loginId = loginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}