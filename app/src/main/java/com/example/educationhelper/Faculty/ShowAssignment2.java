package com.example.educationhelper.Faculty;

//This activity is responsible for showing the selected assignment and submitted assignment (for faculty) and view
//our own submitted assignment for the current assignment(For student)

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.educationhelper.CurrentUser;
import com.example.educationhelper.R;
import com.example.educationhelper.ViewPdfActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class ShowAssignment2 extends AppCompatActivity {
    String channelCode, assignmentCount, URL, username, assignmentNameStaff;
    DatabaseReference reference;
    ListView mListView;
    ArrayList<String> myArrayList = new ArrayList<>();
    ArrayList<String> myArrayListURL = new ArrayList<>();
    long channelCountInt = 0;
    private final String log = "RESULT DATA 2nd Page";
    TextView tvAssignmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_assignment2);
        username = new CurrentUser(ShowAssignment2.this).getUsername();
        channelCode = getIntent().getStringExtra("channelCode");
        assignmentCount = getIntent().getStringExtra("assignmentCount");
        assignmentNameStaff = getIntent().getStringExtra("assignmentNameStaff");
        URL = getIntent().getStringExtra("URL");

        reference = FirebaseDatabase.getInstance().getReference("credentials").child(username).child("channel")
                .child(channelCode).child("assignments").child(assignmentCount);

        mListView= findViewById(R.id.listview);
        tvAssignmentName = findViewById(R.id.tv_assignmentName);

        tvAssignmentName.setText(assignmentNameStaff);

        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(ShowAssignment2.this, android.R.layout.simple_list_item_1, myArrayList);
        mListView.setAdapter(myArrayAdapter);


        reference.orderByChild("status").equalTo("submitted").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i(log, String.valueOf(dataSnapshot.getValue()));

                Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                String value = data.get("studentName").toString();
                String value2 = data.get("studentAssignment").toString();
                myArrayList.add(value);
                myArrayListURL.add(value2);
                myArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String URLStudent = myArrayListURL.get(position).toString();
                Intent intent = new Intent(ShowAssignment2.this, ViewPdfActivity.class);
                intent.putExtra("url", URLStudent);
                startActivity(intent);
            }
        });

    }

    public void onOpenAssignment(View view) {
        Intent intent = new Intent(ShowAssignment2.this, ViewPdfActivity.class);
        intent.putExtra("url", URL);
        startActivity(intent);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        Intent i = new Intent(ShowAssignment2.this, ShowAssignment1.class);
        i.putExtra("channelCode",channelCode);
        startActivity(i);
        finish();
    }
}