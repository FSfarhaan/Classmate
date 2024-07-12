package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.authentication.LoginActivity;
import com.example.finalproject.authentication.LoginAdmin;
import com.example.finalproject.authentication.LoginStudents;

public class ChooseToEnterActivity extends AppCompatActivity {
    ConstraintLayout enterAsStudent, enterAsAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_to_enter);

        enterAsStudent = findViewById(R.id.enterAsStudent);
        enterAsAdmin = findViewById(R.id.enterAsAdmin);

        Intent intent = new Intent(ChooseToEnterActivity.this, LoginActivity.class);

        enterAsStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Login Type", "Student");
                startActivity(intent);
            }
        });

        enterAsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Login Type", "Admin");
                startActivity(intent);
            }
        });
    }
}