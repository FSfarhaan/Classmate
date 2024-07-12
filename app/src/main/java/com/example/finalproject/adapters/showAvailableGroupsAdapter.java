package com.example.finalproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.MainSix.ChatActivity;
import com.example.finalproject.R;
import com.example.finalproject.models.showAvailableGroupsModel;

import java.util.ArrayList;

public class showAvailableGroupsAdapter extends RecyclerView.Adapter<showAvailableGroupsAdapter.ViewHolder> {
    Context context;
    ArrayList<showAvailableGroupsModel> arrayList;

    public showAvailableGroupsAdapter(Context context, ArrayList<showAvailableGroupsModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.available_groups_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img.setImageResource(arrayList.get(position).image);
        holder.groupName.setText(arrayList.get(position).groupName);
        holder.groupText.setText(arrayList.get(position).groupText);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("groupName", holder.groupName.getText().toString());
                context.startActivity(intent);
            }
        });

//        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context)
//                        .setTitle("Deleting the group")
//                        .setMessage("Are you sure about this")
//                        .setPositiveButton("yes", (dialog, which) -> {
//                            arrayList.remove(holder.getAdapterPosition());
//                            notifyItemRemoved(holder.getAdapterPosition());
//                            notifyItemRangeChanged(position, arrayList.size());
//
//                            String groupName = holder.groupName.getText().toString();
//                            db.deleteGroup(groupName);
//                            if(arrayList.isEmpty()) emptyArraylistMessage.setVisibility(View.VISIBLE);
//                            Toast.makeText(context, "Deleting", Toast.LENGTH_SHORT).show();
//                        })
//                        .setNegativeButton("No", (dialog, which) -> {
//                            Toast.makeText(context, "Nahi delete karta bas", Toast.LENGTH_SHORT).show();
//                        });
//                builder.show();
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView groupName, groupText;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.groupImgRV);
            groupName = itemView.findViewById(R.id.groupNameRV);
            groupText = itemView.findViewById(R.id.groupTextRV);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
