package com.es3649.execsec.transaction;

import java.util.List;

/**
 * A wrapper for a list ov messages
 *
 * Created by es3649 on 4/24/19.
 */

public class Conversation {
    private List<Message> conversation;


    public int size() {
        return conversation.size();
    }
}
