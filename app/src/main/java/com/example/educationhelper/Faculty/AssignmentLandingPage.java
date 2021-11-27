package com.example.educationhelper.Faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.educationhelper.R;

public class AssignmentLandingPage extends AppCompatActivity {
    String channelCode,channelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_landing_page);
        channelCode = getIntent().getStringExtra("channelCode");
        channelName = getIntent().getStringExtra("channelName");

    }

    public void onCreateAssignment(View view) { //if the user clicks on "Create Assignment"
        Intent i = new Intent(AssignmentLandingPage.this, CreateAssignmentActivity.class);
        i.putExtra("channelCode",channelCode); //Pass the current channel code to the Assignment Creator
        startActivity(i);
        finish();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() { //if the user press the back button on the device
        Intent i = new Intent(AssignmentLandingPage.this, FacultyChannelComponents.class);
        i.putExtra("channelCode",channelCode);
        startActivity(i);
        finish();
    }

    public void onShowAssignmentList(View view) { //"Show Assignments" Clicked
        Intent i = new Intent(AssignmentLandingPage.this, ShowAssignment1.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("channelName",channelName);
        startActivity(i);
        finish();
    }
}