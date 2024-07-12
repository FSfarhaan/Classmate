package com.example.finalproject.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.ChooseToEnterActivity;
import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.SharedPrefManager;
import com.example.finalproject.models.AdminModel;
import com.example.finalproject.models.StudentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

public class SignupActivity extends AppCompatActivity {
    EditText enterFullName, enterAdminId, enterStudentId, enterDiv,  enterRoll, enterContact, enterPassword;
    TextView alreadyHaveAccount;
    Button btnSignUp;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        enterFullName = findViewById(R.id.enterFullName);
        enterAdminId = findViewById(R.id.enterAdminId);
        enterStudentId = findViewById(R.id.enterStudentId);
        enterDiv = findViewById(R.id.enterDiv);
        enterRoll = findViewById(R.id.enterRoll);
        enterContact = findViewById(R.id.enterContact);
        enterPassword = findViewById(R.id.enterPassword);
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);
        btnSignUp = findViewById(R.id.btnSignUp);
        sharedPrefManager = new SharedPrefManager(this);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating account");
        progressDialog.setMessage("Please wait for a moment");

        Intent intent = getIntent();
        String signupType = intent.getStringExtra("Signup Type");
        assert signupType != null;
        if(signupType.equals("Student")) {
            setVisibilitiesForStudents();
            hideAdminVisibility();
        } else {
            setVisibilitiesForAdmins();
            hideStudentVisibility();
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                // --------------- Getting the details of the user ----------------
                String fullName = enterFullName.getText().toString().trim();
                String contact = enterContact.getText().toString().trim();
                String password = enterPassword.getText().toString().trim();

                if(signupType.equals("Student") ) {
                    String studentID = enterStudentId.getText().toString().trim() + "@gmail.com";
                    String div = enterDiv.getText().toString().trim().toUpperCase();
                    String roll = enterRoll.getText().toString().trim();
                    
                    if(checkForStudents(fullName, studentID, div, roll, contact, password)) {
                        createStudentUser(fullName, studentID, div, roll, contact, password);
                    }
                }
                else {
                    String adminID = enterAdminId.getText().toString().trim();

                    if(checkForAdmins(fullName, adminID, contact, password)) {
                        checkAdminIDAndCreateUser(fullName, adminID, contact, password);
                    }
                }

            }
        });
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLogin = new Intent(SignupActivity.this, LoginActivity.class);
                toLogin.putExtra("Login Type", signupType);
                startActivity(toLogin);
                finish();
            }
        });
    }


    // --------------------- Toggling the visibilities of the field not required while signing in ---------------------
    public void setVisibilitiesForStudents () {
        enterStudentId.setVisibility(View.VISIBLE); enterDiv.setVisibility(View.VISIBLE); enterRoll.setVisibility(View.VISIBLE);
    }
    public void hideAdminVisibility() {
        enterAdminId.setVisibility(View.GONE);
    }
    public void setVisibilitiesForAdmins() {
        enterAdminId.setVisibility(View.VISIBLE);
    }
    public void hideStudentVisibility() {
        enterStudentId.setVisibility(View.GONE); enterDiv.setVisibility(View.GONE); enterRoll.setVisibility(View.GONE);
    }


    // --------------------- Checking for empty fields ---------------------
    public boolean checkForStudents(String fullName, String studentID, String div, String roll, String contact, String password) {
        if(fullName.length() <= 6) {
            enterFullName.setError("Please enter full name");
            return false;
        } else if(studentID.length() != 21) {
            enterStudentId.setError("Enter Valid Student ID");
            return false;
        } else if(TextUtils.isEmpty(div)) {
            enterDiv.setError("This is required");
            return false;
        } else if(!div.equals("A") && !div.equals("B")) {
            enterDiv.setError("Please enter valid value");
            return false;
        } else if(!TextUtils.isDigitsOnly(roll)) {
            enterRoll.setError("Only numbers allowed");
            return false;
        } else if(TextUtils.isEmpty(roll)) {
            enterRoll.setError("This is required");
            return false;
        } else if(TextUtils.isEmpty(contact)) {
            enterContact.setError("This is required");
            return false;
        } else if(!TextUtils.isDigitsOnly(contact)) {
            enterContact.setError("Only numbers allowed");
            return false;
        } else if(TextUtils.isEmpty(password)) {
            enterPassword.setError("This is required");
            return false;
        } else if(password.length() < 6) {
            enterPassword.setError("Password must be at least 6 characters long");
            return false;
        }
        else {
            enterFullName.setError(null);
            enterStudentId.setError(null);
            enterDiv.setError(null);
            enterRoll.setError(null);
            enterContact.setError(null);
            enterPassword.setError(null);
            return true;
        }
    }
    public boolean checkForAdmins(@NonNull String fullName, String adminID, String contact, String password) {
        if(fullName.length() <= 6) {
            enterFullName.setError("Please enter full name");
            return false;
        } else if(TextUtils.isEmpty(adminID)) {
            enterAdminId.setError("This is required");
            return false;
        } else if(TextUtils.isEmpty(contact)) {
            enterContact.setError("This is required");
            return false;
        } else if(!TextUtils.isDigitsOnly(contact)) {
            enterContact.setError("Only numbers allowed");
            return false;
        } else if(TextUtils.isEmpty(password)) {
            enterPassword.setError("This is required");
            return false;
        } else if(password.length() < 6) {
            enterPassword.setError("Password must be at least 6 characters long");
            return false;
        }
        else {
            enterFullName.setError(null);
            enterAdminId.setError(null);
            enterContact.setError(null);
            enterPassword.setError(null);
            Toast.makeText(this, "Check se nikla", Toast.LENGTH_SHORT).show();
            return true;
        }
    }


    // --------------------- Creating Users ---------------------
    public void createStudentUser(String fullName, String studentID, String div, String roll, String contact, String password) {
        auth.createUserWithEmailAndPassword(studentID, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){

                    FirebaseUser user = task.getResult().getUser();
                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(fullName).build();
                    user.updateProfile(profileUpdate);

                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                    StudentModel newStudent = new StudentModel(fullName, studentID.substring(0, studentID.indexOf('@')), div, roll, contact, hashedPassword);
                    String id = task.getResult().getUser().getUid();
                    database.getReference().child("Students").child(id).setValue(newStudent);
                    sharedPrefManager.saveUser(new StudentModel(fullName, studentID.substring(0, studentID.indexOf('@')), div, roll, contact, password));

                    Toast.makeText(SignupActivity.this, "Welcome " + fullName, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                    finish();
                } else{
                    Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void createAdminUser(String fullName, String adminID, String contact, String password) {
        auth.createUserWithEmailAndPassword(adminID, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){

                    FirebaseUser user = task.getResult().getUser();
                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(fullName).build();
                    user.updateProfile(profileUpdate);

                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                    AdminModel newAdmin = new AdminModel(fullName, adminID.substring(0, adminID.indexOf('@')), contact, hashedPassword);
                    String id = task.getResult().getUser().getUid();
                    database.getReference().child("Teachers").child(id).setValue(newAdmin);

                    sharedPrefManager.saveUser(new AdminModel(fullName, adminID.substring(0, adminID.indexOf('@')), contact, password));

                    Toast.makeText(SignupActivity.this, "Welcome " + fullName, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                    finish();
                } else{
                    Toast.makeText(SignupActivity.this, "task.getException().getMessage()", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // --------------------- Checking for Admin ID ---------------------
    public void checkAdminIDAndCreateUser(String fullName, String adminID, String contact, String password) {
        database.getReference().child("Admin Id").child("ids").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean adminExists = false;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String actualAdminId = dataSnapshot.getValue(String.class);
                    if(actualAdminId.equals(adminID)) {
                        adminExists = true;
                        createAdminUser(fullName, adminID + "@gmail.com", contact, password);
                        progressDialog.dismiss();
                        break;
                    }
                }
                if(!adminExists){
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "No Such admin Id exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignupActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignupActivity.this, ChooseToEnterActivity.class));
        finish();
    }
}