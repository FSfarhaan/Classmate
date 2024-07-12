package com.example.finalproject.DrawerOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.models.StudentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SearchStudentActivity extends AppCompatActivity {

    ImageView goBackFromSearch, searchBtn, profImage;
    EditText searchByStuId;
    FirebaseDatabase database;
    TextView noStudentFound;
    ConstraintLayout showProfileOnClick;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_student);

        goBackFromSearch = findViewById(R.id.goBackFromSearch);
        searchByStuId = findViewById(R.id.searchByStuId);
        searchBtn = findViewById(R.id.searchBtn);
        noStudentFound = findViewById(R.id.noStudentFound);
        showProfileOnClick = findViewById(R.id.showProfileOnClick);
        profImage = findViewById(R.id.profImage);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Searching...");
        progressDialog.setMessage("Please wait for a moment");
        database = FirebaseDatabase.getInstance();

        goBackFromSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchStudentActivity.this, MainActivity.class));
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                showProfileOnClick.setVisibility(View.GONE);
                String studentId = searchByStuId.getText().toString() + "@gmail.com";
                database.getReference().child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean studentFound = false;

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            StudentModel model = dataSnapshot.getValue(StudentModel.class);
                            if (model.getStudentId().equals(studentId)) {
                                studentFound = true;
                                noStudentFound.setVisibility(View.GONE);
                                showProfileOnClick.setVisibility(View.VISIBLE);

                                TextView profUserName, profStuId, profEmail, profPhone, profDiv, profRoll;
                                profUserName = findViewById(R.id.profUserName);
                                profStuId = findViewById(R.id.profStuId);
                                profEmail = findViewById(R.id.profEmail);
                                profPhone = findViewById(R.id.profPhone);
                                profDiv = findViewById(R.id.profDiv);
                                profRoll = findViewById(R.id.profRoll);

                                profUserName.setText(model.getFullName());
                                profStuId.setText(model.getStudentId().substring(0, model.getStudentId().indexOf('@')));
                                profEmail.setText(model.getStudentId());
                                profPhone.setText(model.getPhoneNo());
                                profDiv.setText(model.getDivision());
                                profRoll.setText(model.getRollNo());

//                                String imageUrl = model.getProfilePic();
//                                if (imageUrl != null && !imageUrl.isEmpty()) {
//                                    Picasso.get().load(imageUrl).placeholder(R.drawable.avatar_img).into(profImage);
//                                }
                                break; // Exit the loop once student is found
                            }
                        }
                        if (!studentFound) {
                            Toast.makeText(SearchStudentActivity.this, "Student not found", Toast.LENGTH_SHORT).show();
                            showProfileOnClick.setVisibility(View.GONE);
                            noStudentFound.setVisibility(View.VISIBLE);
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Error handling
                        Toast.makeText(SearchStudentActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}