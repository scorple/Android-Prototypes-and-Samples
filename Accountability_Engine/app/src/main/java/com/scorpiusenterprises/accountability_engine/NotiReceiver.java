package com.scorpiusenterprises.accountability_engine;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

public class NotiReceiver extends WakefulBroadcastReceiver {

    Context context;
    Intent intent;

    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;

        pushNotification();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        //PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK |
                        //PowerManager.PARTIAL_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");

        wakeLock.acquire(5000);

        setResultCode(Activity.RESULT_OK);

        completeWakefulIntent(intent);
    }

    public void pushNotification() {
        int id = intent.getIntExtra("id", 0);

        Notification notification = getNotification(
                intent.getIntExtra("icon", 0),
                intent.getStringExtra("title"),
                intent.getStringExtra("text"),
                intent.getStringExtra("ticker"),
                intent.getIntExtra("color", 0),
                intent.getBooleanExtra("vibrate", false),
                intent.getBooleanExtra("sound", false),
                intent.getBooleanExtra("action", false),
                intent.getBooleanExtra("intent", false));

        NotificationManagerCompat.from(context).notify(id, notification);

    }

    public Notification getNotification(int ic, String ti, String te, String tt, int c, boolean v, boolean s, boolean a, boolean in) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        switch(ic) {
            case 0:
                builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
                break;
            case 1:
                builder.setSmallIcon(android.R.drawable.ic_dialog_dialer);
                break;
            case 2:
                builder.setSmallIcon(android.R.drawable.ic_dialog_email);
                break;
            case 3:
                builder.setSmallIcon(android.R.drawable.ic_dialog_info);
                break;
            case 4:
                builder.setSmallIcon(android.R.drawable.ic_dialog_map);
                break;
        }
        if (!(ti == null) && !ti.equalsIgnoreCase("")) {
            System.out.println("title set");
            builder.setContentTitle(ti);
        }
        if (!(te == null) && !te.equalsIgnoreCase("")) {
            System.out.println("text set");
            builder.setContentText(te);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(te));
        }
        if (!(tt == null) && !tt.equalsIgnoreCase("")) {
            System.out.println("ticker text set");
            builder.setTicker(tt);
        }
        switch(c) {
            case 0:
                break;
            case 1:
                System.out.println("lights on");
                builder.setLights(0xFF0000, 500, 500);
                break;
            case 2:
                System.out.println("lights on");
                builder.setLights(0x00FF00, 500, 500);
                break;
            case 3:
                System.out.println("lights on");
                builder.setLights(0x0000FF, 500, 500);
                break;
        }
        if (v) {
            System.out.println("vibrate on");
            builder.setVibrate(new long[]{0, 1000});
        }
        if (s) {
            System.out.println("sound on");
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        if (a) {
            System.out.println("action on");
            builder.addAction(
                    android.R.drawable.ic_popup_reminder,
                    "Action",
                    PendingIntent.getActivity(
                            context,
                            0,
                            new Intent(context, NotiActivity.class),
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }
        if (in) {
            System.out.println("intent on");
            builder.setContentIntent(
                    PendingIntent.getActivity(
                            context,
                            0,
                            new Intent(context, NotiActivity.class),
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }
        builder.setAutoCancel(true);

        return builder.build();
    }
}
