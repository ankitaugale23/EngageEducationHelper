package com.example.educationhelper.Student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.educationhelper.CurrentUser;
import com.example.educationhelper.LoginActivity;
import com.example.educationhelper.R;
import com.example.educationhelper.ShowAnnouncement;

public class StudentHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_page);
    }

    public void onLogOut(View view) {
        //remove the user from the shared preferences
        new CurrentUser(StudentHomePage.this).removeUser();
        startActivity(new Intent(StudentHomePage.this, LoginActivity.class)); //redirect to login activity
        finish();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //if the user clicks the back button on the device
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(StudentHomePage.this);
        builder.setMessage("Are you sure want to exit from app?");
        builder.setCancelable(false);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //here exit app alert close............................................
    }
    //if user clicks on join channel button
    public void onJoinChannel(View view) {
        startActivity(new Intent(StudentHomePage.this, JoinChannelStudent.class));
        finish();
    }

    //if user clicks on show channels button
    public void onShowChannels(View view) {
        startActivity(new Intent(StudentHomePage.this, ShowChannelStudent.class));
        finish();
    }

    //if user clicks on join meeting button
    public void onJoinMeeting(View view) {
        startActivity(new Intent(StudentHomePage.this, JoinMeeting.class));
        finish();
    }

    //if user clicks on show announcements button
    public void onShowAnnouncements(View view) {
        startActivity(new Intent(StudentHomePage.this, ShowAnnouncement.class));
    }
}