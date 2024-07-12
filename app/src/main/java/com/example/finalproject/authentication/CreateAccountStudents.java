package com.example.finalproject.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.ShowAvailableGroups;
import com.example.finalproject.models.StudentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountStudents extends AppCompatActivity {

    EditText studentFullName, studentId, studentDiv, studentRoll, studentContact, studentPassword;
    TextView alreadyHaveAccount;
    Button btnSignUp;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_students);

        studentFullName = findViewById(R.id.studentFullName);
        studentId = findViewById(R.id.studentId);
        studentDiv = findViewById(R.id.studentDiv);
        studentRoll = findViewById(R.id.studentRoll);
        studentContact = findViewById(R.id.studentContact);
        studentPassword = findViewById(R.id.studentPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating account");
        progressDialog.setMessage("Please wait for a moment");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                // Getting the details of the student;
                String fullName = studentFullName.getText().toString();
                String emailId = studentId.getText().toString();
                String div = studentDiv.getText().toString();
                String roll = studentRoll.getText().toString();
                String contact = studentContact.getText().toString();
                String password = studentPassword.getText().toString();

                if(fullName.equals("") || emailId.equals("") || div.equals("") || roll.equals("") || contact.equals("") || password.equals("")){
                    Toast.makeText(CreateAccountStudents.this, "Please enter necessary information", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else{
                    auth.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                //Adding the name entered by the user to the F

                                // irebase;
                                FirebaseUser user = task.getResult().getUser();
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(fullName).build();
                                user.updateProfile(profileUpdate);

                                StudentModel student = new StudentModel(fullName, emailId, div, contact, password, roll);
                                String id = task.getResult().getUser().getUid();
                                database.getReference().child("Students").child(id).setValue(student);
                                Toast.makeText(CreateAccountStudents.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(CreateAccountStudents.this, MainActivity.class));
                            }
                            else{
                                Toast.makeText(CreateAccountStudents.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccountStudents.this, LoginStudents.class));
            }
        });
    }
}