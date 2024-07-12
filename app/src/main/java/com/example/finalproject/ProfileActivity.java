package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.finalproject.MainSix.AnnouncementActivity;
import com.example.finalproject.models.AdminModel;
import com.example.finalproject.models.StudentModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    ImageView goBackFromProfile, profImageOfUser;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    EditText profUserName, profStuId;
    EditText profEmail, profPassword, profPhone, profDiv, profRoll, profExtraCurr;
    LinearLayout updateProfileBtn;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        goBackFromProfile = findViewById(R.id.goBackFromProfile);
        profUserName = findViewById(R.id.profUserName);
        profStuId = findViewById(R.id.profStuId);
        profEmail = findViewById(R.id.profEmail);
        profPassword = findViewById(R.id.profPassword);
        profPhone = findViewById(R.id.profPhone);
        profDiv = findViewById(R.id.profDiv);
        profRoll = findViewById(R.id.profRoll);
        profExtraCurr = findViewById(R.id.profExtraCurr);
        updateProfileBtn = findViewById(R.id.updateProfileBtn);
        profImageOfUser = findViewById(R.id.profImageOfUser);
        sharedPrefManager = new SharedPrefManager(this);
        Object user = sharedPrefManager.getUser();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        int accessCode = intent.getIntExtra("accessCode", 10);

        if (accessCode == 56) {
            // User can edit profile
            enableProfileEditing();
        } else {
            // User cannot edit profile
            disableProfileEditing();
        }

        goBackFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        });

        String receivedEmail = auth.getCurrentUser().getEmail();


        if (user instanceof StudentModel) {
            StudentModel storedStudent = (StudentModel) user;
            profUserName.setText(storedStudent.getFullName());
            profStuId.setText(storedStudent.getStudentId().toUpperCase());
            profEmail.setText(receivedEmail);
            profPassword.setText(storedStudent.getPassword());
            profPhone.setText(storedStudent.getPhoneNo());
            profDiv.setText(storedStudent.getDivision());
            profRoll.setText(storedStudent.getRollNo());
        } else {
            AdminModel storedAdmin = (AdminModel) user;
            profUserName.setText(storedAdmin.getFullName());
            profStuId.setText(storedAdmin.getAdminId().toUpperCase());
            profEmail.setText(receivedEmail);
            profPassword.setText(storedAdmin.getPassword());
            profPhone.setText(storedAdmin.getContactNo());
        }

//        database.getReference().child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                    StudentModel student = childSnapshot.getValue(StudentModel.class);
//                    if (student != null && student.getStudentId().concat("@gmail.com").equalsIgnoreCase(receivedEmail)) {
//                        // Found the student with matching email
//                        String name = student.getFullName();
//                        String stuId = receivedEmail.substring(0, receivedEmail.indexOf('@')).toUpperCase();
//                        String password = student.getPassword();
//                        String phone = student.getPhoneNo();
//                        String division = student.getDivision();
//                        String rollNo = student.getRollNo();
//                        profUserName.setText(name);
//                        profStuId.setText(stuId);
//                        profEmail.setText(receivedEmail);
//                        profPassword.setText(password);
//                        profPhone.setText(phone);
//                        profDiv.setText(division);
//                        profRoll.setText(rollNo);
//
//                        // Load profile picture if available
////                        String imageUrl = student.getProfilePic();
////                        if (imageUrl != null && !imageUrl.isEmpty()) {
////                            Picasso.get().load(imageUrl).placeholder(R.drawable.avatar_img).into(profImageOfUser);
////                        }
//
//                        break; // No need to continue searching once found
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });
    }

    private void enableProfileEditing() {
        profImageOfUser.setClickable(true);
        profUserName.setEnabled(true);
        profStuId.setEnabled(true);
        profEmail.setEnabled(true);
        profPassword.setEnabled(true);
        profPhone.setEnabled(true);
        profDiv.setEnabled(true);
        profRoll.setEnabled(true);
        profExtraCurr.setEnabled(true);
        updateProfileBtn.setVisibility(View.VISIBLE);

        profImageOfUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImage = new Intent();
                getImage.setAction(Intent.ACTION_GET_CONTENT);
                getImage.setType("image/*");
                startActivityForResult(getImage, 75);
            }
        });
    }

    private void disableProfileEditing() {
        profImageOfUser.setClickable(false);
        profUserName.setEnabled(false);
        profStuId.setEnabled(false);
        profEmail.setEnabled(false);
        profPassword.setEnabled(false);
        profPhone.setEnabled(false);
        profDiv.setEnabled(false);
        profRoll.setEnabled(false);
        profExtraCurr.setEnabled(false);
        updateProfileBtn.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 75 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            profImageOfUser.setImageURI(uri);

            StorageReference reference = storage.getReference().child("profile_pictures").child(auth.getUid());

            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Students").child(auth.getUid()).child("profilePic").setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
    }
}
