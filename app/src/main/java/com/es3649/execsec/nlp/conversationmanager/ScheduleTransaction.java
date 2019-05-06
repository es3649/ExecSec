package com.es3649.execsec.nlp.conversationmanager;

import android.util.Log;

import com.es3649.execsec.data.model.Conversation;
import com.es3649.execsec.data.model.Person;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Date;

/**
 * Contains a scheduling transaction.
 * A scheduling transaction has a lot of data about an appointment which is currently
 * being scheduled.
 *
 * Created by es3649 on 4/24/19.
 */

public class ScheduleTransaction {
    private static final int ACTIVE_STATUS = 0;
    private static final int INACTIVE_STATUS = 1;
    private static final int STALE_STATUS = 2;
    private static final int CLOSED_STATUS = 3;

    private static final String TAG = "ScheduleTransaction";

    private Person withWho;
    private int status;
    private Date appointment;
    private Conversation conversation;

    /**
     * constructs a new transaction with the given person, the status is
     * set to ACTIVE by default
     * @param p the person related to this appointment
     */
    public ScheduleTransaction(Person p) {
        this.setWithWho(p);
        this.setStatus(ACTIVE_STATUS);
        conversation = new Conversation();
    }

    /**
     * Constructs a new conversation from a json-blob
     * @param json the json which holds the conversation object
     * @return a new conversation object from the json
     */
    public static ScheduleTransaction fromJson(String json) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, ScheduleTransaction.class);
        } catch (JsonSyntaxException ex) {
            Log.e(TAG, "Found bad json", ex);
            throw new IllegalArgumentException("Bad json", ex);
        }
    }

    /**
     * Serializes a conversation to json
     * @return this, but in json
     */
    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public Person getWithWho() {
        return withWho;
    }
    private void setWithWho(Person withWho) {
        this.withWho = withWho;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public Conversation getConversation() {
        return conversation;
    }
}
