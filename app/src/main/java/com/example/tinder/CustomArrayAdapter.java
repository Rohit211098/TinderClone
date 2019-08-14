package com.example.tinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<User> {

    Context context;

    public CustomArrayAdapter(Context context, int resourceId, List<User> items) {
        super(context,resourceId,items);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user_item = getItem(position);

        if (convertView ==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item,parent,false);
        }

        TextView name = convertView.findViewById(R.id.name_person);
        ImageView imageView = convertView.findViewById(R.id.image_person);

        name.setText(user_item.getName());
        imageView.setImageResource(R.drawable.download);


        return convertView;
    }
}
