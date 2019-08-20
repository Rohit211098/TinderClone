package com.example.tinder;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tinder.Matches.CoustomRecycle;
import com.example.tinder.Matches.MatchPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/28/2017.
 */

public class Tab3Fragment extends Fragment {
    private static final String TAG = "Tab3Fragment";
    private RecyclerView recyclerView;
    CoustomRecycle recycler;
    String currentUserId;
    private ArrayList<MatchPojo> resultMatch = new ArrayList<>();

    private Button btnTEST;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3_fragment,container,false);
        resultMatch.clear();
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recycler = new CoustomRecycle(getDataSet(),getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(recycler);
        swipeRefreshLayout = view.findViewById(R.id.swip);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                recyclerView.removeAllViews();
                resultMatch.clear();
                getUserMatch();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        recyclerView.removeAllViews();
        getUserMatch();
        return view;
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
