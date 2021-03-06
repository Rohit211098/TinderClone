package com.example.tinder.Matches;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tinder.Chat.ChatActivity;
import com.example.tinder.R;

import java.util.List;

public class CoustomRecycle extends RecyclerView.Adapter<CoustomRecycle.CoustomViewHolder> {

    List<MatchPojo> list;
    Context context;


    public CoustomRecycle(List<MatchPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CoustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);

        return new CoustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoustomViewHolder holder, int position) {


        Log.e("tag","in custom "+list.get(position).getMatchId());
        holder.matchName.setText(list.get(position).getMatchName());
        holder.lastMessage.setText(list.get(position).getLastMessage());
        Glide.with(context).load(list.get(position).getMatchImage()).placeholder(R.drawable.download).apply(RequestOptions.circleCropTransform()).into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class CoustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageView;
        private TextView matchName,lastMessage;


        public CoustomViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.image);

            matchName = itemView.findViewById(R.id.matchid_name);
            lastMessage = itemView.findViewById(R.id.matchid_lastchat);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(), ChatActivity.class);
            Bundle bundle= new Bundle();
            bundle.putString("MatchId",list.get(getAdapterPosition()).getMatchId());
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);

        }
    }
}
