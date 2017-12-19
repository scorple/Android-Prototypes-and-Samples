package com.scorpiusenterprises.accountability_engine;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class NotiActivity extends AppCompatActivity implements NotiDialog.NotiDialogListener {

    int notiID;

    Button btnBuild;

    RadioButton radNow, rad5, rad10, radTimed;

    TimePicker timePicker;

    SharedPreferences pref;

    int icon, color;
    String title, text, ticker;
    boolean vibrate, sound, action, intent;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("notiPrefs", Context.MODE_PRIVATE);
        notiID = pref.getInt("notiID", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnBuild = (Button) findViewById(R.id.btnBuild);
        btnBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotificationDialog();
            }
        });

        radNow = (RadioButton) findViewById(R.id.radNow);
        rad5 = (RadioButton) findViewById(R.id.rad5);
        rad10 = (RadioButton) findViewById(R.id.rad10);
        radTimed = (RadioButton) findViewById(R.id.radTimed);

        timePicker = (TimePicker) findViewById(R.id.timePicker);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radNow.isChecked()) {
                    Snackbar.make(view, "Notification pushed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    pushNotification();
                }
                if (rad5.isChecked()) {
                    Snackbar.make(view, "Notification pending", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                    SharedPreferences.Editor prefEdit = pref.edit();
                    prefEdit.putInt("delay", 5000);
                    prefEdit.apply();
                    pushDelayedNotification();
                }
                if (rad10.isChecked()) {
                    Snackbar.make(view, "Notification pending", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    SharedPreferences.Editor prefEdit = pref.edit();
                    prefEdit.putInt("delay", 10000);
                    prefEdit.apply();
                    pushDelayedNotification();
                }
                if (radTimed.isChecked()) {
                    Snackbar.make(view, "Notification scheduled", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    scheduleNotification();
                }
            }
        });

        icon = pref.getInt("icon", 0);
        title = pref.getString("title", "");
        text = pref.getString("text", "");
        ticker = pref.getString("ticker", "");
        color = pref.getInt("color", 0);
        vibrate = pref.getBoolean("vibrate", false);
        sound = pref.getBoolean("sound", false);
        action = pref.getBoolean("action", false);
        intent = pref.getBoolean("intent", false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showNotificationDialog() {
        FragmentManager fm = getSupportFragmentManager();
        NotiDialog notiDialog = new NotiDialog();
        notiDialog.show(fm, "noti_dialog");
    }

    @Override
    public void onFinish(int ic, String ti, String te, String tt, int c, boolean v, boolean s, boolean a, boolean in) {
        icon = ic;
        title = ti;
        text = te;
        ticker = tt;
        color = c;
        vibrate = v;
        sound = s;
        action = a;
        intent = in;

        SharedPreferences.Editor prefEdit = pref.edit();
        prefEdit.putInt("icon", icon);
        prefEdit.putString("title", title);
        prefEdit.putString("text", text);
        prefEdit.putString("ticker", ticker);
        prefEdit.putInt("color", color);
        prefEdit.putBoolean("vibrate", vibrate);
        prefEdit.putBoolean("sound", sound);
        prefEdit.putBoolean("action", action);
        prefEdit.putBoolean("intent", intent);
        prefEdit.apply();
    }

    public void pushNotification() {
        notiID = pref.getInt("notiID", 0);

        Notification notification = getNotification(icon, title, text, ticker, color, vibrate, sound, action, intent);
        NotificationManagerCompat.from(this).notify(notiID, notification);

        SharedPreferences.Editor prefEdit = pref.edit();
        prefEdit.putInt("notiID", ++notiID);
        prefEdit.apply();
    }

    public void pushDelayedNotification() {
        Intent serviceIntent = new Intent(this, NotiService.class);
        startService(serviceIntent);
    }

    public void scheduleNotification() {
        notiID = pref.getInt("notiID", 0);

        Intent notificationIntent = new Intent(this, NotiReceiver.class);
        notificationIntent.putExtra("id", notiID);
        notificationIntent.putExtra("icon", icon);
        notificationIntent.putExtra("title", title);
        notificationIntent.putExtra("text", text);
        notificationIntent.putExtra("ticker", ticker);
        notificationIntent.putExtra("color", color);
        notificationIntent.putExtra("vibrate", vibrate);
        notificationIntent.putExtra("sound", sound);
        notificationIntent.putExtra("action", action);
        notificationIntent.putExtra("intent", intent);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notiID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        calendar.set(Calendar.SECOND, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        SharedPreferences.Editor prefEdit = pref.edit();
        prefEdit.putInt("notiID", ++notiID);
        prefEdit.apply();
    }

    public Notification getNotification(int ic, String ti, String te, String tt, int c, boolean v, boolean s, boolean a, boolean in) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
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
                            this,
                            0,
                            new Intent(this, this.getClass()),
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }
        if (in) {
            System.out.println("intent on");
            builder.setContentIntent(
                    PendingIntent.getActivity(
                            this,
                            0,
                            new Intent(this, this.getClass()),
                            PendingIntent.FLAG_UPDATE_CURRENT));
        }
        builder.setAutoCancel(true);

        return builder.build();
    }
}
