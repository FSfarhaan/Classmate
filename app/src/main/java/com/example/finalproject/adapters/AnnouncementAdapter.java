package com.example.finalproject.adapters;

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
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {
    Context context;
    ArrayList<AnnouncementModel> arrayList;

    public AnnouncementAdapter(Context context, ArrayList<AnnouncementModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.announcement_node, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       AnnouncementModel announcement = arrayList.get(position);

       holder.titleOfAnnouncement.setText(announcement.getTitle());
       holder.nameOfAnnouncer.setText(announcement.getName());
       holder.timeOfAnnouncement.setText(announcement.getTime());
       holder.contentOfAnnouncement.setText(announcement.getContent());

//        if (announcement.getSenderImg() != null && !announcement.getSenderImg().isEmpty()) {
//            Picasso.get().load(announcement.getSenderImg()).placeholder(R.drawable.avatar_img).into(holder.senderImgOfAnnouncer);
//        } else {
//            holder.senderImgOfAnnouncer.setImageResource(R.drawable.avatar_img);
//        }
//
        if (announcement.getImgUrl() != null && !announcement.getImgUrl().isEmpty()) {
            holder.imgOfAnnouncement.setVisibility(View.VISIBLE);
            Picasso.get().load(announcement.getImgUrl()).placeholder(R.drawable.add_image_icon).into(holder.imgOfAnnouncement);
        } else {
            holder.imgOfAnnouncement.setVisibility(View.GONE);
        }

        if(announcement.getSenderImg() != null){
            // holder.senderImgOfAnnouncer.setImageResource(announcement.getSenderImg());
            Picasso.get().load(announcement.getSenderImg()).into(holder.senderImgOfAnnouncer);
        }
        else {
            holder.senderImgOfAnnouncer.setImageResource(R.drawable.avatar_img);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleOfAnnouncement, nameOfAnnouncer, timeOfAnnouncement, contentOfAnnouncement;
        ImageView senderImgOfAnnouncer, imgOfAnnouncement;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOfAnnouncement = itemView.findViewById(R.id.titleOfAnnouncement);
            nameOfAnnouncer = itemView.findViewById(R.id.nameOfAnnouncer);
            timeOfAnnouncement = itemView.findViewById(R.id.timeOfAnnouncement);
            contentOfAnnouncement = itemView.findViewById(R.id.contentOfAnnouncement);
            senderImgOfAnnouncer = itemView.findViewById(R.id.senderImgOfAnnouncer);
            imgOfAnnouncement = itemView.findViewById(R.id.imgOfAnnouncement);
        }
    }
}
