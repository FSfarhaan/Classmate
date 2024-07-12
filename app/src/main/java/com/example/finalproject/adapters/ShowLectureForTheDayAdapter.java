package com.example.finalproject.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

public class ShowLectureForTheDayAdapter extends RecyclerView.Adapter<ShowLectureForTheDayAdapter.ViewHolder> {
    String[][] schedule;
    // String [][] practical;
    String [][] practicalA;
    String [][] practicalB;
    private static final int VIEW_TYPE_LECTURE = 0;
    private static final int VIEW_TYPE_BREAK = 1;
    Context context;
    Dialog dialog;

//    public ShowLectureForTheDayAdapter(Context context, String[][] schedule) {
//        this.schedule = schedule;
//        this.context = context;
//    }
//
//    public ShowLectureForTheDayAdapter(Context context, String[][] schedule, String[][] practical) {
//        this.schedule = schedule;
//        this.practical = practical;
//        this.context = context;
//    }

    public ShowLectureForTheDayAdapter(Context context, String[][] schedule, String[][] practicalA, String[][] practicalB) {
        this.schedule = schedule;
        this.practicalA = practicalA;
        this.practicalB = practicalB;
        this.context = context;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.practical_info);
    }

    @Override
    public int getItemViewType(int position) {
        if (schedule[position][1].equals("Break")) {
            return VIEW_TYPE_BREAK;
        } else {
            return VIEW_TYPE_LECTURE;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_BREAK) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.break_lecture_block, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecture_block, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textTime.setText(schedule[position][0]);
        holder.textSubject.setText(schedule[position][1]);
        holder.textProfessor.setText(schedule[position][2]);
        
        holder.lectureBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = schedule[holder.getAdapterPosition()][1];
                String time = schedule[holder.getAdapterPosition()][0];

                if(subject.equals("Practical")){
                    // Toast.makeText(context, "Practical time", Toast.LENGTH_SHORT).show();
                    if(time.equals("9.00-11.00") && practicalA != null) {
                        // Toast.makeText(context, practicalA[0][1], Toast.LENGTH_SHORT).show();
                        dialog.show();
                        TextView textView1 = dialog.findViewById(R.id.batchB1);
                        TextView textView2 = dialog.findViewById(R.id.batchB2);
                        TextView textView3 = dialog.findViewById(R.id.batchB3);
                        TextView textView4 = dialog.findViewById(R.id.batchB4);

                        // dialog.findViewById(R.id.batchB1).setText("hello");
                        textView1.setText(practicalA[0][0] + " " + practicalA[0][1] + " " + practicalA[0][2]);
                        textView2.setText(practicalA[1][0] + " " + practicalA[1][1] + " " + practicalA[1][2]);
                        textView3.setText(practicalA[2][0] + " " + practicalA[2][1] + " " + practicalA[2][2]);
                        textView4.setText(practicalA[3][0] + " " + practicalA[3][1] + " " + practicalA[3][2]);

                    }
                    else if(time.equals("11.00-1.00")) {
                        // Toast.makeText(context, practicalB[0][1], Toast.LENGTH_SHORT).show();
                        dialog.show();
                        TextView textView1 = dialog.findViewById(R.id.batchB1);
                        TextView textView2 = dialog.findViewById(R.id.batchB2);
                        TextView textView3 = dialog.findViewById(R.id.batchB3);
                        TextView textView4 = dialog.findViewById(R.id.batchB4);

                        // dialog.findViewById(R.id.batchB1).setText("hello");
                        textView1.setText(practicalB[0][0] + " " + practicalB[0][1] + " " + practicalB[0][2]);
                        textView2.setText(practicalB[1][0] + " " + practicalB[1][1] + " " + practicalB[1][2]);
                        textView3.setText(practicalB[2][0] + " " + practicalB[2][1] + " " + practicalB[2][2]);
                        textView4.setText(practicalB[3][0] + " " + practicalB[3][1] + " " + practicalB[3][2]);
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return schedule.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTime, textSubject, textProfessor;
        ConstraintLayout lectureBlock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTime = itemView.findViewById(R.id.timeOfLecture);
            textSubject = itemView.findViewById(R.id.NameOfLecture);
            textProfessor = itemView.findViewById(R.id.ProfessorName);
            lectureBlock = itemView.findViewById(R.id.lectureBlock);
        }
    }
}

