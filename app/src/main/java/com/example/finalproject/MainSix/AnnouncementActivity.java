package com.example.finalproject.MainSix;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.adapters.AnnouncementAdapter;
import com.example.finalproject.models.AnnouncementModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AnnouncementActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView addAnnouncementBtn, goBackFromAnnouncement;
    ArrayList<AnnouncementModel> arrayList;
    AnnouncementAdapter adapter;
    Dialog dialog;
    EditText dialogTitleOfAnnouncement, dialogContentOfAnnouncement;
    LinearLayout dialogAddAnImageBtn, postAnnouncement;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    private static final int REQUEST_IMAGE_PICK = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        recyclerView = findViewById(R.id.rvOfAnnouncement);
        addAnnouncementBtn = findViewById(R.id.addAnnouncementBtn);
        goBackFromAnnouncement = findViewById(R.id.goBackFromAnnouncement);

        dialog = new Dialog(this, R.style.CustomDialog);
        arrayList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setMessage("Please wait for a moment");

        adapter = new AnnouncementAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Getting the announcement from the database;
        database.getReference().child("announcement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot announcementSnapshot : snapshot.getChildren()) {
                    AnnouncementModel model = announcementSnapshot.getValue(AnnouncementModel.class);
                    arrayList.add(0, model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AnnouncementActivity.this, "Failed to retrieve announcements", Toast.LENGTH_SHORT).show();
            }
        });


        goBackFromAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnnouncementActivity.this, MainActivity.class));
                finish();
            }
        });


        addAnnouncementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.dialog_create_announcement);
                dialog.show();

                dialogTitleOfAnnouncement = dialog.findViewById(R.id.dialogTitleOfAnnouncement);
                dialogContentOfAnnouncement = dialog.findViewById(R.id.dialogContentOfAnnouncement);
                dialogAddAnImageBtn = dialog.findViewById(R.id.dialogAddAnImageBtn);
                TextView cancelBtn = dialog.findViewById(R.id.cancelBtn);
                postAnnouncement = dialog.findViewById(R.id.postAnnouncement);

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialogAddAnImageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_IMAGE_PICK);
                    }
                });

                postAnnouncement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dialogAddAnImageBtn.getVisibility() == View.VISIBLE){
                            progressDialog.show();
                            
                            String title = dialogTitleOfAnnouncement.getText().toString();
                            String content = dialogContentOfAnnouncement.getText().toString();
                            String adminName = auth.getCurrentUser().getDisplayName();

                            dialog.dismiss();
                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH) + 1;
                            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int minute = calendar.get(Calendar.MINUTE);

                            String time = String.format("%02d:%02d", hour, minute);
                            String todayDate = dayOfMonth + " " + getMonthName(month) + " " + year;

                            database.getReference().child("Students").child(auth.getUid()).child("profilePic").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String senderImg;
                                    if(snapshot.exists()){
                                        senderImg = snapshot.getValue(String.class);
                                        AnnouncementModel model = new AnnouncementModel(title, adminName, time + " | " + todayDate, content, senderImg);
                                        database.getReference().child("announcement").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(AnnouncementActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    else{
                                        AnnouncementModel model = new AnnouncementModel(title, adminName, time + " | " + todayDate, content, null);
                                        database.getReference().child("announcement").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(AnnouncementActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
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
                });
            }
        });
    }

    private String getMonthName(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month - 1]; // Subtract 1 as month starts from 0
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                View dialogView = dialog.findViewById(android.R.id.content);
                ImageView dialogImgOfAnnouncement = dialogView.findViewById(R.id.dialogImgOfAnnouncement);
                dialogImgOfAnnouncement.setImageURI(selectedImageUri);
                dialogImgOfAnnouncement.setVisibility(View.VISIBLE);

                dialogAddAnImageBtn.setVisibility(View.GONE);

                LinearLayout postAnnouncement = dialogView.findViewById(R.id.postAnnouncement);
                postAnnouncement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = dialogTitleOfAnnouncement.getText().toString();
                        String content = dialogContentOfAnnouncement.getText().toString();
                        if (title.isEmpty() || content.isEmpty()) {
                            Toast.makeText(AnnouncementActivity.this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.show();
                            dialog.dismiss();
                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH) + 1;
                            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int minute = calendar.get(Calendar.MINUTE);

                            String time = String.format("%02d:%02d", hour, minute);
                            String todayDate = dayOfMonth + " " + getMonthName(month) + " " + year;

                            // Upload image to Firebase Storage
                            StorageReference storageRef = storage.getReference().child("noticeImg");
                            String imageName = UUID.randomUUID().toString();
                            StorageReference imageRef = storageRef.child(imageName);

                            imageRef.putFile(selectedImageUri)
                                    .addOnSuccessListener(taskSnapshot -> {
                                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                            String imageUrl = uri.toString();
                                            String currentUserDisplayName = auth.getCurrentUser().getDisplayName();

                                            database.getReference().child("Students").child(auth.getUid()).child("profilePic").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        String senderImg = snapshot.getValue(String.class);
                                                        AnnouncementModel model = new AnnouncementModel(title, currentUserDisplayName, time + " | " + todayDate, content, senderImg);
                                                        // Set imgUrl field only if an image is attached
                                                        model.setImgUrl(imageUrl);

                                                        // Save announcement to Firebase Database
                                                        database.getReference().child("announcement").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(AnnouncementActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                    else{
                                                        AnnouncementModel model = new AnnouncementModel(title, currentUserDisplayName, time + " | " + todayDate, content, null);
                                                        // Set imgUrl field only if an image is attached
                                                        model.setImgUrl(imageUrl);

                                                        // Save announcement to Firebase Database
                                                        database.getReference().child("announcement").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(AnnouncementActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        });
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(AnnouncementActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
                        }
                    }
                });

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AnnouncementActivity.this, MainActivity.class));
        finish();
    }
}
