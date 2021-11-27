package com.example.educationhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.educationhelper.Admin.AdminHomepage;
import com.example.educationhelper.Faculty.FacultyHomePage;
import com.example.educationhelper.Student.StudentHomePage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    TextView register;
    String username,passowrd, loginId, name;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkSession(); //call if the user is already logged in in the past on this device

        //All required fields initialized

        reference = FirebaseDatabase.getInstance().getReference("credentials");
        register = findViewById(R.id.tv_register);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);


        //Invokes if the user clicks "Register Now"
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)); //open the registration form
                finish();
            }
        });
    }

    private void checkSession() {
        if (new CurrentUser(LoginActivity.this).getPass().equals("1")) { //if the user is student i.e. (LoginId: 1)
            startActivity(new Intent(LoginActivity.this, StudentHomePage.class));
            finish();
        } else if (new CurrentUser(LoginActivity.this).getPass().equals("2")){ //if the user is faculty i.e. (LoginId: 2)

            startActivity(new Intent(LoginActivity.this, FacultyHomePage.class));
            finish();
        }
        else {}
    }

    //login functionality
    public void onClickLogin(View view) {
        username = etUsername.getText().toString().trim();
        passowrd = etPassword.getText().toString().trim();


        if(username.equals("000") && passowrd.equals("123")){ //if the user is admin start the admin activity
            Toast.makeText(LoginActivity.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, AdminHomepage.class));
        }else {
            //check for the existence of data in database
            reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){ //if the user exists

                        loginId = snapshot.child("loginId").getValue().toString();
                        name = snapshot.child("name").getValue().toString();
                        String dbPass = snapshot.child("password").getValue().toString();

                        if(dbPass.equals(passowrd)){ //if the password is correct

                            //fetch the data and initialize the current user

                            new CurrentUser(LoginActivity.this).setUsername(username);
                            new CurrentUser(LoginActivity.this).setPass(loginId);
                            new CurrentUser(LoginActivity.this).setName(name);

                            if(loginId.equals("1")){  //if it is a student
                                startActivity(new Intent(LoginActivity.this, StudentHomePage.class));
                                finish();
                            }
                            else { //if it is a faculty
                                startActivity(new Intent(LoginActivity.this, FacultyHomePage.class));
                                finish();
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show(); //wrong password message
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Register Yourself before logging in", Toast.LENGTH_SHORT).show(); // User not registered message
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}

