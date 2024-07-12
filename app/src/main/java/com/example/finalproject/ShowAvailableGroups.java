package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalproject.adapters.showAvailableGroupsAdapter;
import com.example.finalproject.authentication.CreateAccountStudents;
import com.example.finalproject.authentication.LoginStudents;
import com.example.finalproject.models.MessageModel;
import com.example.finalproject.models.showAvailableGroupsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ShowAvailableGroups extends AppCompatActivity {

    ArrayList<showAvailableGroupsModel> arrayList = new ArrayList<showAvailableGroupsModel>();
    showAvailableGroupsAdapter adapter;
    RecyclerView recyclerView;
    Button btnLogout, createGroupBtn;
    FirebaseAuth auth;
    ImageView addBtn, createNewGroupBtn,joinGroupBtn, goBackFromGroups;
    Dialog dialog;
    EditText adminCode, groupName, groupCode;
    int toggleAddBtn = 0;
    FirebaseDatabase database;
    DbHelper db = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_available_groups);
        FirebaseApp.initializeApp(this);

        recyclerView = findViewById(R.id.rvShowAvailableGroups);
        btnLogout = findViewById(R.id.btnLogout);
        addBtn = findViewById(R.id.addBtn);
        createNewGroupBtn = findViewById(R.id.createNewGroupBtn);
        joinGroupBtn = findViewById(R.id.joinGroupBtn);
        goBackFromGroups = findViewById(R.id.goBackFromGroups);

        arrayList  = db.getGroups();
        auth = FirebaseAuth.getInstance();
        dialog = new Dialog(ShowAvailableGroups.this);
        database =  FirebaseDatabase.getInstance();
        dialog.setContentView(R.layout.dialog_create_new_group);
        adminCode = dialog.findViewById(R.id.adminCode);
        groupName = dialog.findViewById(R.id.groupName);
        groupCode = dialog.findViewById(R.id.groupCode);
        createGroupBtn = dialog.findViewById(R.id.createGroupBtn);

        adapter = new showAvailableGroupsAdapter(this, arrayList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowAvailableGroups.this, LoginStudents.class));
                auth.signOut();
                finish();
            }
        });

        goBackFromGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowAvailableGroups.this, MainActivity.class));
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // This will toggle the button. The three buttons will toggle on click of the plus sign;
                if(toggleAddBtn == 0){
                    toggleAddBtn = 1;
                    addBtn.setImageResource(R.drawable.cross_btn);
                    createNewGroupBtn.setVisibility(View.VISIBLE);
                    joinGroupBtn.setVisibility(View.VISIBLE);

                    // When tapped on creating the new group icon;
                    createNewGroupBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(adminCode.getVisibility() == View.GONE) adminCode.setVisibility(View.VISIBLE);
                            dialog.show();

                            // After filling the details, when tapped on create button;
                            createGroupBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String adminPin = adminCode.getText().toString();
                                    String nameOfGroup = groupName.getText().toString();
                                    String codeOfGroup = groupCode.getText().toString();
                                    if(adminPin.equals("") || nameOfGroup.equals("") || codeOfGroup.equals("")) {
                                        Toast.makeText(ShowAvailableGroups.this, "Please fill the necessary details", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        // Query for checking the validity of admin Id;
                                        database.getReference().child("Admin Id").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                // Checking whether the entered Admin Id is valid;
                                                String actualAdminId = snapshot.child("id").getValue(String.class);
                                                if(adminPin.equals(actualAdminId)){

                                                    // Query for checking if any group with same name already exists;
                                                    database.getReference().child("Groups").child(nameOfGroup).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if(snapshot.exists())
                                                                // The group name so specified already exists, hence another group of same name cannot be created;
                                                                Toast.makeText(ShowAvailableGroups.this, "Group with such name already exists", Toast.LENGTH_SHORT).show();
                                                            else {
                                                                // The group so specified does not exists and hence a dialog to confirm the entered group name and id is displayed;
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(ShowAvailableGroups.this)
                                                                        .setTitle("Confirm your group details")
                                                                        .setMessage("Your group name: \n" + nameOfGroup + " \n\nYour group id is: \n" + codeOfGroup)
                                                                        .setPositiveButton("Yes", (dialogInterface, i) -> {

                                                                            // If Yes is clicked, a group is to be created with necessary details of the group;
                                                                            String adminName = auth.getCurrentUser().getDisplayName();
                                                                            showAvailableGroupsModel model = new showAvailableGroupsModel(0, nameOfGroup, "Enter the chat");
                                                                            model.setAdminName(adminName);
                                                                            model.setGroupId(codeOfGroup);

                                                                            // Adding a new Node for the group under the "Groups"
                                                                            database.getReference().child("Groups").child(nameOfGroup).setValue(model)
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {
                                                                                            Toast.makeText(ShowAvailableGroups.this, "The group has been created", Toast.LENGTH_SHORT).show();
                                                                                            dialog.dismiss();
                                                                                        }
                                                                                    });

                                                                            Calendar calendar = Calendar.getInstance();
                                                                            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                                                            String newDate = dateFormat.format(calendar.getTime());

                                                                            String message = "I " + adminName + " welcome you all to my group";
                                                                            MessageModel messageModel = new MessageModel(auth.getUid(), message);
                                                                            messageModel.setTimestamp(new Date().getTime());
                                                                            messageModel.setName(auth.getCurrentUser().getDisplayName());
                                                                            messageModel.setCurrentTime(newDate);

                                                                            // Initializing the group chatting activity;
                                                                            database.getReference().child("Group Chat").child(nameOfGroup).push().setValue(messageModel)
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {

                                                                                        }
                                                                                    });

                                                                            // Adding the same details of the group to be stored in SQLite;
//                                                                    db.insertGroups(groupName, groupId);
//                                                                    model.setImage(R.drawable.img);
//                                                                    model.setGroupText("Enter the chat");
                                                                            arrayList.add(model);
                                                                            adapter.notifyDataSetChanged();
                                                                            // Toast.makeText(MainActivity.this, "You can create the group", Toast.LENGTH_SHORT).show();
                                                                        })
                                                                        .setNegativeButton("No", (dialogInterface, i) -> {

                                                                        });
                                                                builder.show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Toast.makeText(ShowAvailableGroups.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                                else {
                                                    // Admin ID is invalid;
                                                    Toast.makeText(ShowAvailableGroups.this, "Invalid Admin ID", Toast.LENGTH_SHORT).show();
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

                    joinGroupBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adminCode.setVisibility(View.GONE);
                            dialog.show();

                            createGroupBtn.setText("Join");
                            createGroupBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String nameOfGroup = groupName.getText().toString();
                                    String codeOfGroup = groupCode.getText().toString();
                                    if(nameOfGroup.equals("") || codeOfGroup.equals("")) {
                                        Toast.makeText(ShowAvailableGroups.this, "Please fill the necessary details", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        // Checking under the "Groups" Section whether the group specified by the user exists or not;
                                        database.getReference().child("Groups").child(nameOfGroup).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    String actualGroupID = snapshot.child("groupId").getValue(String.class);
                                                    if(actualGroupID != null && actualGroupID.equals(codeOfGroup)) {
                                                        // If entered group exists and the id is correct;

                                                        // Checking whether the user has already joined the group;
                                                        boolean groupExists = false;
                                                        for(showAvailableGroupsModel groupModel: arrayList) {
                                                            if(groupModel.getGroupName().equals(nameOfGroup)){
                                                                Toast.makeText(ShowAvailableGroups.this, "You have already joined that group ", Toast.LENGTH_SHORT).show();
                                                                groupExists = true;
                                                                break;
                                                            }
                                                        }

                                                        // If not then;
                                                        if(!groupExists) {
                                                            Toast.makeText(ShowAvailableGroups.this, "You can join the group", Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                            showAvailableGroupsModel model = new showAvailableGroupsModel();
                                                            db.insertGroups(nameOfGroup, codeOfGroup);
                                                            model.setImage(R.drawable.avatar_img);
                                                            model.setGroupName(nameOfGroup);
                                                            model.setGroupText("Enter the chat");
                                                            arrayList.add(model);
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                    else {
                                                        // If exists but the id is not correct;
                                                        Toast.makeText(ShowAvailableGroups.this, "Incorrect Id", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else{
                                                    // The group does not exists;
                                                    Toast.makeText(ShowAvailableGroups.this, "No such group exists", Toast.LENGTH_SHORT).show();
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
                else{
                    toggleAddBtn = 0;
                    createNewGroupBtn.setVisibility(View.INVISIBLE);
                    joinGroupBtn.setVisibility(View.INVISIBLE);
                    addBtn.setImageResource(R.drawable.create_join_group_btn);
                }
            }
        });

    }
}
