package com.example.tinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tinder.Matches.MatchesActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 2/28/2017.
 */

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";

    private User userClass;
    private CustomArrayAdapter arrayAdapter;
    private int i;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    SwipeFlingAdapterView flingContainer;

    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private String defaultImageUrl = "data:image/jpeg;base64,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";
    ListView listView;
    String userSex,oppositeUserSex;
    List<User> rowItems;
    DatabaseReference userDB = db.child("Users");
    private FloatingActionButton cross,heart;
    private LinearLayout empty,notEmpty;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);

        empty = view.findViewById(R.id.empty);
        notEmpty =view.findViewById(R.id.not_empty);

        cross = view.findViewById(R.id.floating_left);
        heart = view.findViewById(R.id.floating_right);

        Log.e(TAG,user.getUid());

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logout();
//            }
//        });
        checkUserSex();




//        matches.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), MatchesActivity.class);
//                startActivity(intent);
//
//            }
//        });

        rowItems = new ArrayList<User>();


        arrayAdapter = new CustomArrayAdapter(getContext(),R.layout.item,rowItems);
         flingContainer =  view.findViewById(R.id.frame);



        flingContainer.setAdapter(arrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
//                // this is the simplest way to delete an object from the Adapter (/AdapterView)
//                Log.d("LIST", "removed object!");
//                rowItems.remove(0);
//                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                User obj = (User) dataObject;
                String userID = obj.getUserID();
                userDB.child(userID).child("Connections").child("no").child(user.getUid()).setValue(true);
                rowItems.remove(obj);
                isEmpty();

                makeToast(getContext(), "Left!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                User obj = (User) dataObject;
                String userID = obj.getUserID();
                userDB.child(userID).child("Connections").child("yes").child(user.getUid()).setValue(true);
                isConnectionMatch(userID);
                rowItems.remove(obj);
                isEmpty();
                makeToast(getContext(), "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here


            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rowItems.size() != 0){
                    int position = rowItems.size()-1;
                    User currentId = rowItems.get(position);
                    String id = currentId.getUserID();
                    userDB.child(id).child("Connections").child("no").child(user.getUid()).setValue(true);

                    arrayAdapter.remove(currentId);
                    arrayAdapter.notifyDataSetChanged();
                    isEmpty();

                    makeToast(getContext(), "Left!");
                }


            }
        });

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rowItems.size() != 0){
                    int position = rowItems.size()-1;
                    User currentId = rowItems.get(position);
                    String id = currentId.getUserID();
                    userDB.child(id).child("Connections").child("yes").child(user.getUid()).setValue(true);
                    isConnectionMatch(id);
                    arrayAdapter.remove(currentId);
                    arrayAdapter.notifyDataSetChanged();

                    isEmpty();

                    makeToast(getContext(), "Left!");
                }

            }
        });






        return view;
    }

    private void isEmpty(){
        if (rowItems.isEmpty()){


                notEmpty.setVisibility(View.INVISIBLE);
                empty.setVisibility(View.VISIBLE);

        }

    }

    private void isConnectionMatch(String userID) {

        DatabaseReference currentUserConnections = userDB.child(user.getUid()).child("Connections").child("yes").child(userID);
        currentUserConnections.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.e(TAG,dataSnapshot.getKey());
                    String  key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();
                    makeToast(getContext(), "Match!");

                    userDB.child(dataSnapshot.getKey()).child("Connections").child("matches").child(user.getUid()).child("chatId").setValue(key);


                    userDB.child(user.getUid()).child("Connections").child("matches").child(dataSnapshot.getKey()).child("chatId").setValue(key);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }






    private void checkUserSex(){
        DatabaseReference userDatabase = db.child("Users").child(user.getUid());
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if (dataSnapshot.child("sex").getValue() != null) {

                        userSex = dataSnapshot.child("sex").getValue().toString();
                        switch (userSex) {
                            case "Male":
                                oppositeUserSex = "Female";
                                break;
                            case "Female":
                                oppositeUserSex = "Male";
                                break;
                        }

                        displayOppositeSex();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    String userName;

    private void displayOppositeSex(){

        userDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()&&!dataSnapshot.child("Connections").child("no").hasChild(user.getUid())&&!dataSnapshot.child("Connections").child("yes").hasChild(user.getUid()) && dataSnapshot.child("sex").getValue().toString().equals(oppositeUserSex)){

                    try {
                        rowItems.add(new User(dataSnapshot.getKey(),dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("imageUrl").getValue().toString()));
                        arrayAdapter.notifyDataSetChanged();

                    }catch (Exception e){
                        rowItems.add(new User(dataSnapshot.getKey(),dataSnapshot.child("name").getValue().toString(),defaultImageUrl));
                        arrayAdapter.notifyDataSetChanged();
                    }

                }else {



                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



}
