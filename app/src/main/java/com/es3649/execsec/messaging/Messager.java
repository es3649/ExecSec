package com.es3649.execsec.messaging;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.es3649.execsec.data.model.Person;

/**
 * This class can send SMS messages!
 * It also handles all the overhead, wow!
 *
 * Also, this is a singleton
 *
 * Created by es3649 on 4/28/19.
 */

public class Messager {

    public Messager(Context ctx) {
        this.ctx = ctx;
    }

    private static final String FIRST_NAME_TAG = "%name";

    private Context ctx;

    /**
     * This is a pretty basic SMS sender. I don't think it's great
     *
     * @param number a phone number to send it to
     * @param message the message to send
     */
    public void sendMessage(String number, String message) {
        PendingIntent pi = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, Messager.class), 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, message, pi, null);
    }

    /**
     * Does the hard work of replacing tags with values
     *
     * Currently supported tags are:
     * `%name` -> person's first name
     *
     * @param text the text on which to make the replacements
     * @param p the person whose info will be subbed in
     * @return the modified text
     */
    public static String doReplacements(String text, Person p) {
        StringBuilder sbText = new StringBuilder(text);

        int pos = sbText.indexOf(FIRST_NAME_TAG);
        while (pos > -1) {
            sbText.replace(pos, pos + FIRST_NAME_TAG.length(), p.getName());
            pos = sbText.indexOf(FIRST_NAME_TAG);
        }

        return sbText.toString();
    }
}
