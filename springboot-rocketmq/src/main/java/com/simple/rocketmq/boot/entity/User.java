package com.simple.rocketmq.boot.entity;

public class User {

    private String userName;

    private Byte userAge;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Byte getUserAge() {
        return userAge;
    }

    public void setUserAge(Byte userAge) {
        this.userAge = userAge;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userAge=" + userAge +
                '}';
    }
}
