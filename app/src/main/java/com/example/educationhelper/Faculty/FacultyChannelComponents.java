package com.example.educationhelper.Faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.educationhelper.R;

public class FacultyChannelComponents extends AppCompatActivity {
    String channelCode, channelName;
    TextView channelCodeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_channel_components);

        //fetch the channel code and channel name passed from showChannel Activity
        channelCode = getIntent().getStringExtra("channelCode");
        channelName = getIntent().getStringExtra("channelName");
        channelCodeText = findViewById(R.id.tv_ChannelCode);

        channelCodeText.setText("Channel Code: "+channelCode);
    }

    @Override
    public void onBackPressed() { //if the user press back button the device
        startActivity(new Intent(FacultyChannelComponents.this, CreateChannel.class));
        finish();
    }

    public void onAssignments(View view) { //if the user requests to show assignment
        Intent i = new Intent(FacultyChannelComponents.this, AssignmentLandingPage.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("channelName",channelName);
        startActivity(i);
        finish();
    }

    public void onRequestsFaculty(View view) { //if user wants to see pending requests of the students to join channel
        Intent i = new Intent(FacultyChannelComponents.this, RequestFromStudent.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("channelName",channelName);
        startActivity(i);
        finish();
    }

    public void onFacultyForum(View view) { //if the user wants to go to the dedicated forum for the respective channel
        Intent i = new Intent(FacultyChannelComponents.this, FacultyForum.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("channelName",channelName);
        startActivity(i);
        finish();
    }

    public void onCreateMeeting(View view) {
        Intent i = new Intent(FacultyChannelComponents.this, CreateMeeting.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("channelName",channelName);
        startActivity(i);
        finish();
    }
}