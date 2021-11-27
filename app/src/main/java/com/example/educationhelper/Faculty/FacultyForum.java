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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.educationhelper.CurrentUser;
import com.example.educationhelper.FirebaseAdapters.ListChatAdapter;
import com.example.educationhelper.FirebaseDataClass.UploadChat;
import com.example.educationhelper.R;
import com.example.educationhelper.Student.StudentForum;
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

public class FacultyForum extends AppCompatActivity {
    //Declare the required components, reference variables and UI Components
    Button sendMessageButton;
    EditText sendMessageTextField;
    String channelCode,channelName;

    DatabaseReference userRef,groupRef,groupMessageKeyRef;

    String currentUserName,currentUserId,curretDate,currentTime;

    ListView myListView;
    ArrayList<UploadChat> requestList;
    ListChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_forum);

        channelName = getIntent().getStringExtra("channelName");

        this.setTitle(channelName + " Forum");

        IntializeFields();
        //Fetch the current user info from shared preferences
        currentUserName = new CurrentUser(FacultyForum.this).getName(); //userInfo
        currentUserId = new CurrentUser(FacultyForum.this).getUsername(); //userInfo
        channelCode = getIntent().getStringExtra("channelCode");

        //Initialize the database references
        userRef = FirebaseDatabase.getInstance().getReference().child("credentials");
        groupRef = userRef.child(currentUserId).child("channel").child(channelCode).child("forum");

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

        //Fetch all the previous chats
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();

                for (DataSnapshot employeeDatasnap : snapshot.getChildren()) {
                    UploadChat request = employeeDatasnap.getValue(UploadChat.class);

                    requestList.add(request);

                }
                adapter = new ListChatAdapter(FacultyForum.this, requestList);
                myListView.setAdapter(adapter);
                myListView.setSelection(adapter.getCount()-1);

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

        if(TextUtils.isEmpty(message)) //if the message field is empty
        {
            Toast.makeText(FacultyForum.this, "Please enter the message..", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Get and store the data into the variables
            Calendar callForDate = Calendar.getInstance();
            SimpleDateFormat curretDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            curretDate = curretDateFormat.format(callForDate.getTime());

            Calendar callForTime = Calendar.getInstance();
            SimpleDateFormat curretTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = curretTimeFormat.format(callForTime.getTime());

            HashMap<String,Object> groupMessageKey = new HashMap<>();
            groupRef.updateChildren(groupMessageKey);

            groupMessageKeyRef = groupRef.child(messageKey); //point to the unique mesage key

            //Store the data fetched to the database under the unique message key
            final UploadChat uploadChat = new UploadChat(currentUserName, currentUserId, message, curretDate, currentTime);

            groupMessageKeyRef.setValue(uploadChat);
        }
    }

    private void displayMessages(DataSnapshot snapshot) { //show chats

        Iterator iterator = snapshot.getChildren().iterator();

        while (iterator.hasNext())
        {

            String chatDate = (String) ( (DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ( (DataSnapshot)iterator.next()).getValue();
            String senderName = (String) ( (DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ( (DataSnapshot)iterator.next()).getValue();
            String senderNo = (String) ( (DataSnapshot)iterator.next()).getValue();

        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //if the user press back button on the device
    @Override
    public void onBackPressed() {
        Intent i = new Intent(FacultyForum.this, FacultyChannelComponents.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("channelName",channelName);
        startActivity(i);
        finish();
    }
}