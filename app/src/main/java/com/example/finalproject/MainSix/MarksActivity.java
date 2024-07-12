package com.example.finalproject.MainSix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.adapters.MarksAdapter;
import com.example.finalproject.models.MarksModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MarksActivity extends AppCompatActivity {

    ImageView addMarksBtn, goBackFromMarks;
    Dialog dialog;
    TextView cancelBtn;
    LinearLayout postMarks;
    EditText dialogTitleOfMarks;
    EditText ETmathsMarks, ETmpMarks, ETosMarks, ETpyMarks, ETdbmsMarks;
    EditText ETmathsOutOf, ETmpOutOf, ETosOutOf, ETpyOutOf, ETdbmsOutOf;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    MarksAdapter adapter;
    ArrayList<MarksModel> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);

        addMarksBtn = findViewById(R.id.addMarksBtn);
        goBackFromMarks = findViewById(R.id.goBackFromMarks);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_marks);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setMessage("Please wait for a moment");
        recyclerView = findViewById(R.id.rvOfMarks);

        adapter = new MarksAdapter(this, arrayList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database.getReference().child("Marks").child(auth.getCurrentUser().getDisplayName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot announcementSnapshot : snapshot.getChildren()) {
                    MarksModel model = announcementSnapshot.getValue(MarksModel.class);
                    arrayList.add(0, model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addMarksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                dialogTitleOfMarks = dialog.findViewById(R.id.dialogTitleOfMarks);
                cancelBtn = dialog.findViewById(R.id.cancelBtn);
                postMarks = dialog.findViewById(R.id.postMarks);

                ETmathsMarks = dialog.findViewById(R.id.ETmathsMarks);
                ETmathsOutOf = dialog.findViewById(R.id.ETmathsOutOf);
                ETmpMarks = dialog.findViewById(R.id.ETmpMarks);
                ETmpOutOf = dialog.findViewById(R.id.ETmpOutOf);
                ETosMarks = dialog.findViewById(R.id.ETosMarks);
                ETosOutOf = dialog.findViewById(R.id.ETosOutOf);
                ETpyMarks = dialog.findViewById(R.id.ETpyMarks);
                ETpyOutOf = dialog.findViewById(R.id.ETpyOutOf);
                ETdbmsMarks = dialog.findViewById(R.id.ETdbmsMarks);
                ETdbmsOutOf = dialog.findViewById(R.id.ETdbmsOutOf);

                postMarks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String titleOfMarks = dialogTitleOfMarks.getText().toString();
                        String mathsMarks = ETmathsMarks.getText().toString();
                        String mathsOutOf = ETmathsOutOf.getText().toString();
                        String mpMarks = ETmpMarks.getText().toString();
                        String mpOutOf = ETmpOutOf.getText().toString();
                        String osMarks = ETosMarks.getText().toString();
                        String osOutOf = ETosOutOf.getText().toString();
                        String pyMarks = ETpyMarks.getText().toString();
                        String pyOutOf = ETpyOutOf.getText().toString();
                        String dbmsMarks = ETdbmsMarks.getText().toString();
                        String dbmsOutOf = ETdbmsOutOf.getText().toString();

                        if(titleOfMarks.isEmpty() || mathsMarks.isEmpty() || mathsOutOf.isEmpty() || mpMarks.isEmpty() || mpOutOf.isEmpty() || osMarks.isEmpty() || osOutOf.isEmpty() || pyMarks.isEmpty() || pyOutOf.isEmpty() || dbmsMarks.isEmpty() || dbmsOutOf.isEmpty()){
                            Toast.makeText(MarksActivity.this, "Please enter necessary details", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(titleOfMarks.equals("IA1") || titleOfMarks.equals("IA2")){
                                progressDialog.show();
                                MarksModel model = new MarksModel(titleOfMarks, mathsMarks, mpMarks, osMarks, pyMarks, dbmsMarks);
                                model.setM4Out("20");
                                model.setMpOut("20");
                                model.setOsOut("20");
                                model.setPyOut("20");
                                model.setDbOut("20");

                                database.getReference().child("Marks").child(auth.getCurrentUser().getDisplayName()).child(titleOfMarks).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(MarksActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });

                            }
                            else if(titleOfMarks.equals("Semester")){
                                progressDialog.show();
                                MarksModel model = new MarksModel(titleOfMarks, mathsMarks, mpMarks, osMarks, pyMarks, dbmsMarks);
                                model.setM4Out("80");
                                model.setMpOut("80");
                                model.setOsOut("80");
                                model.setPyOut("80");
                                model.setDbOut("80");

                                database.getReference().child("Marks").child(auth.getCurrentUser().getDisplayName()).child(titleOfMarks).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(MarksActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                            else{
                                Toast.makeText(MarksActivity.this, "Please enter proper title", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        goBackFromMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MarksActivity.this, MainActivity.class));
            }
        });
    }
}