package com.example.tinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Info extends AppCompatActivity {

    private ImageView  profilePic;
    private TextView nameAge,about;
    private String userId,mName,mAge,mProfilePic,mAbout;
    private FloatingActionButton back;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        profilePic = findViewById(R.id.info_profile_image);
        nameAge = findViewById(R.id.info_name);
        about = findViewById(R.id.info_about);
        back = findViewById(R.id.fab_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profilePic = findViewById(R.id.info_profile_image);

        userId = getIntent().getStringExtra("userid");

        db = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String,Object> map = ( Map<String,Object>)dataSnapshot.getValue();
                    if (map.get("name")!=null){
                        mName = map.get("name").toString();


                    }
                    if (map.get("age")!=null){
                        mAge = map.get("age").toString();


                    }

                    nameAge.setText(mName+", "+mAge);
                    if (map.get("about")!=null){
                        mAbout = map.get("about").toString();
                        about.setText(mAbout);

                    }
                    if (map.get("imageUrl")!=null){
                        mProfilePic = map.get("imageUrl").toString();

                        Glide.with(getApplicationContext()).load(mProfilePic).placeholder(R.drawable.download).apply(RequestOptions.circleCropTransform()).into(profilePic);
                    }else {
                        Glide.with(getApplicationContext()).load(R.drawable.noimage).placeholder(R.drawable.download).apply(RequestOptions.circleCropTransform()).into(profilePic);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
