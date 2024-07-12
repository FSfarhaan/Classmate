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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginStudents extends AppCompatActivity {

    EditText studentId, studentPassword;
    TextView CreateAccount;
    Button btnLogIn;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_students);

        studentId = findViewById(R.id.studentId);
        studentPassword = findViewById(R.id.studentPassword);
        CreateAccount = findViewById(R.id.CreateAccount);
        btnLogIn = findViewById(R.id.btnLogIn);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Please wait for a moment");

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String userName = studentId.getText().toString();
                String password = studentPassword.getText().toString();
                if(userName.equals("") || password.equals(""))
                    Toast.makeText(LoginStudents.this, "Please enter necessary information", Toast.LENGTH_SHORT).show();
                else {
                    auth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                Intent intent = new Intent(LoginStudents.this, MainActivity.class);
                                finish();
                                startActivity(intent);
                                // Toast.makeText(SignInActivity.this, "Sign In successfull", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(LoginStudents.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginStudents.this, CreateAccountStudents.class));
            }
        });

        if(auth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginStudents.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }
}