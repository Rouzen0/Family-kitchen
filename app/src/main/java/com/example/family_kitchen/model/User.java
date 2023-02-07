package com.example.family_kitchen.model;

public class User {

    String Name,Email,Password,PhoneNumber,MaroofNumber,UserType,UserId,Longitude,Latitude,Category,Image,Available;

    public User(){}

    public User(String name, String email, String password, String phoneNumber, String maroofNumber, String userType, String userId, String longitude, String latitude, String category, String image, String available) {
        Name = name;
        Email = email;
        Password = password;
        PhoneNumber = phoneNumber;
        MaroofNumber = maroofNumber;
        UserType = userType;
        UserId = userId;
        Longitude = longitude;
        Latitude = latitude;
        Category = category;
        Image = image;
        Available = available;
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

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAvailable() {
        return Available;
    }

    public void setAvailable(String available) {
        Available = available;
    }
}
