package com.scorpius_enterprises.apps.companion;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class CompanionActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CompanionDbHelper dbHelper = new CompanionDbHelper(this);
        SQLiteDatabase    db       = dbHelper.getWritableDatabase();
        db.execSQL(CompanionDbHelper.SQL_DELETE_TABLE);
        db.execSQL(CompanionDbHelper.SQL_CREATE_TABLE);

        ContentValues values = new ContentValues();
        values.put(CompanionDataContract.PriorityTask.COLUMN_NAME, "Think happy thoughts");
        values.put(CompanionDataContract.PriorityTask.COLUMN_PRIORITY, 2);
        values.put(CompanionDataContract.PriorityTask.COLUMN_LAST_FIRED, 0);

        db.insert(CompanionDataContract.PriorityTask.TABLE_NAME, null, values);

        db = dbHelper.getReadableDatabase();

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
        dbHelper.close();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pushService();

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
    }

    public void pushService()
    {
        Intent   serviceIntent = new Intent(getApplicationContext(), CompanionService.class);
        EditText txtPrompt     = (EditText) findViewById(R.id.txtPrompt);
        if (!txtPrompt.getText().toString().equals(""))
        {
            serviceIntent.putExtra("message", txtPrompt.getText().toString());
            txtPrompt.setText("");
        }
        serviceIntent.putExtra("timedMessageEnabled",
                               ((Switch) findViewById(R.id.swTimedMessageEnabled)).isChecked());
        startService(serviceIntent);
        Toast.makeText(getApplicationContext(), "Service started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_companion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Intent serviceIntent = new Intent(getApplicationContext(), CompanionService.class);
            stopService(serviceIntent);
            Toast.makeText(getApplicationContext(), "Service stopped", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
