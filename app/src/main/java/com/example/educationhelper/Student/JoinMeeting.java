package com.example.educationhelper.Student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.educationhelper.Faculty.CreateMeeting;
import com.example.educationhelper.R;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class JoinMeeting extends AppCompatActivity {

    EditText etMeetingCode;
    String meetingCode, channelName, username, channelCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_meeting);

        etMeetingCode = findViewById(R.id.et_meetingCode);

        channelName = getIntent().getStringExtra("channelName");
        username = getIntent().getStringExtra("facultyPhone");
        channelCode = getIntent().getStringExtra("channelCode");
    }

    public void onCreateMeeting(View view) {
        meetingCode = etMeetingCode.getText().toString().trim();

        if(meetingCode.equals("")){
            Toast.makeText(JoinMeeting.this, "Enter Meeting Code First", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                        .setServerURL(new URL("https://meet.jit.si"))
                        .setRoom("educationHelper"+meetingCode)
                        .setAudioMuted(true)
                        .setVideoMuted(true)
                        .setAudioOnly(false)
                        .setWelcomePageEnabled(false)
                        .build();
                JitsiMeetActivity.launch(JoinMeeting.this, options);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        Intent i = new Intent(JoinMeeting.this, StudentChannelComponents.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("facultyPhone",username);
        i.putExtra("channelName",channelName);
        startActivity(i);
        finish();
    }
}