package com.example.educationhelper.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.educationhelper.FirebaseDataClass.UploadTeacherRegistration;
import com.example.educationhelper.LoginActivity;
import com.example.educationhelper.R;
import com.example.educationhelper.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterFacultyActivity extends AppCompatActivity {
    DatabaseReference reference;
    EditText sName, sEmail, sPass, sMobile;
    String name, email, password, mobile, loginId="2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_faculty);

        reference = FirebaseDatabase.getInstance().getReference("credentials");

        sName = findViewById(R.id.et_name);
        sEmail = findViewById(R.id.et_email);
        sPass = findViewById(R.id.et_password);
        sMobile = findViewById(R.id.et_mobile);

    }

    public void onClickRegister(View view) {
        name = sName.getText().toString().trim();
        email = sEmail.getText().toString().trim();
        password = sPass.getText().toString().trim();
        mobile = sMobile.getText().toString().trim();

        if(name.equals("") || email.equals("") || password.equals("") || mobile.equals("")){
            Toast.makeText(RegisterFacultyActivity.this, "Complete all details first.", Toast.LENGTH_SHORT).show();
        }
        else if(mobile.length()<10){
            sMobile.setError("Enter 10 digits");
        }
        else {
            final UploadTeacherRegistration upload = new UploadTeacherRegistration(name,email,password,mobile,loginId);

            reference.child(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Toast.makeText(RegisterFacultyActivity.this, "Can't Use same mobile number", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        reference.child(mobile).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(RegisterFacultyActivity.this, AdminHomepage.class));
                                finish();
                                Toast.makeText(RegisterFacultyActivity.this, "User Register Successful.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterFacultyActivity.this, AdminHomepage.class));
        finish();
    }
}