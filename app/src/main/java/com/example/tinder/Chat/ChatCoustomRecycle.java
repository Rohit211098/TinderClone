package com.example.tinder.Chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tinder.R;

import java.util.List;

public class ChatCoustomRecycle extends RecyclerView.Adapter<ChatCoustomRecycle.ChatCoustomViewHolder> {

    List<ChatPojo> chatList;
    Context context;
    View view;


    public ChatCoustomRecycle(List<ChatPojo> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatCoustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);

        return new ChatCoustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatCoustomViewHolder holder, int position) {

        holder.message.setText(chatList.get(position).getMessage());

        if (chatList.get(position).getCurrentUser()){
            holder.linearLayout.setGravity(Gravity.END);
            holder.linearLayoutParent.setGravity(Gravity.END);
            holder.message.setTextColor(Color.parseColor("#404040"));
            holder.message.setBackground(view.getResources().getDrawable(R.drawable.rounded_rectangle_user));
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#F4F4F4"));
        }else {
            holder.linearLayout.setGravity(Gravity.START);
            holder.linearLayoutParent.setGravity(Gravity.START);
            holder.message.setTextColor(Color.parseColor("#FFFFFF"));
            holder.message.setBackground(view.getResources().getDrawable(R.drawable.rounded_rectangle_another_user));
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#2DB4C8"));
        }





    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    protected class ChatCoustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView message;
        private LinearLayout linearLayout,linearLayoutParent;

        public ChatCoustomViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);


            message = itemView.findViewById(R.id.message);
            linearLayout = itemView.findViewById(R.id.container);
            linearLayoutParent = itemView.findViewById(R.id.container_parent);

        }

        @Override
        public void onClick(View v) {



        }
    }
}
