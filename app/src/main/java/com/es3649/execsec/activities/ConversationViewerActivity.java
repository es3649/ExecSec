package com.es3649.execsec.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.es3649.execsec.R;
import com.es3649.execsec.adapters.ConversationViewerAdapter;

public class ConversationViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_viewer);

        // wire up recyclerView
        RecyclerView recyclerView = findViewById(R.id.cvaRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        // TODO use intents (probably) to figure out which message goes here
        recyclerView.setAdapter(new ConversationViewerAdapter(null));

    }
}
