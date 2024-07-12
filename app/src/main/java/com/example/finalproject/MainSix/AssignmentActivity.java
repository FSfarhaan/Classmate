package com.example.finalproject.MainSix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.adapters.AssignmentAdapter;
import com.example.finalproject.models.AssignmentModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

public class AssignmentActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AssignmentAdapter adapter;
    ImageView addAssignmentBtn, goBackFromAssignment;
    ArrayList<AssignmentModel> arrayList = new ArrayList<>();
    Dialog dialog;
    EditText titleOfFile;
    Button btnSelectFile, btnUploadFile;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    Uri selectedFileUri; // Added variable to store selected file URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        recyclerView = findViewById(R.id.rvOfAssignment);
        addAssignmentBtn = findViewById(R.id.addAssignmentBtn);
        goBackFromAssignment = findViewById(R.id.goBackFromAssignment);
        dialog = new Dialog(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        dialog.setContentView(R.layout.dialog_add_assignment);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setMessage("Please wait for a moment");

//        arrayList.add(new AssignmentModel("AOA", "20-3-24"));
//        arrayList.add(new AssignmentModel("MP", "24-3-24"));
//        arrayList.add(new AssignmentModel("DBMS", "20-4-24"));
//        arrayList.add(new AssignmentModel("OS", "13-4-24"));
//        arrayList.add(new AssignmentModel("Python", "20-3-24"));


        database.getReference().child("pdfs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot pdfSnapshot: snapshot.getChildren()) {
                    AssignmentModel model = pdfSnapshot.getValue(AssignmentModel.class);
                    arrayList.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new AssignmentAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addAssignmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                titleOfFile = dialog.findViewById(R.id.titleOfFile);
                btnSelectFile = dialog.findViewById(R.id.btnSelectFile);
                btnUploadFile = dialog.findViewById(R.id.btnUploadFile);

                btnUploadFile.setVisibility(View.INVISIBLE);

                btnSelectFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/pdf | application/msword | application/vnd.ms-powerpoint");
                        startActivityForResult(Intent.createChooser(intent, "Select PDF"), 33);
                    }
                });
            }
        });

        goBackFromAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AssignmentActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 33 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedFileUri = data.getData();
            // Update UI based on selected file
            titleOfFile.setText(getFileNameFromUri(selectedFileUri));
            btnUploadFile.setVisibility(View.VISIBLE);
            btnSelectFile.setVisibility(View.INVISIBLE);

            btnUploadFile.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Check if a file is selected
                    if (selectedFileUri != null) {
                        progressDialog.show();
                        uploadPDF(selectedFileUri);
                    } else {
                        Toast.makeText(AssignmentActivity.this, "Please select a file first", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void uploadPDF(Uri pdfUri) {
        String fileName = getFileNameFromUri(pdfUri);
        int index = fileName.indexOf('.');
        String ogName = fileName.substring(0, index);
        StorageReference pdfRef = storageReference.child("pdfs/" + fileName);
        pdfRef.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl  = uri.toString();
                        AssignmentModel model = new AssignmentModel(ogName, downloadUrl);
                        model.setTimestamp(new Date().getTime());
                        database.getReference().child("pdfs").child(ogName).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AssignmentActivity.this, "File uploaded", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });

                    }
                });

            }
        });
    }

    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            assert result != null;
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AssignmentActivity.this, MainActivity.class));
        finish();
    }
}
