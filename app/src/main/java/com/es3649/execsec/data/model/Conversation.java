package com.es3649.execsec.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper for a list of messages
 *
 * Created by es3649 on 4/24/19.
 */

public class Conversation {
    private static final String TAG = "Conversation";
    private Person participant;
    private List<Message> conversation;


    public Conversation(){
        conversation = new ArrayList<Message>();
    }

    public int size() {
        return conversation.size();
    }

    public void addMessage(Message m) {
        conversation.add(m);
    }
}
