package com.example.digitaltoken;

public class User {

    String usersId;
    String userEmail;
    String userPhone;
    String userBusiness;
    String userLocation;
    String userName;

    public User() {

    }

    public User(String usersId, String userEmail, String userPhone, String userBusiness, String userLocation, String userName) {
        this.usersId = usersId;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userBusiness = userBusiness;
        this.userLocation = userLocation;
        this.userName = userName;
    }

    public String getUsersId() {
        return usersId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserBusiness() {
        return userBusiness;
    }

    public String getUserLocation() {
        return userLocation;
    }


    public String getUserName() {
        return userName;
    }
}
