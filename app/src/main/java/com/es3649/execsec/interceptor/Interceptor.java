package com.es3649.execsec.interceptor;

import android.app.NotificationChannel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;

import com.es3649.execsec.R;
import com.es3649.execsec.dao.DB_Proxy;
import com.es3649.execsec.nlp.NLPIntent;
import com.es3649.execsec.nlp.Processor;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

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

        if (canITouchThis(context)) {
            // TODO parse the message body
            Processor nlpProcessor = new Processor();
            List<NLPIntent> intents = nlpProcessor.process(msg_body);

            // no perceived intent
            if (intents.isEmpty()) return;

            we//TODO we need a conversation manager package of some kind
            pushNotification(context, msg_body);
        }
    }

    private void pushNotification(Context ctx, String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, NotificationCompatSideChannelService.NOTIFICATION_SERVICE)
                .setSmallIcon(R.drawable.envelope)
                .setContentTitle(PhoneNumberUtils.normalizeNumber(sender))
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.notify(0, builder.build());
    }

    /**
     * looks up the sender in the DB to see if we know them. If we know them, we can mess, if not,
     * then forget about it.
     * @return a boolean indicating DB containment
     */
    private boolean canITouchThis(Context ctx) {
        DB_Proxy db = new DB_Proxy(ctx);
        return !(null == db.lookupPerson(sender));
    }
}