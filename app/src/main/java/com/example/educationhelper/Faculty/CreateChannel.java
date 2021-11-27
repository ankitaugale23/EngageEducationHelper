package com.example.educationhelper.Faculty;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.example.educationhelper.FirebaseDataClass.UploadChannel;
import com.example.educationhelper.LoginActivity;
import com.example.educationhelper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class CreateChannel extends AppCompatActivity {

    //Declare the required data, containers, database references
    String channelName, username, channelCode, count;
    DatabaseReference reference, historyChannel;
    EditText mChannelName;
    ListView mListView;
    ArrayList<String> myArrayList = new ArrayList<>();
    ArrayList<String> myArrayListCode = new ArrayList<>();
    long channelCountInt = 0;
    private final String log = "RESULT DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel);

        //Initialize the identifiers
        username = new CurrentUser(CreateChannel.this).getUsername();
        reference = FirebaseDatabase.getInstance().getReference("credentials")
                .child(username).child("channel");
        historyChannel = FirebaseDatabase.getInstance().getReference();

        mChannelName = findViewById(R.id.enterChannelName);
        mListView= findViewById(R.id.listview);

        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(CreateChannel.this, android.R.layout.simple_list_item_1, myArrayList);
        mListView.setAdapter(myArrayAdapter);

        ////////////////////////////////////////////////////////////////////////////////////////////

        historyChannel.child("channelCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    count = dataSnapshot.getValue().toString();
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i(log, String.valueOf(dataSnapshot.getValue()));

                Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                String value = data.get("channelName").toString();
                String value2 = data.get("channelCode").toString();
                myArrayList.add(value);
                myArrayListCode.add(value2);
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
                String currentSerialNo = myArrayListCode.get(position).toString();
                String channelName = myArrayList.get(position).toString();
                Intent i = new Intent(CreateChannel.this, FacultyChannelComponents.class);
                i.putExtra("channelCode",currentSerialNo);
                i.putExtra("channelName",channelName);
                startActivity(i);
                finish();
            }
        });

    }

    public void addSectorBtn(View view) {
        channelName = mChannelName.getText().toString().trim();
        if(channelName.equals("")){
            Toast.makeText(CreateChannel.this, "enter channel name", Toast.LENGTH_SHORT).show();
        }
        else {
            channelCountInt = Long.parseLong(count) + 1;
            channelCode = String.valueOf(channelCountInt);

            final UploadChannel u = new UploadChannel(channelCode, channelName);

            reference.child(channelCode).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    historyChannel.child("channelCount").setValue(channelCode).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(CreateChannel.this, "Channel Added Successfully..", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateChannel.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreateChannel.this, FacultyHomePage.class));
        finish();
    }
}