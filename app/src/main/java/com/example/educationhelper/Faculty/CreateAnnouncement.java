package com.example.educationhelper.Faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.educationhelper.CurrentUser;
import com.example.educationhelper.R;
import com.example.educationhelper.ShowAnnouncement;
import com.example.educationhelper.Student.StudentForum;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.InternalTokenProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class CreateAnnouncement extends AppCompatActivity {
    Button postButton;
    EditText announcement;
    TextView displayAnnouncement;

    DatabaseReference userRef, groupRef, groupMessageKeyRef;

    String currentUserName, currentTime, currentDate, currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);

        IntializeFields();

        currentUserName = new CurrentUser(CreateAnnouncement.this).getName();
        currentUserId = new CurrentUser(CreateAnnouncement.this).getUsername();

        userRef =  FirebaseDatabase.getInstance().getReference().child("credentials"); //User reference from the database to fetch required user info
        groupRef = FirebaseDatabase.getInstance().getReference().child("announcements"); //Reference to announcement section in the database

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnnouncementInfoToDatabase();

                announcement.setText("");

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }






    private void IntializeFields() {
        postButton = findViewById(R.id.post_announcement_button);
        announcement = findViewById(R.id.input_announcement);
        displayAnnouncement = findViewById(R.id.announcement_text);
    }

    private void saveAnnouncementInfoToDatabase() {
        String announcementText = announcement.getText().toString();
        String announcementKey = groupRef.push().getKey(); //get a unique key

        if(TextUtils.isEmpty(announcementText)) //if the user leaves the field empty
        {
            Toast.makeText(CreateAnnouncement.this, "Please enter the Announcement", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Collect and store all the current and required data
            Calendar callForDate = Calendar.getInstance();
            SimpleDateFormat curretDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = curretDateFormat.format(callForDate.getTime());

            Calendar callForTime = Calendar.getInstance();
            SimpleDateFormat curretTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = curretTimeFormat.format(callForTime.getTime());

            HashMap<String,Object> groupMessageKey = new HashMap<>();
            groupRef.updateChildren(groupMessageKey); //Update the info in the database to where the reference is pointing

            groupMessageKeyRef = groupRef.child(announcementKey); //Reference to a particular message

            HashMap<String,Object> messageInfoMap = new HashMap<>();

            messageInfoMap.put("name",currentUserName);
            messageInfoMap.put("userid",currentUserId);
            messageInfoMap.put("message",announcementText);
            messageInfoMap.put("date",currentDate);
            messageInfoMap.put("time",currentTime);

            groupMessageKeyRef.updateChildren(messageInfoMap).addOnSuccessListener(new OnSuccessListener<Void>() { //Store the message into the database
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(CreateAnnouncement.this, "Announcement added successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateAnnouncement.this, ShowAnnouncement.class));
                }
            });
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    //if the user press the back button on the device
    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreateAnnouncement.this, FacultyHomePage.class));
        finish();
    }
}