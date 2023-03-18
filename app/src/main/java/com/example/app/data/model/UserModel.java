package com.example.app.data.model;

import java.util.Date;

public class UserModel {
    public String email;
    public String username;
    public String firstName;
    public String lastName;
    public String phone;
    public String creditCard;
    public String avatar;
    public Double balance;
    public Integer premiumTier;

    public UserModel() {
    }

    public UserModel(String email, String username, String firstName, String lastName, String phone, String creditCard, String avatar, Double balance, Integer premiumTier) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.creditCard = creditCard;
        this.avatar = avatar;
        this.balance = balance;
        this.premiumTier = premiumTier;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getPremiumTier() {
        return premiumTier;
    }

    public void setPremiumTier(Integer premiumTier) {
        this.premiumTier = premiumTier;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", creditCard='" + creditCard + '\'' +
                ", avatar='" + avatar + '\'' +
                ", balance=" + balance +
                ", premiumTier=" + premiumTier +
                '}';
    }
}
