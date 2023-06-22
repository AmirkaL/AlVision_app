package com.example.alinvision.Models;

public class User {
    private String email, password, name, cameraID;

//    пустой конструктор для экземпляра User
    public User() {}


    public User(String email, String password, String name, String cameraID) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.cameraID = cameraID;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCameraID() {
        return cameraID;
    }

    public void setCameraID(String cameraID) {
        this.cameraID = cameraID;
    }
}
