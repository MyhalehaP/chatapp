package com.example.chat.Chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private ArrayList<MessageObject> messageList;

    public MessageAdapter(ArrayList<MessageObject> messageList){
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        return new MessageViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {
        holder.mMessage.setText(messageList.get(position).getMessage());
        holder.mSender.setText(messageList.get(position).getSenderId());

        if(messageList.get(holder.getAdapterPosition()).getMediaUrlList().isEmpty()){
            holder.mViewMedia.setVisibility(View.GONE);
        }

        holder.mViewMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageViewer.Builder(v.getContext(),messageList.get(holder.getAdapterPosition()).getMediaUrlList())
                        .setStartPosition(0).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("------>>>> ML Size: ", String.valueOf(messageList.size()));
        return messageList.size();

    }


    static class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView mMessage, mSender;
        LinearLayout mLayout;
        Button mViewMedia;

         MessageViewHolder (View view){
            super(view);
            mLayout = view.findViewById(R.id.layout);
            mMessage = view.findViewById(R.id.message);
            mSender = view.findViewById(R.id.sender);
            mViewMedia = view.findViewById(R.id.viewMedia);
         };
    }
}
