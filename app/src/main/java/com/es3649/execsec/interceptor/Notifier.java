package com.es3649.execsec.interceptor;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.app.NotificationManagerCompat;

import com.es3649.execsec.R;
import com.es3649.execsec.data.model.Person;

/**
 * This class handles assembly and submission of push notifications from the interceptor.
 *
 * Created by es3649 on 4/26/19.
 */

public class Notifier {
    private static final String TAG = "Notifier";

    /**
     * Pushes a notification.
     * The notification header is the name of the sender, and the body is passed as an argument
     * Buttons exist which allow the user to send the suggested response, or edit it.
     * Tapping the notification also takes the user to the message revision screen.
     *
     * TODO I want these notifications to be expandable, and when expanded they will show
     * TODO | the whole message, as well as buttons allowing the user to edit the message
     * TODO | or to send it as is
     *
     * @param ctx a Context from which the notification will be sent
     * @param p the person the message is from/to
     * @param body the body of the notification
     */
    void pushNotification(Context ctx, Person p, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, NotificationCompatSideChannelService.NOTIFICATION_SERVICE)
                .setSmallIcon(R.drawable.envelope)
                .setContentTitle(p.getFullName())
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
        notificationManager.notify(0, builder.build());
    }
}
