package com.example.finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.MessageModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    ArrayList<MessageModel> list;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<MessageModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_message_style, parent, false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_message_style, parent, false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())) return SENDER_VIEW_TYPE;
        else return RECEIVER_VIEW_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder) holder).senderText.setText(list.get(position).getMessage());
            // ((SenderViewHolder) holder).senderName.setText(list.get(position).getName());
            ((SenderViewHolder) holder).senderTime.setText(list.get(position).getCurrentTime());
        }
        else {
            ((ReceiverViewHolder) holder).receiverText.setText(list.get(position).getMessage());
            ((ReceiverViewHolder) holder).receiverName.setText(list.get(position).getName());
            ((ReceiverViewHolder) holder).receiverTime.setText(list.get(position).getCurrentTime());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView receiverName, receiverText, receiverTime;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverName = itemView.findViewById(R.id.thatName);
            receiverText = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderName, senderText, senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            // senderName = itemView.findViewById(R.id.thisName);
            senderText = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }
}

