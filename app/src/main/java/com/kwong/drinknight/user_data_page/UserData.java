package com.kwong.drinknight.user_data_page;

/**
 * Created by 锐锋 on 2017/8/25.
 */

public class UserData {

    String userName;
    String account;
    String gender;
    String phoneNumber;
    String password;
    String registerTime;
    int age;
    float height;
    float weight;
    String emailAddress;
    String imageName;

    String cup1ID;
    String cup2ID;
    String cup3ID;
    public String getCup1ID() {
        return cup1ID;
    }

    public void setCup1ID(String cup1ID) {
        this.cup1ID = cup1ID;
    }

    public void setCup2ID(String cup2ID) {
        this.cup2ID = cup2ID;
    }

    public void setCup3ID(String cup3ID) {
        this.cup3ID = cup3ID;
    }

    public String getCup2ID() {
        return cup2ID;
    }

    public String getCup3ID() {
        return cup3ID;
    }

    public UserData(){}
    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }



    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserName() {

        return userName;
    }

    public String getAccount() {
        return account;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public int getAge() {
        return age;
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}