package com.example.finalproject.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalproject.R;
import com.example.finalproject.models.AnnouncementModel;
import com.example.finalproject.models.MarksModel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.ViewHolder> {
    Context context;
    ArrayList<MarksModel> arrayList;

    public MarksAdapter(Context context, ArrayList<MarksModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.marks_node, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MarksModel model = arrayList.get(position);
        holder.titleOfMarks.setText(model.getTitle());

        holder.mathsTotal.setText(model.getM4Marks());
        holder.mpTotal.setText(model.getMpMarks());
        holder.osTotal.setText(model.getOsMarks());
        holder.pyTotal.setText(model.getPyMarks());
        holder.dbmsTotal.setText(model.getDbMarks());

        holder.mathsOut.setText(model.getM4Out());
        holder.mpOut.setText(model.getMpOut());
        holder.osOut.setText(model.getOsOut());
        holder.pyOut.setText(model.getPyOut());
        holder.dbmsOut.setText(model.getDbOut());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleOfMarks;
        TextView mathsTotal, mpTotal, osTotal, pyTotal, dbmsTotal;
        TextView mathsOut, mpOut, osOut, pyOut, dbmsOut;
        ImageView editMarks;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOfMarks = itemView.findViewById(R.id.titleOfMarks);
            editMarks = itemView.findViewById(R.id.editMarks);

            mathsTotal = itemView.findViewById(R.id.mathsTotal);
            mpTotal = itemView.findViewById(R.id.mpTotal);
            osTotal = itemView.findViewById(R.id.osTotal);
            pyTotal = itemView.findViewById(R.id.pyTotal);
            dbmsTotal = itemView.findViewById(R.id.dbmsTotal);

            mathsOut = itemView.findViewById(R.id.mathsOutOf);
            mpOut = itemView.findViewById(R.id.mpOutOf);
            osOut = itemView.findViewById(R.id.osOutOf);
            pyOut = itemView.findViewById(R.id.pyOutOf);
            dbmsOut = itemView.findViewById(R.id.dbmsOutOf);


        }
    }
}
