package com.example.educationhelper.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.educationhelper.CurrentUser;
import com.example.educationhelper.Faculty.CreateAssignmentActivity;
import com.example.educationhelper.FirebaseDataClass.UploadStudentRequest;
import com.example.educationhelper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinChannelStudent extends AppCompatActivity {

    //Declare all the required database references and UI components
    EditText mChannelCode, mFacultyNumber;
    String channelName, channelCode, facultyNumber, studentPhone, studentName;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_channel_student);

        //get the current user data and store it
        studentPhone = new CurrentUser(JoinChannelStudent.this).getUsername();
        studentName = new CurrentUser(JoinChannelStudent.this).getName();

        mChannelCode = findViewById(R.id.et_channelCode);
        mFacultyNumber = findViewById(R.id.et_facultyNumber);
    }

    public void onJoinChannel(View view) {

        //fetch the data user just have entered
        channelCode = mChannelCode.getText().toString().trim();
        facultyNumber = mFacultyNumber.getText().toString().trim();

        //if the user input is empty
        if(channelCode.equals("") || facultyNumber.equals("")){
            Toast.makeText(JoinChannelStudent.this, "Enter Details First.", Toast.LENGTH_SHORT).show();
        }
        //if the user input contains the values
        else {
            reference = FirebaseDatabase.getInstance().getReference("credentials"); //Initialize the database reference

            //access{Initialize the reference} the user's data in the respective channel
            reference.child(studentPhone).child("channel").child(channelCode).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //if the user is already a part of respective channel
                    if(snapshot.exists()){
                        Toast.makeText(JoinChannelStudent.this, "Already added in your list.", Toast.LENGTH_SHORT).show();
                    }
                    //if the user is not a part of respective channel
                    else {

                        reference.child(facultyNumber).child("channel")
                                .child(channelCode).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    channelName = snapshot.child("channelName").getValue().toString();
                                    final UploadStudentRequest u = new UploadStudentRequest(studentName, channelName, channelCode, facultyNumber, studentPhone,"pending");
                                    reference.child(facultyNumber).child("channel")
                                            .child(channelCode)
                                            .child("request").child(studentPhone).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            reference.child(studentPhone).child("channel").child(channelCode).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        Toast.makeText(JoinChannelStudent.this, "Already Joined to this channel", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        reference.child(studentPhone).child("channel").child(channelCode).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                startActivity(new Intent(JoinChannelStudent.this, StudentHomePage.class));
                                                                finish();
                                                                Toast.makeText(JoinChannelStudent.this, "Request Sent Successfully.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(JoinChannelStudent.this, "This channel is not available..", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //if user clicks the back button, redirect to student home page
    @Override
    public void onBackPressed() {
        startActivity(new Intent(JoinChannelStudent.this, StudentHomePage.class));
        finish();
    }
}