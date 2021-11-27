package com.example.educationhelper.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.educationhelper.R;

public class AdminHomepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);
    }

    public void onClickRegisterFaculty(View view) {
        startActivity(new Intent(AdminHomepage.this, RegisterFacultyActivity.class));
        finish();
    }
}