package com.example.educationhelper.Faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.example.educationhelper.CurrentUser;
import com.example.educationhelper.FirebaseAdapters.ListStudentRequestAdapter;
import com.example.educationhelper.FirebaseDataClass.UploadStudentRequest;
import com.example.educationhelper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestFromStudent extends AppCompatActivity implements TextWatcher {

    //Declare required identifiers, data containers, and database references
    String channelCode, username,channelName;
    DatabaseReference reference;
    ListView myListView;
    ArrayList<UploadStudentRequest> requestList;
    ListStudentRequestAdapter adapter;
    EditText searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_from_student);

        //Initialize the identifiers, data containers, and database references
        channelCode = getIntent().getStringExtra("channelCode");
        username = new CurrentUser(RequestFromStudent.this).getUsername();

        channelName = getIntent().getStringExtra("channelName");

        reference = FirebaseDatabase.getInstance().getReference().child("credentials")
                .child(username).child("channel").child(channelCode).child("request");
        myListView = findViewById(R.id.myListView);
        searchView = findViewById(R.id.searchView);
        searchView.addTextChangedListener(this);
        requestList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear(); //clear the old request list

                for (DataSnapshot employeeDatasnap : snapshot.getChildren()) {
                    UploadStudentRequest request = employeeDatasnap.getValue(UploadStudentRequest.class);

                        requestList.add(request); //if request from student is present add it to the request list

                }
                adapter = new ListStudentRequestAdapter(RequestFromStudent.this, requestList);
                myListView.setAdapter(adapter); //show the requests

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    //if the user press back button on the device
    @Override
    public void onBackPressed() {
        Intent i = new Intent(RequestFromStudent.this, FacultyChannelComponents.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("channelName",channelName);
        startActivity(i);
        finish();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        this.adapter.getFilter().filter(s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}