package com.es3649.execsec.dao.model;

import java.util.Date;

/**
 * Defines a message in a conversation
 *
 * Created by es3649 on 4/24/19.
 */

public class Message {
    private boolean isFromMe;
    private String messageContent;
    private Date time;
    // TODO intents inferred from messages need to be linked to this somehow.
}
