package com.example.tinder.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    public static String TAG = "tag";

    private RecyclerView mChatRecyclerView;
    ChatCoustomRecycle mChatAdapter;
    String currentUserId,matchId;
    private ArrayList<ChatPojo> resultChat = new ArrayList<>();
    private EditText sendEditText;
    private Button send;
    private DatabaseReference mDatabase,mDatabaseChat;
    private String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        sendEditText = findViewById(R.id.message_send);
        send = findViewById(R.id.send_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        matchId = getIntent().getExtras().getString("MatchId");

        mChatRecyclerView = findViewById(R.id.chat_recycle);
        mChatRecyclerView.setNestedScrollingEnabled(false);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Connections").child("matches").child(matchId);
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");
        mChatAdapter = new ChatCoustomRecycle(getDataSetChat(),ChatActivity.this);

        mChatRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mChatRecyclerView.setLayoutManager(linearLayoutManager);

        mChatRecyclerView.setAdapter(mChatAdapter);
        getChatId();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage();
            }
        });



    }

    private void sendMessage() {


        String sendMessageText = sendEditText.getText().toString();

        if (!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();

            Map newMessage = new HashMap();
            newMessage.put("createdByUser",currentUserId);
            newMessage.put("text",sendMessageText);

            newMessageDb.setValue(newMessage);

        }

        sendEditText.setText(null);


    }

    private void  getChatId(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String,Object> map = ( Map<String,Object>)dataSnapshot.getValue();
                    if (map.get("chatId")!=null){
                        chatId = map.get("chatId").toString();

                        mDatabaseChat = mDatabaseChat.child(chatId);

                        getChatMessages();
                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getChatMessages() {

        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String message =null;
                    String createdByUser = null;

                    if(dataSnapshot.child("text").getValue()!= null){
                        message =dataSnapshot.child("text").getValue().toString();

                    }

                    if( dataSnapshot.child("createdByUser").getValue()!= null){

                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }

                    if (message != null && createdByUser != null){
                        Boolean currentUserBoolean =false;
                        if (createdByUser.equals(currentUserId)){
                            currentUserBoolean = true;
                        }

                        ChatPojo chatPojo = new ChatPojo(message,currentUserBoolean);
                        resultChat.add(chatPojo);
                        mChatRecyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);
                        mChatAdapter.notifyDataSetChanged();
                    }
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

    private List<ChatPojo> getDataSetChat() {
        return resultChat;
    }
}
