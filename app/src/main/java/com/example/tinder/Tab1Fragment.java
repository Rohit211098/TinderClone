package com.example.tinder;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    private ImageButton setting,update;
    private ImageView profilePicture;
    private TextView name;
    private FirebaseAuth auth;
    FirebaseUser mUserId;
    private DatabaseReference db;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);

        profilePicture =view.findViewById(R.id.user_profile_info);
        name = view.findViewById(R.id.user_profile_name);
        update = view.findViewById(R.id.user_profile_edit);
        setting = view.findViewById(R.id.user_profile_setting);

        auth = FirebaseAuth.getInstance();
        mUserId = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserId.getUid());
        getUserInfo();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Edit.class);
                startActivity(intent);
                getUserInfo();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });



        return view;
    }


    private void getUserInfo() {
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String,Object> map = ( Map<String,Object>)dataSnapshot.getValue();
                    if (map.get("name")!=null){

                        name.setText(map.get("name").toString());
                    }
                    if (map.get("imageUrl")!=null){
                        Glide.with(getContext()).load(map.get("imageUrl").toString()).placeholder(R.drawable.download).apply(RequestOptions.circleCropTransform()).into(profilePicture);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void logout(){
        auth.signOut();
        Intent intent = new Intent(getContext(),RegistrationAndLogin.class);

        startActivity(intent);
        onDestroy();

    }
}
