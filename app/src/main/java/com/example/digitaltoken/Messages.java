package com.example.digitaltoken;

public class Messages {

    String counter;
    String timings;
    String notifications;

    public Messages() {
    }

    public Messages(String counter, String timings, String notifications) {
        this.counter = counter;
        this.timings = timings;
        this.notifications = notifications;
    }

    public String getCounter() {
        return counter;
    }

    public String getTimings() {
        return timings;
    }

    public String getNotifications() {
        return notifications;
    }
}
