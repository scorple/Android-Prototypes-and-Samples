package com.scorpius_enterprises.apps.companion;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by rickm on 2/2/2017.
 */

public class CompanionService extends Service
{
    int             id              = 1;
    CompanionThread companionThread = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String message = intent.getStringExtra("message");
        boolean timedMessageEnabled = intent.getBooleanExtra("timedMessageEnabled", false);

        if (companionThread == null)
        {
            NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext());
            builder.setSmallIcon(android.R.drawable.ic_menu_compass);
            builder.setContentTitle("Companion");
            builder.setContentText("Ready!");

            startForeground(id++, builder.build());

            companionThread = new CompanionThread();
            companionThread.start();
        }

        if (message != null)
        {
            companionThread.nextMessage = message;
            companionThread.pushReady = true;
        }

        companionThread.timedMessageEnabled = timedMessageEnabled;

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private void stop()
    {
        if (companionThread != null)
        {
            companionThread.running = false;
        }
        companionThread = null;
        stopForeground(true);
    }

    private class CompanionThread extends Thread
    {
        boolean running;
        boolean pushReady;
        String  nextMessage;
        boolean timedMessageEnabled;
        long    startTime;
        long    currentTime;

        CompanionThread()
        {
            running = true;
            pushReady = false;
            timedMessageEnabled = false;
        }

        public void run()
        {
            startTime = System.currentTimeMillis();

            CompanionDbHelper dbHelper = new CompanionDbHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                CompanionDataContract.PriorityTask.COLUMN_NAME,
                CompanionDataContract.PriorityTask.COLUMN_PRIORITY,
                CompanionDataContract.PriorityTask.COLUMN_LAST_FIRED
            };

            Cursor cursor = db.query(
                CompanionDataContract.PriorityTask.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
            );

            while (cursor.moveToNext())
            {
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(CompanionDataContract.PriorityTask.COLUMN_NAME)));
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(CompanionDataContract.PriorityTask.COLUMN_PRIORITY)));
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(CompanionDataContract.PriorityTask.COLUMN_LAST_FIRED)));
            }

            cursor.close();

            while (running)
            {
                if (pushReady)
                {
                    NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(getApplicationContext());
                    builder.setSmallIcon(android.R.drawable.ic_menu_edit);
                    builder.setContentTitle("Companion Message");
                    builder.setContentText(nextMessage);

                    NotificationManagerCompat.from(getApplicationContext())
                                             .notify(id++, builder.build());

                    pushReady = false;
                }

                if (timedMessageEnabled)
                {
                    currentTime = System.currentTimeMillis();

                    //pushes a notification every 5 seconds
                    if (currentTime - 5000 > startTime)
                    {
                        startTime = currentTime;

                        NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(getApplicationContext());
                        builder.setSmallIcon(android.R.drawable.ic_menu_edit);
                        builder.setContentTitle("Companion Message");
                        builder.setContentText("Timed Message");

                        NotificationManagerCompat.from(getApplicationContext())
                                                 .notify(id++, builder.build());

                        PowerManager pm =
                            (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                        PowerManager.WakeLock wakeLock = pm.newWakeLock((
                                                                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                                                                            //PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK |
                                                                            //PowerManager.PARTIAL_WAKE_LOCK |
                                                                            PowerManager.ACQUIRE_CAUSES_WAKEUP),
                                                                        "TAG");

                        wakeLock.acquire(5000);

                        cursor = db.query(
                            CompanionDataContract.PriorityTask.TABLE_NAME,
                            projection,
                            null,
                            null,
                            null,
                            null,
                            null
                        );

                        while (cursor.moveToNext())
                        {
                            System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(CompanionDataContract.PriorityTask.COLUMN_NAME)));
                            System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(CompanionDataContract.PriorityTask.COLUMN_PRIORITY)));
                            System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(CompanionDataContract.PriorityTask.COLUMN_LAST_FIRED)));
                        }

                        cursor.close();
                    }
                }
            }

            dbHelper.close();
        }
    }
}
