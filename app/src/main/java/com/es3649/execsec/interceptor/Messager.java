package com.es3649.execsec.interceptor;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

/**
 * This class can send SMS messages!
 * It also handles all the overhead, wow!
 *
 * Also, this is a singleton
 *
 * Created by es3649 on 4/28/19.
 */

public class Messager {
    public Messager() {}

    /**
     * This is a pretty basic SMS sender. I don't think it's great
     *
     * @param ctx some context
     * @param number a phone number to send it to
     * @param message the message to send
     */
    public void sendMessage(Context ctx, String number, String message) {
        PendingIntent pi = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, Messager.class), 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, message, pi, null);
    }
}
