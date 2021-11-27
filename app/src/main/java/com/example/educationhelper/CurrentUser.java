package com.example.educationhelper;

import android.content.Context;
import android.content.SharedPreferences;

//This class stores the info of the current user from context and stores it into the shared preferences
//for future use.

public class CurrentUser {
    private String username, pass, name;
    Context context;
    SharedPreferences sharedPreferences;

    public CurrentUser(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("login_details", Context.MODE_PRIVATE);
    }

    public String getUsername() {
        username = sharedPreferences.getString("username", "");
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        sharedPreferences.edit().putString("username", username).commit();
    }

    public String getPass() {
        pass = sharedPreferences.getString("pass", "");
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
        sharedPreferences.edit().putString("pass", pass).commit();
    }

    public String getName() {
        name = sharedPreferences.getString("name", "");
        return name;
    }

    public void setName(String name) {
        this.name = name;
        sharedPreferences.edit().putString("name", name).commit();    }

    public void removeUser() {
        sharedPreferences.edit().clear().commit();

    }
}