package com.example.educationhelper.Student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.educationhelper.CurrentUser;

import com.example.educationhelper.FirebaseDataClass.UploadAssignmentFaculty;
import com.example.educationhelper.FirebaseDataClass.UploadStudentAssignment;
import com.example.educationhelper.R;
import com.example.educationhelper.ViewPdfActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Map;

public class StudentUploadAssignment extends AppCompatActivity {

    //Declare required identifiers, data containers, and database references
    String channelCode, assignmentCount, URL, username, assignmentNameStaff, studentPhone,
            studentName, uploadStatus = null, studentAssignmentURL;
    DatabaseReference reference;
    ListView mListView;
    private final String log = "RESULT DATA 2nd Page";
    TextView tvAssignmentName,tvAssignmentNameStudent;
    private static final int STORAGE_PERMISSION_CODE = 101;
    LoadingDialog loadingDialog;
    Uri dataImage;
    StorageReference storageReference;

    LinearLayout linearLayout;
    Button uploadAssignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_upload_assignment);

        //Initialize the identifiers, data containers, and database references
        loadingDialog = new LoadingDialog(StudentUploadAssignment.this);
        loadingDialog.startLoadingDialog();
        loadingDialog.setText("Loading");

        studentPhone = new CurrentUser(StudentUploadAssignment.this).getUsername();
        studentName = new CurrentUser(StudentUploadAssignment.this).getName();

        username = getIntent().getStringExtra("facultyPhone");
        channelCode = getIntent().getStringExtra("channelCode");
        assignmentCount = getIntent().getStringExtra("assignmentCount");
        assignmentNameStaff = getIntent().getStringExtra("assignmentNameStaff");
        URL = getIntent().getStringExtra("URL");

        linearLayout = findViewById(R.id.linearLayout);
        uploadAssignment = findViewById(R.id.bt_uploadAssignment);

        reference = FirebaseDatabase.getInstance().getReference("credentials").child(username).child("channel")
                .child(channelCode).child("assignments").child(assignmentCount);
        storageReference = FirebaseStorage.getInstance().getReference();

        mListView= findViewById(R.id.listview);
        tvAssignmentName = findViewById(R.id.tv_assignmentName);
        tvAssignmentNameStudent = findViewById(R.id.tv_assignmentNameStudent);

        tvAssignmentName.setText(assignmentNameStaff);
        tvAssignmentNameStudent.setText(assignmentNameStaff+" "+studentName);

        reference.child(studentPhone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if the assignment is already uploaded then stop showing "Upload Assignment" option
                if(snapshot.exists()){
                    uploadAssignment.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);

                    studentAssignmentURL = snapshot.child("studentAssignment").getValue().toString();
                    loadingDialog.dismissDialog();
                }
                else{
                    uploadAssignment.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.INVISIBLE);
                    loadingDialog.dismissDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE);

        //upload assignment when button is clicked
        uploadAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                //call to function created for selecting only pdf from device
                selectPDFFile();
            }
        });


    }
    //requests the permission for storage access
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(StudentUploadAssignment.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(StudentUploadAssignment.this,
                    new String[]{permission},
                    requestCode);
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        //if the permission is granted
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(StudentUploadAssignment.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            //if the permission is not granted
            else {
                Toast.makeText(StudentUploadAssignment.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    //select only pdf files from device
    private void selectPDFFile() {
        try {
            Intent intent = new Intent();
            intent.setType("application/pdf");//give type of file
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select your pdf note.."), 1);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Result for selected file
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            dataImage = data.getData();

            //calling function for uploading selected file
            uploadFile(new StudentUploadAssignment.FirebaseCallback() {
                @Override
                public void onCallback(String value) {
                    if (value.equals("true")) {
                        loadingDialog.dismissDialog();
                        Toast.makeText(StudentUploadAssignment.this, "Uploaded..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(StudentUploadAssignment.this, StudentHomePage.class));
                        finish();
                    } else {
                        loadingDialog.dismissDialog();
                        Toast.makeText(StudentUploadAssignment.this, "Error try again..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            loadingDialog.dismissDialog();
            Toast.makeText(this, "try again.", Toast.LENGTH_SHORT).show();
        }
    }

    //Actual function to upload file to firebase storage
    private void uploadFile(final StudentUploadAssignment.FirebaseCallback firebaseCallback) {

        //Creating reference for storage
        StorageReference storageReference1 = storageReference.child("Assignments").child(username).child(studentPhone+ "/" + assignmentNameStaff + ".pdf");
        storageReference1.putFile(dataImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete()) ;

                //getting URL of stored file in storage
                Uri url = uri.getResult();

                String key2;
                final UploadStudentAssignment uploadNotes = new UploadStudentAssignment(studentPhone, studentName, url.toString(),
                        "submitted");

                reference.child(studentPhone).setValue(uploadNotes).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        uploadStatus = "true";
                        firebaseCallback.onCallback(uploadStatus);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        uploadStatus = "false";
                        firebaseCallback.onCallback(uploadStatus);
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                //Show progress till all data is storing then close loading dialog
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                loadingDialog.setText("uploaded: " + (int) progress + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                uploadStatus = "false";
                firebaseCallback.onCallback(uploadStatus);
            }
        });

    }

    //view student assignment activity intent
    public void onOpenAssignmentStudent(View view) {
        Intent intent = new Intent(StudentUploadAssignment.this, ViewPdfActivity.class);
        intent.putExtra("url", studentAssignmentURL);
        startActivity(intent);
    }

    //view teacher assignment activity intent
    public void onOpenAssignment(View view) {
        Intent intent = new Intent(StudentUploadAssignment.this, ViewPdfActivity.class);
        intent.putExtra("url", URL);
        startActivity(intent);
    }

    //Firebase call back method work as async option
    private interface FirebaseCallback {
        void onCallback(String value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        Intent i = new Intent(StudentUploadAssignment.this, StudentShowAssignment.class);
        i.putExtra("channelCode",channelCode);
        i.putExtra("facultyPhone",username);
        startActivity(i);
        finish();
    }
}