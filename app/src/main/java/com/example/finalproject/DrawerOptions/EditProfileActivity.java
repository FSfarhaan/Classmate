package com.example.finalproject.DrawerOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;

public class EditProfileActivity extends AppCompatActivity {

    ImageView goBackFromEditProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        goBackFromEditProfile = findViewById(R.id.goBackFromEditProfile);

        goBackFromEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, SettingsActivity.class));
            }
        });
    }
}