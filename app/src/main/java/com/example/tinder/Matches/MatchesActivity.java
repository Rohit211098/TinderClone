package com.example.tinder.Matches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    CoustomRecycle recycler;
    String currentUserId;
    private ArrayList<MatchPojo> resultMatch = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recycler = new CoustomRecycle(getDataSet(),getApplicationContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(recycler);

        getUserMatch();


    }

    private void getUserMatch() {

        DatabaseReference userrDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Connections").child("matches");
        userrDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot match :dataSnapshot.getChildren()){
                        fetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchMatchInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);

        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String mUserId = dataSnapshot.getKey();
                    String mUserName = "";
                    String mUserImageUrl = "";

                    if (dataSnapshot.child("name").getValue()!=null){
                            mUserName =dataSnapshot.child("name").getValue().toString();
                    }
                    if (dataSnapshot.child("imageUrl").getValue()!=null){
                        mUserImageUrl =dataSnapshot.child("imageUrl").getValue().toString();
                    }

                    resultMatch.add(new MatchPojo(mUserId,mUserName,mUserImageUrl));
                    recycler.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private List<MatchPojo> getDataSet() {
        return resultMatch;
    }
}
