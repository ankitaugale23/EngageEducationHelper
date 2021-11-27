package com.example.educationhelper.Faculty;

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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.educationhelper.CurrentUser;
import com.example.educationhelper.FirebaseDataClass.UploadAssignmentFaculty;
import com.example.educationhelper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateAssignmentActivity extends AppCompatActivity {
    DatabaseReference reference, historyAssignment;
    String assignmentName, username, assignmentCode, count, uploadStatus = null, channelCode;
    long assignmentCountInt = 0;
    EditText mAssignmentName;
    private static final int STORAGE_PERMISSION_CODE = 101;
    Uri dataImage;
    LoadingDialog loadingDialog;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assignment);

        channelCode = getIntent().getStringExtra("channelCode");
        loadingDialog = new LoadingDialog(CreateAssignmentActivity.this);
        username = new CurrentUser(CreateAssignmentActivity.this).getUsername();
        reference = FirebaseDatabase.getInstance().getReference("credentials").child(username).child("channel")
                .child(channelCode).child("assignments");
        historyAssignment = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        mAssignmentName = findViewById(R.id.nameofnotes);
        checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE);

        ////////////////////////////////////////////////////////////////////////////////////////////

        //will check if the assignment is already present or not
        historyAssignment.child("assignmentCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) { //if present
                    count = dataSnapshot.getValue().toString();
                }
                //if not present
                else {
                    Toast.makeText(CreateAssignmentActivity.this, "not present", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }

    //will upload the assignment
    public void uploadNotes(View view) {
        loadingDialog.startLoadingDialog();

        assignmentName = mAssignmentName.getText().toString().trim();

        if(assignmentName.equals("")){ //if "Assignment name" field is empty
            loadingDialog.dismissDialog();
            Toast.makeText(CreateAssignmentActivity.this, "Enter Assignment Name", Toast.LENGTH_SHORT).show();
        }
        //if the user has provided the name
        else {
            assignmentCountInt = Long.parseLong(count) + 1; //increment the assignment code
            assignmentCode = String.valueOf(assignmentCountInt);
            selectPDFFile(); //call a function which will let you select the file to upload
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(CreateAssignmentActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(CreateAssignmentActivity.this,
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

        if (requestCode == STORAGE_PERMISSION_CODE) {
            //if the request is granted
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(CreateAssignmentActivity.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(CreateAssignmentActivity.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    //function to let the user select file from device storage
    private void selectPDFFile() {
        try {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select your pdf note.."), 1);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if user selects the appropriate file
        if (requestCode == 1 && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            dataImage = data.getData();

            //upload the file
            uploadFile(new FirebaseCallback() {
                @Override
                public void onCallback(String value) {
                    if (value.equals("true")) {
                        loadingDialog.dismissDialog();
                        Toast.makeText(CreateAssignmentActivity.this, "Uploaded..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CreateAssignmentActivity.this, FacultyHomePage.class));
                        finish();
                    } else {
                        loadingDialog.dismissDialog();
                        Toast.makeText(CreateAssignmentActivity.this, "Error try again..", Toast.LENGTH_SHORT).show(); //if some error occurs
                    }
                }
            });
        } else {
            loadingDialog.dismissDialog();
            Toast.makeText(this, "try again.", Toast.LENGTH_SHORT).show(); //if user does not select any file
        }
    }

    private void uploadFile(final FirebaseCallback firebaseCallback) {

        //store the file into database storage
        StorageReference storageReference1 = storageReference.child("Assignments").child(username).child(assignmentCode+ "/" + assignmentName + ".pdf");
        storageReference1.putFile(dataImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete()) ;
                Uri url = uri.getResult();

                String key2;
                final UploadAssignmentFaculty uploadNotes = new UploadAssignmentFaculty( assignmentName, url.toString(), assignmentCode);

                historyAssignment.child("assignmentCount").setValue(assignmentCode).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //store the uploaded assignment
                        reference.child(assignmentCode).setValue(uploadNotes).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
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
                });


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            //responsible to show progress dialog for uploading the assignment
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
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

    private interface FirebaseCallback {
        void onCallback(String value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    //if the user clicks the back button on the device
    @Override
    public void onBackPressed() {
        Intent i = new Intent(CreateAssignmentActivity.this, AssignmentLandingPage.class);
        i.putExtra("channelCode",channelCode);
        startActivity(i);
        finish();
    }
}