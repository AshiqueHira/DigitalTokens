package com.example.digitaltoken;

public class Message {
    String mUserId;
    String mTime;
    String mCount;
    String mNotificaton;

    public Message() {

    }

    public Message(String mUserId, String mTime, String mCount, String mNotificaton) {
        this.mUserId = mUserId;
        this.mTime = mTime;
        this.mCount = mCount;
        this.mNotificaton = mNotificaton;
    }

    public String getmUserId() {
        return mUserId;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmCount() {
        return mCount;
    }

    public String getmNotificaton() {
        return mNotificaton;
    }
}
