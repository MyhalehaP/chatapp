package com.example.chat.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.BuildConfig;
import com.example.chat.ChatActivity;
import com.example.chat.R;
import com.example.chat.User.UserObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.AlgorithmParameterGenerator;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {
    ArrayList<ChatObject> chatList = new ArrayList<>();
    private String myName = "";
    public ChatListAdapter(ArrayList<ChatObject> chatList){
        this.chatList = chatList;
    }


    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        getMyName();
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        ChatListViewHolder rcv = new ChatListViewHolder(layoutView);

        return rcv;
    }

    private void getMyName() {
        String me = FirebaseAuth.getInstance().getUid();
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("user").child(me).child("name");
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())myName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListViewHolder holder, final int position) {
        
        StringBuilder title_bld = new StringBuilder();

        ArrayList<UserObject> User = chatList.get(position).getUserObjectArrayList();
        ArrayList<String> str = new ArrayList<>();

        for(UserObject s : User){
            if(!s.getNames().isEmpty()){
                str = s.getNames();
                break;
            }
        }

            for (String s : str) {
            title_bld.append(s);
            title_bld.append(", ");
            }

            if(str.size()> 1){
                title_bld.delete(title_bld.length()-2,title_bld.length()-1);
            }
        holder.mTitle.setText(title_bld.toString());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("chatObject", chatList.get(holder.getAdapterPosition()));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return chatList.size();
    }


    public class ChatListViewHolder extends RecyclerView.ViewHolder{

        public TextView mTitle;
        public LinearLayout mLayout;
        public ChatListViewHolder (View view){
            super(view);
            mTitle = view.findViewById(R.id.title);
            mLayout = view.findViewById(R.id.layout);
        };
    }
}
