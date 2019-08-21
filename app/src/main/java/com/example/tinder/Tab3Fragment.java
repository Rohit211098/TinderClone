package com.example.tinder;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tinder.Matches.CoustomRecycle;
import com.example.tinder.Matches.MatchPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private RecyclerView recyclerViewMessages,recyclerViewNewMatches;
    CoustomRecycle coustomRecycle;
    NewMatchesRecycle newMatchesRecycle;
    String currentUserId;
    private ArrayList<MatchPojo> resultMatch = new ArrayList<>();
    private ArrayList<MatchPojo> chatedResultMatch = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView textView;
    String defualtImage = "https://icon-library.net/images/no-profile-pic-icon/no-profile-pic-icon-12.jpg";



    private Button btnTEST;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3_fragment,container,false);
        resultMatch.clear();
        chatedResultMatch.clear();





        textView = view.findViewById(R.id.textView);
        progressBar = view.findViewById(R.id.progressBar_frag_3);
        recyclerViewNewMatches = view.findViewById(R.id.recycler_new_match);


        recyclerViewNewMatches.setNestedScrollingEnabled(false);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        coustomRecycle = new CoustomRecycle(chatedResultMatch,getContext());
        newMatchesRecycle = new NewMatchesRecycle(getContext(),getDataSet());


        recyclerViewNewMatches.setHasFixedSize(true);
        recyclerViewNewMatches.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));


        recyclerViewNewMatches.setAdapter(newMatchesRecycle);


        recyclerViewMessages = view.findViewById(R.id.recycler_messages);
        recyclerViewMessages.setNestedScrollingEnabled(false);
        recyclerViewMessages.setHasFixedSize(true);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMessages.setAdapter(coustomRecycle);

        progressBarVisible();


        swipeRefreshLayout = view.findViewById(R.id.swip);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerViewNewMatches.removeAllViews();
                recyclerViewMessages.removeAllViews();
                resultMatch.clear();
                chatedResultMatch.clear();
                getUserMatch();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        recyclerViewMessages.removeAllViews();
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
                        Log.e(TAG,"key "+ match.getKey() );
                    }
                }else {
                    textView.setText("No Matches");
                }

                progressBarInvisible();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    DatabaseReference chatDb =  FirebaseDatabase.getInstance().getReference().child("Chat");

    private void fetchMatchInformation(final  String key) {
        Log.e("a", "key pass " + key );
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        DatabaseReference chatIdRefrence = userDb.child("Connections").child("matches").child(currentUserId);

        chatIdRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("a", "onDataChange: " + dataSnapshot.getValue() );
                if(dataSnapshot.exists()){
                   String chatId = dataSnapshot.child("chatId").getValue().toString();
                    Log.e("a", "onDataChange: " + chatId );
                    chatId(chatId,key);



                }else {


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void chatId(final String chatId,final String key){
        chatDb.addListenerForSingleValueEvent(new ValueEventListener() {
            String lastMessage;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean haveChated;
                if (dataSnapshot.exists()){
                    Log.e("a", "onDataChange:  in " + dataSnapshot.getValue() );


                    if(dataSnapshot.child(chatId).exists()){
                        for (DataSnapshot match : dataSnapshot.getChildren()){
                            if (match.getKey().equals(chatId)){

                                for (DataSnapshot last : match.getChildren()){
                                    lastMessage = last.child("text").getValue().toString();
                                    Log.e("a", "yes 32132" + lastMessage );
                                }

                            }

                        }
                        haveChated=true;
                        Log.e("a", "yes " + chatId );


                    }else{
                        haveChated=false;
                        Log.e("a", "no " + chatId );
                    }



                }else {
                    haveChated=false;


                }
                chatMessage(haveChated,key,lastMessage);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void chatMessage(final Boolean haveChated, final String key,final  String lastMessage){
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.e("a", "onDataChange: rounnn");
                if (dataSnapshot.exists()){
                    String mUserId = dataSnapshot.getKey();
                    String mUserName = "";
                    String mUserImageUrl = "";

                    if (dataSnapshot.child("name").getValue()!=null){
                        mUserName =dataSnapshot.child("name").getValue().toString();
                        Log.e(TAG, "name user"+mUserName );
                    }
                    if (dataSnapshot.child("imageUrl").getValue()!=null){
                        mUserImageUrl =dataSnapshot.child("imageUrl").getValue().toString();
                    }else {
                        mUserImageUrl = defualtImage;
                    }

                    resultMatch.add(new MatchPojo(mUserId,mUserName,mUserImageUrl));
                    Log.e("a","have "+resultMatch.size());
                    if (haveChated){
                        chatedResultMatch.add(new MatchPojo(mUserId,mUserName,mUserImageUrl,lastMessage));

                    }

                    coustomRecycle.notifyDataSetChanged();
                    newMatchesRecycle.notifyDataSetChanged();
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

    private void progressBarVisible(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewMessages.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);



    }

    private void progressBarInvisible(){

        progressBar.setVisibility(View.INVISIBLE);
        recyclerViewMessages.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

    }
}
