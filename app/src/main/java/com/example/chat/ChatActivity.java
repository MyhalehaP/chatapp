package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chat.Chat.ChatObject;
import com.example.chat.Chat.MessageAdapter;
import com.example.chat.Chat.MessageObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mChat;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    String chatID;
    DatabaseReference mChatDb;
    ArrayList<MessageObject> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatID = getIntent().getExtras().getString("chatID");
        mChatDb = FirebaseDatabase.getInstance().getReference().child("chat").child(chatID);
        Button mSend = findViewById(R.id.send_message);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        initializeRecyclerView();
        getChatMessages();

    }

    private void getChatMessages() {
        mChatDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String text = "test", creatorID = "test";

                    if(dataSnapshot.child("text").getValue() != null)
                        text = dataSnapshot.child("text").getValue().toString();

                    if(dataSnapshot.child("creator").getValue() != null)
                        creatorID = dataSnapshot.child("creator").getValue().toString();


                    MessageObject mMessage = new MessageObject(dataSnapshot.getKey(),creatorID,text);
                    messageList.add(mMessage);
                    mChatLayoutManager.scrollToPosition(messageList.size()-1);
                    mChatAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void sendMessage(){
        EditText mMessage = findViewById(R.id.new_message);
        if(!mMessage.getText().toString().isEmpty()){

            DatabaseReference newMessageDB  = mChatDb.push();

            Map newMessageMap = new HashMap<>();
            newMessageMap.put("text",mMessage.getText().toString());
            newMessageMap.put("creator", FirebaseAuth.getInstance().getUid());

            newMessageDB.updateChildren(newMessageMap);
        }
        mMessage.setText(null);
    }

    private void initializeRecyclerView() {
        messageList = new ArrayList<>();
        mChat = findViewById(R.id.messageList);
        mChat.setNestedScrollingEnabled(false);
        mChat.setHasFixedSize(false);

        mChatLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mChat.setLayoutManager(mChatLayoutManager);

        mChatAdapter = new MessageAdapter(messageList);
        mChat.setAdapter(mChatAdapter);


    }


}