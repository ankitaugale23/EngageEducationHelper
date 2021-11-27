package com.example.educationhelper.Student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.educationhelper.CurrentUser;
import com.example.educationhelper.Faculty.RequestFromStudent;
import com.example.educationhelper.FirebaseAdapters.ListChatAdapter;
import com.example.educationhelper.FirebaseAdapters.ListStudentRequestAdapter;
import com.example.educationhelper.FirebaseDataClass.UploadChat;
import com.example.educationhelper.FirebaseDataClass.UploadStudentRequest;
import com.example.educationhelper.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class StudentForum extends AppCompatActivity {
    Button sendMessageButton;
    EditText sendMessageTextField;
    String channelCode,channelName;
    DatabaseReference userRef,groupRef,groupMessageKeyRef;
    String currentUserName,currentUserId,curretDate,currentTime, facultyPhone;

    ListView myListView;
    ArrayList<UploadChat> requestList;
    ListChatAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_forum);
        IntializeFields();

        currentUserName = new CurrentUser(StudentForum.this).getName(); //userInfo
        currentUserId = new CurrentUser(StudentForum.this).getUsername(); //userInfo
        facultyPhone = getIntent().getStringExtra("facultyPhone"); //userInfo
        channelCode = getIntent().getStringExtra("channelCode");
        channelName = getIntent().getStringExtra("channelName");

        this.setTitle(channelName + " Forum");

        userRef = FirebaseDatabase.getInstance().getReference().child("credentials");
        groupRef = userRef.child(facultyPhone).child("channel").child(channelCode).child("forum");

        myListView = findViewById(R.id.myListView);
        requestList = new ArrayList<>();
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessageInfoToDataBase();

                sendMessageTextField.setText("");

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();

                for (DataSnapshot employeeDatasnap : snapshot.getChildren()) {
                    UploadChat request = employeeDatasnap.getValue(UploadChat.class);

                    requestList.add(request); //fetch and store the existing messages in the database

                }
                //set the adapter to bridge the data to listview
                adapter = new ListChatAdapter(StudentForum.this, requestList);
                myListView.setAdapter(adapter);
                myListView.setSelection(adapter.getCount()-1); //set focus to the latest message

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void IntializeFields() {
        sendMessageButton  = findViewById(R.id.send_message_button);
        sendMessageTextField = findViewById(R.id.input_group_message);
    }

    private void saveMessageInfoToDataBase() {

        String message = sendMessageTextField.getText().toString();
        String messageKey = groupRef.push().getKey();

        if(TextUtils.isEmpty(message))
        {
            Toast.makeText(StudentForum.this, "Please enter the message..", Toast.LENGTH_SHORT).show();
        }
        else
        {

            Calendar callForDate = Calendar.getInstance();
            SimpleDateFormat curretDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            curretDate = curretDateFormat.format(callForDate.getTime());

            Calendar callForTime = Calendar.getInstance();
            SimpleDateFormat curretTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = curretTimeFormat.format(callForTime.getTime());

            HashMap<String,Object> groupMessageKey = new HashMap<>();
            groupRef.updateChildren(groupMessageKey);

            groupMessageKeyRef = groupRef.child(messageKey);

            final UploadChat uploadChat = new UploadChat(currentUserName, currentUserId, message, curretDate, currentTime);

            groupMessageKeyRef.setValue(uploadChat);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        Intent i = new Intent(StudentForum.this, StudentChannelComponents.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("facultyPhone",facultyPhone);
        i.putExtra("channelName",channelName);
        startActivity(i);
        finish();
    }
}