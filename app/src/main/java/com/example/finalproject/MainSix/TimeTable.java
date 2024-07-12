package com.example.finalproject.MainSix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.adapters.ShowLectureForTheDayAdapter;

import java.util.Calendar;

public class TimeTable extends AppCompatActivity {
    ShowLectureForTheDayAdapter adapter;
    RecyclerView recyclerView;
    ImageView goBackFromTimeTable;
    LinearLayout mondaySelector, tuesdaySelector, wednesdaySelector, thursdaySelector, fridaySelector;
    HorizontalScrollView horizontalScrollView;
    String [][] MondayPract = {
            {"B1", "SBLC Lab", "501"},
            {"B2", "MP Lab", "507"},
            {"B3", "AOA Lab", "601"},
            {"B4", "DBMS Lab", "402"}
    };
    String [][] WednesdayPractA = {
            {"B1", "OS Lab", "402"},
            {"B2", "SBLC Lab", "501"},
            {"B3", "MP Lab", "507"},
            {"B4", "AOA Lab", "601"}
    };
    String [][] WednesdayPractB = {
            {"B1", "MP Lab", "507"},
            {"B2", "AOA Lab", "601"},
            {"B3", "DBMS Lab", "402"},
            {"B4", "OS Lab", "508"}
    };
    String [][] ThursdayPractA = {
            {"B1", "DBMS Lab", "402"},
            {"B2", "OS Lab", "601"},
            {"B3", "SBLC Lab", "501"},
            {"B4", "MP Lab", "507"}
    };
    String [][] ThursdayPractB = {
            {"B1", "AOA Lab", "601"},
            {"B2", "DBMS Lab", "402"},
            {"B3", "OS Lab", "507"},
            {"B4", "SBLC Lab", "508"}
    };

    String [][] Monday = {
            {"9.00-10.00", "Maths-4 Tut (B4)", "Abhi Ruchi"},
            {"10.00-11.00", "Maths-4", "Abhi Ruchi"},
            {"11.00-1.00", "Practical", "Click for info"},
            {"1.00-1.45", "Break", ""},
            {"1.45-2.45", "DBMS", "Amol Pande"},
            {"2.45-3.45", "AOA", "Jyoti Gaikwad"},
            {"3.45-4.45", "MP", "Raju Mendhe"},
            {"4.45-5.45", "OS", "Vaishali Gaidhane"}
    };
    String [][] Tuesday = {
            {"9.00-11.00", "Mini Project", ""},
            {"11.00-1.00", "Mini Project", ""},
            {"1.00-1.45", "Break", ""},
            {"1.45-2.45", "AOA", "Jyoti Gaikwad"},
            {"2.45-3.45", "OS", "Vaishali Gaidhane"},
            {"3.45-4.45", "MP", "Raju Mendhe"},
            {"4.45-5.45", "Maths-4 Tut (B2)", "Abhi Ruchi"}
    };
    String [][] Wednesday = {
            {"9.00-11.00", "Practical", "Click for info"},
            {"11.00-1.00", "Practical", "Click for info"},
            {"1.00-1.45", "Break", ""},
            {"1.45-2.45", "OS", "Vaishali Gaidhane"},
            {"2.45-3.45", "DBMS", "Amol Pande"},
            {"3.45-4.45", "SBLC", "Rina Bora"},
            {"4.45-5.45", "Maths-4 Tut (B3)", "Abhi Ruchi"}
    };
    String [][] Thursday = {
            {"9.00-11.00", "Practical", "Click for info"},
            {"11.00-1.00", "Practical", "Click for info"},
            {"1.00-1.45", "Break", ""},
            {"1.45-2.45", "Maths-4", "Abhi Ruchi"},
            {"2.45-3.45", "OS", "Vaishali Gaidhane"}
    };
    String [][] Friday = {
            {"9.00-10.00", "Maths-4 Tut (B1)", "Abhi Ruchi"},
            {"10.00-11.00", "DBMS", "Amol Pande"},
            {"11.00-12.00", "SBLC", "Rina Bora"},
            {"12.00-1.00", "Maths-4", "Abhi Ruchi"},
            {"1.00-1.45", "Break", ""},
            {"1.45-2.45", "MP", "Raju Mendhe"},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        recyclerView = findViewById(R.id.rvOfTimeTable);
        goBackFromTimeTable = findViewById(R.id.goBackFromTimeTable);

        mondaySelector = findViewById(R.id.mondaySelector);
        tuesdaySelector = findViewById(R.id.tuesdaySelector);
        wednesdaySelector = findViewById(R.id.wednesdaySelector);
        thursdaySelector = findViewById(R.id.thursdaySelector);
        fridaySelector = findViewById(R.id.fridaySelector);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);

        // Handle click events for each LinearLayout

        adapter = new ShowLectureForTheDayAdapter(TimeTable.this, Monday, null, MondayPract);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mondaySelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mondaySelector.setBackgroundResource(R.drawable.background_profile_orange);
                tuesdaySelector.setBackgroundResource(R.drawable.background_edittext);
                wednesdaySelector.setBackgroundResource(R.drawable.background_edittext);
                thursdaySelector.setBackgroundResource(R.drawable.background_edittext);
                fridaySelector.setBackgroundResource(R.drawable.background_edittext);

                adapter = new ShowLectureForTheDayAdapter(TimeTable.this, Monday, null, MondayPract);
                recyclerView.setAdapter(adapter); // Set adapter back to RecyclerView
            }
        });

        tuesdaySelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mondaySelector.setBackgroundResource(R.drawable.background_edittext);
                tuesdaySelector.setBackgroundResource(R.drawable.background_profile_orange);
                wednesdaySelector.setBackgroundResource(R.drawable.background_edittext);
                thursdaySelector.setBackgroundResource(R.drawable.background_edittext);
                fridaySelector.setBackgroundResource(R.drawable.background_edittext);

                adapter = new ShowLectureForTheDayAdapter(TimeTable.this, Tuesday, null, null);
                recyclerView.setAdapter(adapter); // Set adapter back to RecyclerView
            }
        });

        wednesdaySelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mondaySelector.setBackgroundResource(R.drawable.background_edittext);
                tuesdaySelector.setBackgroundResource(R.drawable.background_edittext);
                wednesdaySelector.setBackgroundResource(R.drawable.background_profile_orange);
                thursdaySelector.setBackgroundResource(R.drawable.background_edittext);
                fridaySelector.setBackgroundResource(R.drawable.background_edittext);

                adapter = new ShowLectureForTheDayAdapter(TimeTable.this, Wednesday, WednesdayPractA, WednesdayPractB);
                recyclerView.setAdapter(adapter); // Set adapter back to RecyclerView
            }
        });

        thursdaySelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mondaySelector.setBackgroundResource(R.drawable.background_edittext);
                tuesdaySelector.setBackgroundResource(R.drawable.background_edittext);
                wednesdaySelector.setBackgroundResource(R.drawable.background_edittext);
                thursdaySelector.setBackgroundResource(R.drawable.background_profile_orange);
                fridaySelector.setBackgroundResource(R.drawable.background_edittext);

                adapter = new ShowLectureForTheDayAdapter(TimeTable.this, Thursday, ThursdayPractA, ThursdayPractB);
                recyclerView.setAdapter(adapter); // Set adapter back to RecyclerView
            }
        });

        fridaySelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mondaySelector.setBackgroundResource(R.drawable.background_edittext);
                tuesdaySelector.setBackgroundResource(R.drawable.background_edittext);
                wednesdaySelector.setBackgroundResource(R.drawable.background_edittext);
                thursdaySelector.setBackgroundResource(R.drawable.background_edittext);
                fridaySelector.setBackgroundResource(R.drawable.background_profile_orange);

                adapter = new ShowLectureForTheDayAdapter(TimeTable.this, Friday, null, null);
                recyclerView.setAdapter(adapter); // Set adapter back to RecyclerView
            }
        });

        goBackFromTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TimeTable.this, MainActivity.class));
            }
        });
    }

}