package com.es3649.execsec.interceptor;

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
import com.es3649.execsec.data.database.DB_Proxy;
import com.es3649.execsec.data.model.Person;
import com.es3649.execsec.nlp.NLPIntent;
import com.es3649.execsec.nlp.Processor;
import com.es3649.execsec.nlp.conversationmanager.Responder;
import com.es3649.execsec.nlp.conversationmanager.ScheduleTransaction;

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
    private DB_Proxy db = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle b = intent.getExtras();
            SmsMessage[] msgs;
            StringBuilder sb = new StringBuilder();
            if (b != null) {
                try {
                    Object[] pdus = (Object[]) b.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        sb.append(msgs[i].getMessageBody());
                    }

                    sender = msgs[0].getOriginatingAddress();
                    msg_body = sb.toString();
                } catch (Exception ex) {
                    Log.e(TAG, "Failed to get messages", ex);
                }
            }
        }

        DB_Proxy db = new DB_Proxy(context);
        Person p = db.lookupPerson(sender);
        if (p != null) {
            // TODO parse the message body
            Processor nlpProcessor = new Processor();
            List<NLPIntent> intents = nlpProcessor.process(msg_body);

            // no perceived intent
//            if (intents.isEmpty()) return;

            // if we have intent, look for a current transaction
            ScheduleTransaction st = db.lookupTransaction(sender);
            if (st == null) {
                st = new ScheduleTransaction(p);
            }

            // suggest a response
            Responder r = new Responder();
            String response = r.getResponse(st, intents);

            // push the notification with the suggestion

            //TODO we need a conversation manager package of some kind
            new Notifier().pushNotification(context, p, response);
        }
    }


}
