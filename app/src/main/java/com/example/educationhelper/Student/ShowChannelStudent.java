package com.example.educationhelper.Student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.educationhelper.CurrentUser;
import com.example.educationhelper.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class ShowChannelStudent extends AppCompatActivity {
    DatabaseReference reference;
    String studentPhone, facultyPhone;
    EditText mChannelName;
    ListView mListView;
    ArrayList<String> myArrayList = new ArrayList<>();
    ArrayList<String> myArrayListCode = new ArrayList<>();
    ArrayList<String> myArrayListFacultyPhone = new ArrayList<>();
    private final String log = "RESULT DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_channel_student);

        studentPhone = new CurrentUser(ShowChannelStudent.this).getUsername();
        reference = FirebaseDatabase.getInstance().getReference("credentials").child(studentPhone).child("channel");

        mListView= findViewById(R.id.listview);

        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(ShowChannelStudent.this, android.R.layout.simple_list_item_1, myArrayList);
        mListView.setAdapter(myArrayAdapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i(log, String.valueOf(dataSnapshot.getValue()));

                Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                String value = data.get("channelName").toString();
                String value2 = data.get("channelCode").toString();
                String value4 = data.get("facultyPhone").toString();
                String value3 = data.get("status").toString();

                if(value3.equals("accepted")){
                    myArrayList.add(value);
                    myArrayListCode.add(value2);
                    myArrayListFacultyPhone.add(value4);
                    myArrayAdapter.notifyDataSetChanged();
                }
                else{
                }
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
                String currentSerialNo = myArrayListCode.get(position).toString();
                String channelName = myArrayList.get(position).toString();
                facultyPhone = myArrayListFacultyPhone.get(position).toString();
                Intent i = new Intent(ShowChannelStudent.this, StudentChannelComponents.class);
                i.putExtra("channelCode",currentSerialNo);
                i.putExtra("facultyPhone",facultyPhone);
                i.putExtra("channelName",channelName);
                startActivity(i);
                finish();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ShowChannelStudent.this, StudentHomePage.class));
        finish();
    }
}