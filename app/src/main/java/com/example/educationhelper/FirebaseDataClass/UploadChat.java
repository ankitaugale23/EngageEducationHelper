package com.example.educationhelper.FirebaseDataClass;

public class UploadChat {
    String name,userid, message,date, time;

    public UploadChat() {
    }

    public UploadChat(String name, String userid, String message, String date, String time) {
        this.name = name;
        this.userid = userid;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
