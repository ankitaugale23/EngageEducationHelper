package com.example.educationhelper.FirebaseDataClass;

public class UploadChannel {
    String channelCode, channelName;

    public UploadChannel() {
    }

    public UploadChannel(String channelCode, String channelName) {
        this.channelCode = channelCode;
        this.channelName = channelName;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return channelName;
    }
}
