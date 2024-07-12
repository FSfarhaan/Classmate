package com.example.finalproject.DrawerOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;

public class AboutActivity extends AppCompatActivity {
    ImageView instaFS, instaAS, instaSS, instaHS, liFS, liAS, liSS, liHS;
    ImageView goBackFromAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        instaFS = findViewById(R.id.instaFS);
        instaAS = findViewById(R.id.instaAS);
        instaSS = findViewById(R.id.instaSS);
        instaHS = findViewById(R.id.instaHS);
        liFS = findViewById(R.id.liFS);
        liAS = findViewById(R.id.liAS);
        liSS = findViewById(R.id.liSS);
        liHS = findViewById(R.id.liHS);

        goBackFromAbout = findViewById(R.id.goBackFromAbout);

        goBackFromAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutActivity.this, MainActivity.class));
            }
        });

        // <--------------- Opening Insta Accounts ---------------->
        instaFS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstagramProfile("the_farhaanshaikh?igsh=MW9vdWVqbGx4cTF5bg==");
            }
        });
        instaAS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstagramProfile("_aniket_11.11");
            }
        });
        instaSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstagramProfile("shravani_sawant2818");
            }
        });
        instaHS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstagramProfile("harshika.043");
            }
        });

        // <--------------- Opening LinkedIn Accounts ---------------->

        liFS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLinkedIn("farhaan-shaikh-422301252");
            }
        });


    }
    private void openInstagramProfile(String username) {
        // Construct the Instagram profile URL based on the username
        String url = "https://www.instagram.com/" + username;
        // Create an intent to open the Instagram profile
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Log.d("Bata kyu nahi chal raha", url);
        // Check if there's an app available to handle the intent
            // Open the Instagram profile
        startActivity(intent);
    }
    private void openLinkedIn(String username) {
        // Construct the Instagram profile URL based on the username
        String url = "https://www.linkedin.com/in/" + username;
        // Create an intent to open the Instagram profile
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setPackage("com.linkedin.android");
        startActivity(intent);
    }
}