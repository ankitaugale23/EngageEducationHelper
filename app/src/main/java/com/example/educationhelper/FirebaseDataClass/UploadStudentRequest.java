package com.example.educationhelper.FirebaseDataClass;

public class UploadStudentRequest {
    String studentName, channelName, channelCode, facultyPhone, studentPhone, status;

    public UploadStudentRequest() {
    }

    public UploadStudentRequest(String studentName, String channelName, String channelCode, String facultyPhone, String studentPhone, String status) {
        this.studentName = studentName;
        this.channelName = channelName;
        this.channelCode = channelCode;
        this.facultyPhone = facultyPhone;
        this.studentPhone = studentPhone;
        this.status = status;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getFacultyPhone() {
        return facultyPhone;
    }

    public void setFacultyPhone(String facultyPhone) {
        this.facultyPhone = facultyPhone;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
