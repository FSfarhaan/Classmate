package com.example.finalproject.MainSix;

import static com.google.android.material.internal.ViewUtils.dpToPx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.ShowAvailableGroups;
import com.example.finalproject.adapters.ChatAdapter;
import com.example.finalproject.models.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    ImageView goBackFromGroup,sendMessageBtn;
    EditText etMessageG;
    RecyclerView RwOfGroup;
    FirebaseDatabase database;
    FirebaseAuth auth;
    TextView groupChatName;
    Toolbar toolbar;
    String adminName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final String senderId = FirebaseAuth.getInstance().getUid();
        goBackFromGroup = findViewById(R.id.goBackFromGroup);
        sendMessageBtn= findViewById(R.id.sendMessageBtn);
        etMessageG = findViewById(R.id.etMessageG);
        RwOfGroup = findViewById(R.id.RwOfGroup);
        toolbar = findViewById(R.id.toolbar);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        groupChatName = findViewById(R.id.groupChatName);
        ArrayList<MessageModel> list = new ArrayList<>();
        ChatAdapter adapter = new ChatAdapter(list, this);

        Intent intent = getIntent();
        String groupName = intent.getStringExtra("groupName");
        groupChatName.setText(groupName);

        RwOfGroup.setAdapter(adapter);
        RwOfGroup.setLayoutManager(new LinearLayoutManager(this));

        assert groupName != null;
        database.getReference().child("Group Chat").child(groupName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Handle the initial load of messages
                list.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    MessageModel model = messageSnapshot.getValue(MessageModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
                if (list.size() > 0) {
                    RwOfGroup.smoothScrollToPosition(list.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(GroupChatActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        goBackFromGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, ShowAvailableGroups.class));
                finish();
            }
        });

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String newDate = dateFormat.format(calendar.getTime());
                Log.d("newDate", newDate + Calendar.AM_PM);

                String message = etMessageG.getText().toString();
                MessageModel model = new MessageModel(senderId, message);
                model.setTimestamp(new Date().getTime());
                // model.setName(auth.getCurrentUser().getEmail().substring(0, auth.getCurrentUser().getEmail().indexOf('@')));
                model.setName(auth.getCurrentUser().getDisplayName());
                model.setCurrentTime(newDate);
                etMessageG.setText("");

                if(!message.equals("")) {
                    database.getReference().child("Group Chat").child(groupName).push().setValue(model)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    View rootView = findViewById(android.R.id.content);

                                    if(list.size() > 5) {
                                        rootView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                            @Override
                                            public boolean onPreDraw() {
                                                // Check if the keyboard is visible
                                                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                                                @SuppressLint("RestrictedApi") boolean isKeyboardVisible = heightDiff > dpToPx(ChatActivity.this, 200); // You may need to adjust this threshold

                                                // Perform actions based on keyboard visibility, e.g., scroll the RecyclerView
                                                if (isKeyboardVisible) {
                                                    RwOfGroup.smoothScrollToPosition(adapter.getItemCount() - 1);
                                                }

                                                return true;
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });

//        toolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(ChatActivity.this, GroupDetailActivity.class);
//                database.getReference().child("Groups").child(groupName).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        adminName = snapshot.child("adminName").getValue(String.class);
//                        if(adminName != null) {
//                            intent1.putExtra("adminName", adminName);
//                            startActivity(intent1);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
    }
}