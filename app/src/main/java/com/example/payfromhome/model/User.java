package com.example.payfromhome.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

//import com.example.payfromhome.BR;

public class User extends BaseObservable {
    private String name;
    private String username;
    private String password;
    private int balance;

    public User() {
    }

    public User(String name, String username, String password, int balance) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
//        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
//        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
//        notifyPropertyChanged(BR.password);
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
