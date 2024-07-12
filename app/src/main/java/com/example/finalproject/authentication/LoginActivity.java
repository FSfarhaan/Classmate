package com.example.finalproject.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.ChooseToEnterActivity;
import com.example.finalproject.MainActivity;
import com.example.finalproject.MainSix.AnnouncementActivity;
import com.example.finalproject.R;
import com.example.finalproject.SharedPrefManager;
import com.example.finalproject.models.AdminModel;
import com.example.finalproject.models.StudentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {
    EditText enterId, enterPassword;
    TextView createAccount;
    Button btnLogIn;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        enterId = findViewById(R.id.enterId);
        enterPassword = findViewById(R.id.enterPassword);
        createAccount = findViewById(R.id.createAccount);
        btnLogIn = findViewById(R.id.btnLogIn);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging in to your account");
        progressDialog.setMessage("Please wait for a moment");
        sharedPrefManager = new SharedPrefManager(this);

        Intent intent = getIntent();
        String loginType = intent.getStringExtra("Login Type");
        assert loginType != null;
        if (loginType.equals("Student")) enterId.setHint("Student ID");
        else enterId.setHint("Admin ID");

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra("Signup Type", loginType);
                startActivity(intent);
                finish();
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                String userId = enterId.getText().toString().trim();
                String password = enterPassword.getText().toString().trim();

                if (checkLogin(userId, password)) {
                    checkCredentials(userId, password, loginType);
                } else {
                    progressDialog.dismiss();
                }
            }
        });
    }

    // --------------------- Checking for empty fields ---------------------
    public boolean checkLogin(String userId, String password) {
        if (TextUtils.isEmpty(userId)) {
            enterId.setError("This is required");
            Log.d("DEKHTE HAI KYU NAHI AA RAHA", "NMahi nahi yaha");
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Log.d("DEKHTE HAI KYU NAHI AA RAHA", "Oye nahi yaha");
            enterPassword.setError("This is required");
            return false;
        } else {
            enterId.setError(null);
            enterPassword.setError(null);
            return true;
        }
    }


    // --------------------- Checking for credentials ---------------------
    private void checkCredentials(String userId, String password, String loginType) {
        String userType = loginType.equals("Student") ? "Students" : "Teachers";
        String idField = userType.equals("Students") ? "studentId" : "adminId";
        Log.d("DEKHTE HAI KYU NAHI AA RAHA", loginType);

        database.getReference().child(userType).orderByChild(idField).equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String hashedPassword = userSnapshot.child("password").getValue(String.class);

                    if (hashedPassword != null && BCrypt.checkpw(password, hashedPassword)) {

                        if(userType.equals("Students")) {
                            String fullName = userSnapshot.child("fullName").getValue(String.class);
                            String div = userSnapshot.child("division").getValue(String.class);
                            String roll = userSnapshot.child("rollNo").getValue(String.class);
                            String contact = userSnapshot.child("phoneNo").getValue(String.class);
                            StudentModel newStudent = new StudentModel(fullName, userId, div, roll, contact, password);
                            sharedPrefManager.saveUser(newStudent);
                        } else {
                            String fullName = userSnapshot.child("fullName").getValue(String.class);
                            String contact = userSnapshot.child("contactNo").getValue(String.class);
                            AdminModel newAdmin = new AdminModel(fullName, userId, contact, password);
                            sharedPrefManager.saveUser(newAdmin);
                        }

                        // Password matches, proceed with Firebase Auth login using the actual password
                        Toast.makeText(LoginActivity.this, "Match hogaya re", Toast.LENGTH_SHORT).show();
                        Log.d("DEKHTE HAI KYU NAHI AA RAHA", "Match hua re");
                        String email = userId + "@gmail.com";
                        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Log.d("DEKHTE HAI KYU NAHI AA RAHA", task.getException().getMessage());
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, ChooseToEnterActivity.class));
        finish();
    }
}
