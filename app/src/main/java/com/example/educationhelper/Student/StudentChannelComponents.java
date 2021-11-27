package com.example.educationhelper.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.educationhelper.CurrentUser;
import com.example.educationhelper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentChannelComponents extends AppCompatActivity {
    String facultyPhone, channelCode, channelName, studentPhone;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_channel_components);
        channelCode = getIntent().getStringExtra("channelCode");
        facultyPhone = getIntent().getStringExtra("facultyPhone");
        channelName = getIntent().getStringExtra("channelName");
        studentPhone = new CurrentUser(StudentChannelComponents.this).getUsername();

        reference = FirebaseDatabase.getInstance().getReference("credentials")
                .child(studentPhone).child("channel");
    }

    public void onLeaveChannel(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Leaving Channel")
                .setMessage("Are you sure you want to leave this channel?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    reference.child(channelCode).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(StudentChannelComponents.this, "Left channel successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(StudentChannelComponents.this, StudentHomePage.class));
                            finish();
                        }
                    });

                    }
                }).setNegativeButton("No", null).show();
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(StudentChannelComponents.this, ShowChannelStudent.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("facultyPhone",facultyPhone);
        startActivity(i);
        finish();
    }

    public void onStudentForum(View view) {
        Intent i = new Intent(StudentChannelComponents.this, StudentForum.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("facultyPhone",facultyPhone);
        i.putExtra("channelName",channelName);
        startActivity(i);
        finish();
    }

    public void onShowAssignments(View view) {
        Intent i = new Intent(StudentChannelComponents.this, StudentShowAssignment.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("facultyPhone",facultyPhone);
        i.putExtra("channelName",channelName);
        startActivity(i);
        finish();
    }

    public void onJoinChannel(View view) {
        Intent i = new Intent(StudentChannelComponents.this, JoinMeeting.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("facultyPhone",facultyPhone);
        i.putExtra("channelName",channelName);
        startActivity(i);
        finish();
    }
}