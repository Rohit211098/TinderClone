package com.example.tinder.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tinder.MatchPojo;
import com.example.tinder.R;

import java.util.List;

public class ChatCoustomRecycle extends RecyclerView.Adapter<ChatCoustomRecycle.ChatCoustomViewHolder> {

    List<ChatPojo> chatList;
    Context context;

    public ChatCoustomRecycle(List<ChatPojo> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatCoustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);

        return new ChatCoustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatCoustomViewHolder holder, int position) {





    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    protected class ChatCoustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public ChatCoustomViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {



        }
    }
}
