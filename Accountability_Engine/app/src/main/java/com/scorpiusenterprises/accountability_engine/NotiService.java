package com.scorpiusenterprises.accountability_engine;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by Rick on 2/12/2016.
 */
public class NotiService extends Service {

    Intent intent;

    SharedPreferences pref;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("service bound");

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;

        pref = getSharedPreferences("notiPrefs", Context.MODE_PRIVATE);

        NotiThread notiRunner = new NotiThread();
        Thread thread = new Thread(notiRunner);
        thread.start();

        return START_STICKY;
    }

    class NotiThread implements Runnable {
        @Override
        public void run() {
            int delay = pref.getInt("delay", 0);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            pushNotification();

            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock((
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                            //PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK |
                            //PowerManager.PARTIAL_WAKE_LOCK |
                            PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");

            wakeLock.acquire(5000);
        }
    }

    public void pushNotification() {
        int id = pref.getInt("notiID", 0);

        Notification notification = getNotification(
                pref.getInt("icon", 0),
                pref.getString("title", ""),
                pref.getString("text", ""),
                pref.getString("ticker", ""),
                pref.getInt("color", 0),
                pref.getBoolean("vibrate", false),
                pref.getBoolean("sound", false),
                pref.getBoolean("action", false),
                pref.getBoolean("intent", false));

        NotificationManagerCompat.from(getApplicationContext()).notify(id, notification);

        SharedPreferences.Editor prefEdit = pref.edit();
        prefEdit.putInt("notiID", ++id);
        prefEdit.apply();

        stopSelf();
    }

    public Notification getNotification(int ic, String ti, String te, String tt, int c, boolean v, boolean s, boolean a, boolean in) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
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
                            getApplicationContext(),
                            0,
                            new Intent(getApplicationContext(), NotiActivity.class),
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }
        if (in) {
            System.out.println("intent on");
            builder.setContentIntent(
                    PendingIntent.getActivity(
                            getApplicationContext(),
                            0,
                            new Intent(getApplicationContext(), NotiActivity.class),
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }
        builder.setAutoCancel(true);

        return builder.build();
    }
}
