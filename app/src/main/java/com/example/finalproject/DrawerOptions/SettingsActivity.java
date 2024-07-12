package com.example.finalproject.DrawerOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.finalproject.MainActivity;
import com.example.finalproject.ProfileActivity;
import com.example.finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SettingsActivity extends AppCompatActivity {
    ImageView toggleDarkMode, toChangeProf;
    ImageView goBackFromSettings;
    LinearLayout goToEditProfile;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toggleDarkMode = findViewById(R.id.toggleDarkMode);
        goBackFromSettings = findViewById(R.id.goBackFromSettings);
        goToEditProfile = findViewById(R.id.goToEditProfile);
        toChangeProf = findViewById(R.id.toChangeProf);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        database.getReference().child("Students").child(auth.getUid()).child("profilePic").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String imgUrl = snapshot.getValue(String.class);
                    Picasso.get().load(imgUrl).placeholder(R.drawable.avatar_img).into(toChangeProf);
                }
                else{
                    toChangeProf.setImageResource(R.drawable.avatar_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        goToEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
                intent.putExtra("accessCode", 56);
                startActivity(intent);
            }
        });


        final boolean[] isDarkModeOn = {false};
        toggleDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDarkModeOn[0] = !isDarkModeOn[0];

                // Set the drawable based on the state
                if (isDarkModeOn[0]) {
                    toggleDarkMode.setImageResource(R.drawable.switch_on_icon);
                } else {
                    toggleDarkMode.setImageResource(R.drawable.switch_off_icon);
                }
            }
        });

        goBackFromSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            }
        });
    }
}