package com.example.educationhelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.educationhelper.Faculty.CreateAnnouncement;
import com.example.educationhelper.Student.ShowChannelStudent;
import com.example.educationhelper.Student.StudentHomePage;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;

public class ShowAnnouncement extends AppCompatActivity {

    //Declare required identifiers, data containers, and database references
    DatabaseReference groupRef;
    String currentUserName, currentTime, currentDate, currentUserId;
    TextView displayAnnouncement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_announcement);

        //Initialize the identifiers, data containers, and database references
        currentUserName = new CurrentUser(ShowAnnouncement.this).getName();
        currentUserId = new CurrentUser(ShowAnnouncement.this).getUsername();
        displayAnnouncement = findViewById(R.id.announcement_text);

        groupRef = FirebaseDatabase.getInstance().getReference().child("announcements");
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Fetch the previous announcements
        groupRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //if the previous announcements exists
                if(snapshot.exists())
                {
                    displayMessages(snapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //if the data is changed (New Announcement created) show the messages
                        if(snapshot.exists())
                        {
                            displayMessages(snapshot);
                        }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //show the announcements
    private void displayMessages(DataSnapshot snapshot) {
        Iterator iterator = snapshot.getChildren().iterator();

        while (iterator.hasNext())
        {

            String chatDate = (String) ( (DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ( (DataSnapshot)iterator.next()).getValue();
            String senderName = (String) ( (DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ( (DataSnapshot)iterator.next()).getValue();
            String senderNo = (String) ( (DataSnapshot)iterator.next()).getValue();

            displayAnnouncement.append("\n"+chatMessage +"\n("+chatTime+") "+chatDate+"\n----------------------------------------------------------------");


        }
    }


}