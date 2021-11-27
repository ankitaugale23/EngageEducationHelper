package com.example.educationhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.educationhelper.Admin.AdminHomepage;
import com.example.educationhelper.Admin.RegisterFacultyActivity;
import com.example.educationhelper.FirebaseDataClass.UploadStudentRegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    //Declare required identifiers, data containers, and database references
    DatabaseReference reference;
    EditText sEnrollNo, sName, sEmail, sPass, sMobile;
    String enrollmentNo, name, email, password, mobile, loginId="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize the identifiers, data containers, and database references
        reference = FirebaseDatabase.getInstance().getReference("credentials");

        sEnrollNo = findViewById(R.id.et_enrollment);
        sName = findViewById(R.id.et_name);
        sEmail = findViewById(R.id.et_email);
        sPass = findViewById(R.id.et_password);
        sMobile = findViewById(R.id.et_mobile);
    }

    public void onClickRegister(View view) { //if the user clicks "Register"

        //Fetch the data entered by user and store it
        enrollmentNo = sEnrollNo.getText().toString().trim();
        name = sName.getText().toString().trim();
        email = sEmail.getText().toString().trim();
        password = sPass.getText().toString().trim();
        mobile = sMobile.getText().toString().trim();

        final UploadStudentRegistration upload = new UploadStudentRegistration(enrollmentNo,name,email,password,mobile,loginId);

        //if the details are not entered by students
        if(enrollmentNo.equals("") || name.equals("") || email.equals("") || password.equals("") || mobile.equals("")){
            Toast.makeText(RegisterActivity.this, "Complete all details first.", Toast.LENGTH_SHORT).show();
        }
        else if(mobile.length()<10){
            sMobile.setError("Enter 10 digits");
        }
        else {
            //add the user into the database under his/her mobile no. root
            reference.child(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Toast.makeText(RegisterActivity.this, "Can't Use same mobile number", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        reference.child(mobile).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                                Toast.makeText(RegisterActivity.this, "User Register Successful.", Toast.LENGTH_SHORT).show();
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

    //if the user press back button
    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}