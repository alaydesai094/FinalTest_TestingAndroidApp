package com.example.macstudent.parkingticket;

import android.app.Application;

import com.example.macstudent.parkingticket.model.User;

/**
 * Created by macstudent on 2018-04-20.
 */

public class MyApplication extends Application {

    User user;
    @Override
    public void onCreate() {
        super.onCreate();

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
