package com.example.educationhelper.Faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.educationhelper.CurrentUser;
import com.example.educationhelper.FirebaseDataClass.UploadChat;
import com.example.educationhelper.R;
import com.example.educationhelper.Student.JoinMeeting;
import com.example.educationhelper.Student.StudentHomePage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CreateMeeting extends AppCompatActivity {
    EditText etMeetingCode;
    String meetingCode;
    String channelCode, channelName, currentUserId,currentUserName;
    DatabaseReference userRef,groupRef, groupMessageKeyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        //fetch the channel code and channel name passed from showChannel Activity
        channelCode = getIntent().getStringExtra("channelCode");
        channelName = getIntent().getStringExtra("channelName");
        etMeetingCode = findViewById(R.id.et_meetingCode);
        currentUserId = new CurrentUser(CreateMeeting.this).getUsername(); //userInfo
        currentUserName = new CurrentUser(CreateMeeting.this).getName(); //userInfo

        userRef = FirebaseDatabase.getInstance().getReference().child("credentials");
        groupRef = userRef.child(currentUserId).child("channel").child(channelCode).child("forum");
    }

    public void onCreateMeeting(View view) {
        meetingCode = etMeetingCode.getText().toString().trim(); //get the entered meeting code from user

        if(meetingCode.equals("")){ //if the meeting code field is empty
            Toast.makeText(CreateMeeting.this, "Enter Meeting Code First", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                //create new meeting using provided meeting code

                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                        .setServerURL(new URL("https://meet.jit.si"))
                        .setRoom("educationHelper"+meetingCode)
                        .setAudioMuted(true)
                        .setVideoMuted(true)
                        .setAudioOnly(false)
                        .setWelcomePageEnabled(true)
                        .build();
                JitsiMeetActivity.launch(CreateMeeting.this, options);

                setInForum();
            } catch (MalformedURLException e) { //if the error occurs
                e.printStackTrace();
            }
        }
    }

    private void setInForum() {

        String messageKey = groupRef.push().getKey();

        //Get and store the data into the variables
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat curretDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String curretDate = curretDateFormat.format(callForDate.getTime());

        Calendar callForTime = Calendar.getInstance();
        SimpleDateFormat curretTimeFormat = new SimpleDateFormat("hh:mm a");
        String currentTime = curretTimeFormat.format(callForTime.getTime());

        HashMap<String,Object> groupMessageKey = new HashMap<>();
        groupRef.updateChildren(groupMessageKey);

        groupMessageKeyRef = groupRef.child(messageKey); //point to the unique mesage key

        //Store the data fetched to the database under the unique message key
        final UploadChat uploadChat = new UploadChat(currentUserName, currentUserId, "Meeting code for lecture is : "+meetingCode, curretDate, currentTime);

        groupMessageKeyRef.setValue(uploadChat);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    //if the user press back button on the device
    @Override
    public void onBackPressed() {
        Intent i = new Intent(CreateMeeting.this, FacultyChannelComponents.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("channelName",channelName);
        startActivity(i);
        finish();
    }
}