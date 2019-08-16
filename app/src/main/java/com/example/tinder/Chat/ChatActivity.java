package com.example.tinder.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tinder.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mChatRecyclerView;
    ChatCoustomRecycle mChatAdapter;
    String currentUserId;
    private ArrayList<ChatPojo> resultChat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatRecyclerView = findViewById(R.id.chat_recycle);
        mChatRecyclerView.setNestedScrollingEnabled(false);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mChatAdapter = new ChatCoustomRecycle(getDataSetChat(),ChatActivity.this);

        mChatRecyclerView.setHasFixedSize(true);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mChatRecyclerView.setAdapter(mChatAdapter);



    }

    private List<ChatPojo> getDataSetChat() {
        return resultChat;
    }
}
