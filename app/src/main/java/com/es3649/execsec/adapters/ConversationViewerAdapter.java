package com.es3649.execsec.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.es3649.execsec.data.model.Conversation;

/**
 * Defines view holders and adapts them for the ConversationViewerActivity's RecyclerView
 * Created by es3649 on 4/24/19.
 */

public class ConversationViewerAdapter extends RecyclerView.Adapter<ConversationViewerAdapter.ViewHolder> {
    // TODO we need to figure out how to do an upside down recycler:
    // TODO because message feeds start at the bottom and go up

    private Conversation conversation;

    public ConversationViewerAdapter(Conversation c) {
        conversation = c;
    }

    @Override
    public int getItemCount() {
        return conversation.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // TODO create a viewHolder Layout file
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // TODO create a viewHolder Layout file
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }
}
