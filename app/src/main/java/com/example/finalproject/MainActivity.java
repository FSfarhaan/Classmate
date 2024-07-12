package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finalproject.DrawerOptions.AboutActivity;
import com.example.finalproject.DrawerOptions.SearchStudentActivity;
import com.example.finalproject.DrawerOptions.SettingsActivity;
import com.example.finalproject.MainSix.AnnouncementActivity;
import com.example.finalproject.MainSix.AssignmentActivity;
import com.example.finalproject.MainSix.AttendanceActivity;
import com.example.finalproject.MainSix.MarksActivity;
import com.example.finalproject.MainSix.TimeTable;
import com.example.finalproject.authentication.LoginStudents;
import com.example.finalproject.models.AdminModel;
import com.example.finalproject.models.StudentModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menuButton, profImg;
    LinearLayout optionNotices, optionTimeTable, optionChat, optionAssignment, optionAttendance,optionMarks;
    LinearLayout userProfile;
    TextView userName, userStuID, nameOfProfile;
    NavigationView navigationView;
    FirebaseAuth auth;
    FirebaseDatabase database;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayoutOfMain);
        menuButton = findViewById(R.id.menuButton);
        optionNotices = findViewById(R.id.optionNotices);
        optionTimeTable = findViewById(R.id.optionTimeTable);
        optionChat = findViewById(R.id.optionChat);
        optionAssignment = findViewById(R.id.optionAssignment);
        optionAttendance = findViewById(R.id.optionAttendance);
        optionMarks = findViewById(R.id.optionMarks);
        navigationView = findViewById(R.id.navigationView);
        userProfile = findViewById(R.id.userProfile);
        userName = findViewById(R.id.userName);
        userStuID = findViewById(R.id.userStuID);
        profImg = findViewById(R.id.profImg);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        sharedPrefManager = new SharedPrefManager(this);
        Object user = sharedPrefManager.getUser();


        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        View headerLayout = LayoutInflater.from(this).inflate(R.layout.header_layout, null, false);

        // Access the TextView within the header layout
        TextView nameOfProfile = headerLayout.findViewById(R.id.nameOfProfile);
        // nameOfProfile.setText("Hello bhai");

        if (user instanceof StudentModel) {
            StudentModel storedStudent = (StudentModel) user;
            userName.setText(storedStudent.getFullName());
            userStuID.setText(storedStudent.getStudentId().toUpperCase());
        } else {
            AdminModel storedAdmin = (AdminModel) user;
            userName.setText(storedAdmin.getFullName());
            userStuID.setText(storedAdmin.getAdminId().toUpperCase());
        }

//        userName.setText(auth.getCurrentUser().getDisplayName());
//        String actualStudId = auth.getCurrentUser().getEmail();
//        int index = actualStudId.indexOf('@');
//        userStuID.setText(actualStudId.substring(0, index).toUpperCase());

        // StudentModel studentModel = FirebaseDatabase.getInstance().getReference().child("Students").child(FirebaseAuth.getInstance().getUid()).child("profilePic").getClass();

        database.getReference().child("Students").child(auth.getUid()).child("profilePic").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String imgUrl = snapshot.getValue(String.class);
                    Picasso.get().load(imgUrl).placeholder(R.drawable.avatar_img).into(profImg);
                }
                else profImg.setImageResource(R.drawable.avatar_img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // <----------Toggling of the side menu---------->
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        // <--------Navigating to the Profile Activity-------->
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                // intent.putExtra("stuId", actualStudId);
                intent.putExtra("accessCode", 76);
                startActivity(intent);
                // startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        // <--------Navigating to the Announcement Activity-------->
        optionNotices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnnouncementActivity.class);
                startActivity(intent);
            }
        });

        // <--------Navigating to the Timetable Activity-------->
        optionTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TimeTable.class);
                startActivity(intent);
            }
        });

        // <--------Navigating to the chat Activity-------->
        optionChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowAvailableGroups.class);
                startActivity(intent);
            }
        });

        // <--------Navigating to the Assignment Activity-------->
        optionAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AssignmentActivity.class);
                startActivity(intent);
            }
        });

        // <--------Navigating to the Attendance Activity-------->
        optionAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AttendanceActivity.class);
                startActivity(intent);
            }
        });

        // <--------Navigating to the Marks Activity-------->
        optionMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MarksActivity.class);
                startActivity(intent);
            }
        });
    }

    // <--------------- Handle Drawer Options ----------------->
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Handle item selection

        if(id == R.id.optSearch){
            startActivity(new Intent(MainActivity.this, SearchStudentActivity.class));
        }
        else if(id == R.id.optAbout){
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }
        else if(id == R.id.optSettings){
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
        else if(id == R.id.optHelp){

        }
        else if(id == R.id.optLogOut){
            startActivity(new Intent(MainActivity.this, LoginStudents.class));
            sharedPrefManager.clearUser();
            auth.signOut();
            finish();
        }

        drawerLayout.closeDrawers(); // Close the drawer after selecting the item
        return true;
    }
}