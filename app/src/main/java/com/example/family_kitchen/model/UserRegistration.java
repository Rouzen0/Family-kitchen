package com.example.family_kitchen.model;

public class UserRegistration {

    String Name,Email,Password,PhoneNumber,MaroofNumber,UserType,UserId;

    public UserRegistration(String name, String email, String password, String phoneNumber, String maroofNumber, String userType, String userId) {
        Name = name;
        Email = email;
        Password = password;
        PhoneNumber = phoneNumber;
        MaroofNumber = maroofNumber;
        UserType = userType;
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getMaroofNumber() {
        return MaroofNumber;
    }

    public void setMaroofNumber(String maroofNumber) {
        MaroofNumber = maroofNumber;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
