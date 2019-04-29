package com.es3649.execsec.nlp.conversationmanager;

import com.es3649.execsec.nlp.NLPIntent;

import java.util.List;

/**
 * Based on a current transaction state and a list of inferred intents (from a message)
 * this class suggests a response to the message.
 *
 * Created by es3649 on 4/28/19.
 */

public class Responder {
    public Responder() {};

    /**
     * This method does all the work for the class.
     * @param transaction a transaction to respond to
     * @param intents the intents inferred from the message to respond to
     * @return some form of suggested response
     */
    public String getResponse(ScheduleTransaction transaction, List<NLPIntent> intents) {
        return null;
    }
}
