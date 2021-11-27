package com.example.educationhelper.FirebaseDataClass;

public class UploadStudentAssignment {
    String studentPhone, studentName, studentAssignment, status;

    public UploadStudentAssignment() {
    }

    public UploadStudentAssignment(String studentPhone, String studentName, String studentAssignment, String status) {
        this.studentPhone = studentPhone;
        this.studentName = studentName;
        this.studentAssignment = studentAssignment;
        this.status = status;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentAssignment() {
        return studentAssignment;
    }

    public void setStudentAssignment(String studentAssignment) {
        this.studentAssignment = studentAssignment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
