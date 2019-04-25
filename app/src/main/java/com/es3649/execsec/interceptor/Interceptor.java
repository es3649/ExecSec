package com.es3649.execsec.interceptor;

import android.app.NotificationChannel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.es3649.execsec.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

/**
 * The Interceptor has a broadcast receiver for incoming SMS messages.
 * It takes higher priority then the Messages app, and then it shoved them through the processor
 * and sends notifications as needed.
 *
 * TODO if it doesn't find an NLPIntent in the message, send the SMS notification back to Messages
 *
 * Created by es3649 on 4/24/19.
 */

public class Interceptor extends BroadcastReceiver {

    private static final String TAG = "interceptor.Interceptor";

    private String msg_body = "";
    private String sender;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle b = intent.getExtras();
            SmsMessage[] msgs;
            if (b != null) {
                try {
                    Object[] pdus = (Object[]) b.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_body += msgs[i].getMessageBody();
                    }

                    sender = msgs[0].getOriginatingAddress();
                } catch (Exception ex) {
                    Log.e(TAG, "Failed to get messages", ex);
                }
            }
        }

        pushNotification(context, msg_body);
        // TODO parse the message body
    }

    private void pushNotification(Context ctx, String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, NotificationCompatSideChannelService.NOTIFICATION_SERVICE)
                .setSmallIcon(R.drawable.envelope)
                .setContentTitle("Text Message!")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.notify(0, builder.build());
    }
}
