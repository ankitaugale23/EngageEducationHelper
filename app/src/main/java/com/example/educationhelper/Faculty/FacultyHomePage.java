package com.example.educationhelper.Faculty;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.example.educationhelper.CurrentUser;
import com.example.educationhelper.LoginActivity;
import com.example.educationhelper.R;
import com.example.educationhelper.ShowAnnouncement;

public class FacultyHomePage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home_page);
    }

    public void onLogOut(View view) { //if the user wants to logout
        new CurrentUser(FacultyHomePage.this).removeUser(); //remove user from shared preferences
        startActivity(new Intent(FacultyHomePage.this, LoginActivity.class));
        finish();
    }

    public void onCreateChannel(View view) { //if the user wants to create channel.
        //Redirect and start appropriate activity
        startActivity(new Intent(FacultyHomePage.this, CreateChannel.class));
        finish();
    }

    public void onCreateAnnouncement(View view) { //if the user wants to create an announcements
        //Redirect and start appropriate activity
        startActivity(new Intent(FacultyHomePage.this,CreateAnnouncement.class));
        finish();
    }

    public void onShowAnnouncements(View view) { //if the user wants to view the announcements
        //Redirect and start appropriate activity
        startActivity(new Intent(FacultyHomePage.this, ShowAnnouncement.class));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    //if the user press back button on the device
    @Override
    public void onBackPressed() {

        //show alert
        final AlertDialog.Builder builder = new AlertDialog.Builder(FacultyHomePage.this);
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

    public void onCreateMeeting(View view) {
        startActivity(new Intent(FacultyHomePage.this, CreateMeeting.class));
        finish();
    }



}