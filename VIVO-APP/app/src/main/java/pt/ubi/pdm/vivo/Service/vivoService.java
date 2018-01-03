package pt.ubi.pdm.vivo.Service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import pt.ubi.pdm.vivo.R;

public class vivoService extends IntentService {

    public vivoService() {
        super("vivoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Here we build the notification that user will see

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "vivo_id_01");
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Vivo - Mountain Center")
                        .setContentText(intent.getStringExtra("content"));

        Intent resultIntent = new Intent(this, Notification.class);
        resultIntent.putExtras(intent.getExtras());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Notification.class);

        // Adds the Intent that starts the Notification Activity
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        // Adds a sound to Notification
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(alarmSound);

        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification build, lets notify user and show notification contents
        mNotificationManager.notify(0, notificationBuilder.build());
    }
}