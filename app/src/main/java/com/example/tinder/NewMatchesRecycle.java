package com.example.tinder;

import android.content.Context;
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
import com.example.tinder.Matches.MatchPojo;

import java.util.List;

public class NewMatchesRecycle extends RecyclerView.Adapter<NewMatchesRecycle.NewMatchViewHolder> {

    private Context context;
    List<MatchPojo> list;

    public NewMatchesRecycle(Context context, List<MatchPojo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NewMatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_match_recycler,parent,false);

        return new NewMatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewMatchViewHolder holder, int position) {

        holder.profileName.setText(list.get(position).getMatchName());
        Log.e("tag","in newmatchrecycler "+list.get(position));

        Glide.with(context).load(list.get(position).getMatchImage()).placeholder(R.drawable.download).apply(RequestOptions.circleCropTransform()).into(holder.profileImage);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class NewMatchViewHolder extends RecyclerView.ViewHolder{

        private ImageView profileImage;
        private TextView profileName;

        public NewMatchViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.new_match_recycler_profile_image);
            profileName = itemView.findViewById(R.id.new_match_recycler_name);

        }
    }


}
