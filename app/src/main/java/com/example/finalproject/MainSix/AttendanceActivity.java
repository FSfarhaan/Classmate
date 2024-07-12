package com.example.finalproject.MainSix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.graphics.Color;
import android.widget.TextView;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class AttendanceActivity extends AppCompatActivity {

    ImageView goBackFromAttendance, addAttendanceBtn;
    PieChart pieChart;
    private TextView monthTextView;
    private ImageView leftArrowImageView, rightArrowImageView;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        goBackFromAttendance = findViewById(R.id.goBackFromAttendance);
        addAttendanceBtn = findViewById(R.id.addAttendanceBtn);
        monthTextView = findViewById(R.id.monthTextView);
        leftArrowImageView = findViewById(R.id.leftArrowImageView);
        rightArrowImageView = findViewById(R.id.rightArrowImageView);
        calendar = Calendar.getInstance();
        updateMonthText();

        rightArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                updateMonthText();
            }
        });

        leftArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                updateMonthText();
            }
        });

        // recyclerView = findViewById(R.id.rvOfAttendance);

        int totalSubjects = 5;
        int totalLecturesPerSubject = 20;

        int[] attendedLectures = {14, 12, 16, 8, 10}; // Example data

        int totalAttended = 0;
        for (int lectures : attendedLectures) {
            totalAttended += lectures;
        }

        float attendancePercentage = (float) totalAttended / (totalSubjects * totalLecturesPerSubject) * 100;

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        for (int i = 0; i < totalSubjects; i++) {
            // Add present lectures entry
            entries.add(new PieEntry(attendedLectures[i], ""));
            colors.add(getColorForSubject(i));

            // Add absent lectures entry next to present lectures
            int absentLectures = totalLecturesPerSubject - attendedLectures[i];
            entries.add(new PieEntry(absentLectures, ""));
            colors.add(getDarkerColorForSubject(i));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.DEFAULT_BOLD);

        pieChart = findViewById(R.id.pieChart);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(35f);
        pieChart.setHoleColor(Color.BLACK);
        pieChart.animateY(1000);
        pieChart.invalidate();

        pieChart.setCenterText(String.format("%.0f%% Attendance", attendancePercentage));
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setCenterTextSize(28);
        pieChart.setCenterTextColor(Color.WHITE);

        pieChart.getLegend().setEnabled(false);

        // attendanceText.setText(String.format("You attended %d out of %d lectures", attendedLectures, totalLectures));

        goBackFromAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendanceActivity.this, MainActivity.class));
            }
        });


    }

    private int getColorForSubject(int subjectIndex) {
        switch (subjectIndex) {
            case 0:
                return Color.BLUE; // Maths
            case 1:
                return Color.GREEN; // Microprocessor
            case 2:
                return Color.RED; // OS
            case 3:
                return Color.MAGENTA; // SBLC
            case 4:
                return Color.CYAN; // DBMS
            default:
                return Color.WHITE; // Default color
        }
    }

    // Method to get a darker shade of color for absent lectures
    private int getDarkerColorForSubject(int subjectIndex) {
        int color = getColorForSubject(subjectIndex);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.7f; // Decrease brightness
        return Color.HSVToColor(hsv);
    }

    private void updateMonthText() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.getDefault());
        String month = sdf.format(calendar.getTime());
        monthTextView.setText(month);
    }
}